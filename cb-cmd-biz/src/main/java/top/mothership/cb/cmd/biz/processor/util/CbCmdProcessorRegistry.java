package top.mothership.cb.cmd.biz.processor.util;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import top.mothership.cb.cmd.biz.constant.CbCmdPrefix;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdProcessor;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;
import top.mothership.cb.cmd.model.response.CbCmdResponse;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CbCmdProcessorRegistry {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CbCmdProcessorManager cbCmdProcessorManager;

    @PostConstruct
    public void checkRoutingAmbiguous() {

    }

    @PostConstruct
    public void checkArgumentAmbiguous() {

    }

    public CbCmdProcessorInfo getProcessorInfo(RawCommand raw) {

        String commandName = getCommandName(raw);

        Method method = cbCmdProcessorManager.getByCommandName(commandName);

        CbCmdProcessor processorInfo = AnnotationUtils.findAnnotation(method, CbCmdProcessor.class);

        //如果命令标明了要自己处理参数，而不是依赖注解，则默认入参为String类型
        if (processorInfo != null && processorInfo.rawParameter()) {
            return CbCmdProcessorInfo.builder()
                    .className(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .parameterType(String.class)
                    .parameter(raw.getText())
                    .build();
        }

        //否则从命令文本中解析出参数对象
        Class<?> parameterClz = method.getParameterTypes()[0];

        Object parameter = getParameterByText(parameterClz,
                raw.getText().replace(commandName, ""));

        return CbCmdProcessorInfo.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterType(parameterClz)
                .parameter(parameter)
                .build();
    }

    @SneakyThrows
    public CbCmdResponse invokeProcessor(CbCmdProcessorInfo info) {
        Class<?> clz = Class.forName(info.getClassName());
        Object o = applicationContext.getBean(clz);
        Method m = clz.getMethod(info.getMethodName(), info.getParameterType());
        return (CbCmdResponse) m.invoke(o, info.getParameter());
    }

    /**
     * 拆解命令名称
     * 1. 取第一个感叹号 和 第一个空格之间的文本
     * 2. 如果是sudo，则取第一个感叹号 到第二个空格之间的文本
     *
     * @param raw
     * @return
     */
    private String getCommandName(RawCommand raw) {
        String text = raw.getText();

        int start = StringUtils.indexOfAny(text, new char[]{'!', '！'});
        int end = StringUtils.indexOf(text, " ");
        String commandName = text.substring(start, end);

        if (Objects.equals("sudo", commandName)) {
            end = StringUtils.ordinalIndexOf(text, " ", 2);
            commandName = text.substring(start, end);
        }

        return commandName;
    }

    /**
     * 拆解命令参数
     * 1. 检查所有参数前缀在命令中出现的次数，多余一次直接报错，强制要求命令自己处理参数
     * 2. 切割出每个参数前缀<->空格之间的字符串，设置到对应注解的字段中
     *
     * @param parameterType
     * @param text
     * @return
     */
    @SneakyThrows
    private Object getParameterByText(Class<?> parameterType, String text) {

        Object param = parameterType.getDeclaredConstructor().newInstance();


        List<CbCmdArgument> annotations = Arrays.stream(parameterType.getDeclaredFields())
                .map(field -> field.getAnnotation(CbCmdArgument.class)).toList();

        List<Character> prefixList = annotations.stream()
                .map(a -> getValidPrefix(a.value(), text))
                .toList();

        Map<Character, String> prefixTextMap = split(text, prefixList);

        for (Field declaredField : parameterType.getDeclaredFields()) {
            CbCmdArgument argumentAnnotation = declaredField.getAnnotation(CbCmdArgument.class);
            Character prefix = argumentAnnotation.value();
            String value = prefixTextMap.get(prefix);
            if (value != null) {
                declaredField.setAccessible(true);
                declaredField.set(param, value);
            }
        }

        return param;

    }

    private Character getValidPrefix(Character prefix, String text) {
        if (CbCmdPrefix.isNothing(prefix)) {
            return prefix;
        }

        String[] split = StringUtils.split(text, String.valueOf(prefix), 2);

        if (split[1].indexOf(prefix) != -1) {
            log.warn("命令解析错误：命令{}中参数前缀{}重复", text, prefix);
            throw new RuntimeException("命令解析错误：参数前缀重复");
        }
        return prefix;
    }

    private Map<Character, String> split(String s, List<Character> separatorList) {
        Map<Character, String> result = new HashMap<>(separatorList.size());
        for (Character currentSeparator : separatorList) {

            //如果是空前缀的参数，则取文本开头 到第一个非空前缀之间的内容，再去除头尾空格
            if (CbCmdPrefix.isNothing(currentSeparator)) {
                int end = findNextSeparator(s, separatorList, currentSeparator);
                result.put(currentSeparator, s.substring(0, end).trim());
                continue;
            }

            //如果是普通前缀的参数，则遍历字符串，找到当前前缀 和字符串顺序内下一个前缀之间的内容，再去除头尾空格
            int i = 0;
            while (i < s.length()) {
                if (currentSeparator.equals(s.charAt(i))) {
                    int start = i + 1;
                    int end = findNextSeparator(s, separatorList, currentSeparator);
                    result.put(currentSeparator, s.substring(start, end).trim());
                }
                i++;
            }
        }
        return result;
    }

    private int findNextSeparator(String s, List<Character> separator, Character currentSeparator) {
        int index  = s.indexOf(currentSeparator);

        int j = 0;
        while (j < s.length()) {
            if (separator.contains(s.charAt(j))
                    && !CbCmdPrefix.isNothing(s.charAt(j))
                    && j > index) {
                return j;
            }
            j++;
        }

        //没有找到下一个分隔符，则返回最后一个字符的位置
        return j;
    }

}
