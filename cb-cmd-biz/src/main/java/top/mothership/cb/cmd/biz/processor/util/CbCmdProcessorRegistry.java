package top.mothership.cb.cmd.biz.processor.util;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Component;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.command.general.query.PrCommand;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdProcessor;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;
import top.mothership.cb.cmd.model.response.CbCmdResponse;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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


        List<String> prefixList = new ArrayList<>(parameterType.getDeclaredFields().length);

        for (Field declaredField : parameterType.getDeclaredFields()) {
            CbCmdArgument argumentAnnotation = declaredField.getAnnotation(CbCmdArgument.class);
            String prefix = argumentAnnotation.value();

            String[] split = StringUtils.split(text, prefix, 2);

            if (split[1].contains(prefix)){
                log.warn("命令解析错误：命令{}中参数前缀{}重复", text, prefix);
                throw new RuntimeException("命令解析错误：参数前缀重复");
            }
            prefixList.add(prefix);
        }

        Map<String, String> prefixTextMap = split(text, prefixList);

        return param;

    }

    private Map<String, String> split(String s, List<String> separator){
        Map<String,String> result = new HashMap<>(separator.size());
        for (String sep : separator) {
            int i = 0;
            int start;
            int end = 0;
            while (i < s.length()) {
                if (sep.indexOf(s.charAt(i)) >= 0){
                    start = i + 1;
                    for (String otherSep : separator) {
                        if (otherSep.equals(sep)){
                            continue;
                        }
                        int j = 0;
                        while (j < s.length()) {
                            if (otherSep.indexOf(s.charAt(j)) >= 0 && j > i){
                                end = j;
                                break;
                            }
                            j++;
                        }
                    }
                    result.put(sep, s.substring(start, end));
                }
                i++;
            }
        }
        return result;
    }
}
