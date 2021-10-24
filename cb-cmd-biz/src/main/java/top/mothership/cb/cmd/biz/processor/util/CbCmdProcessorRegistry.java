package top.mothership.cb.cmd.biz.processor.util;


import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdProcessor;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;
import top.mothership.cb.cmd.model.response.CbCmdResponse;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
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

        BaseCommand baseCommand = toBaseCommand(raw);

        Method method = cbCmdProcessorManager.getByCommandName(baseCommand.getCommandName());

        CbCmdProcessor processorInfo = AnnotationUtils.findAnnotation(method, CbCmdProcessor.class);

        if (processorInfo != null && processorInfo.rawParameter()) {
            return CbCmdProcessorInfo.builder()
                    .className(method.getDeclaringClass().getName())
                    .methodName(method.getName())
                    .parameterType(String.class)
                    .parameter(raw.getText())
                    .build();
        }

        Class<?> parameterClz = method.getParameterTypes()[0];

        Object parameter = getParameterByText(parameterClz, raw.getText());

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
    private BaseCommand toBaseCommand(RawCommand raw) {
        String text = raw.getText();

        int start = StringUtils.indexOfAny(text, new char[]{'!', '！'});
        int end = StringUtils.indexOf(text, " ");
        String commandName = text.substring(start, end);

        if (Objects.equals("sudo", commandName)) {
            end = StringUtils.ordinalIndexOf(text, " ", 2);
            commandName = text.substring(start, end);
        }

        return BaseCommand.builder().commandName(commandName).build();
    }

    /**
     * 拆解命令参数
     * 1. 如果是
     *
     * @param parameterType
     * @param text
     * @return
     */
    private Object getParameterByText(Class<?> parameterType, String text) {
        return null;

    }
}
