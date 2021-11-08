package top.mothership.cb.cmd.biz.processor.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdProcessor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class CbCmdProcessorManager implements BeanPostProcessor {
    private final Map<String,Method> commandMapping = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean,  @Nullable String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            CbCmdProcessor processorInfo = AnnotationUtils.findAnnotation(method, CbCmdProcessor.class);
            if (processorInfo == null) {
                continue;
            }
            log.debug("【白菜-命令模块】启动时扫描命令处理器，{}的处理器为{}",processorInfo.value(), method.getName());
            commandMapping.put(processorInfo.value(), method);
        }
        return bean;
    }

    public Method getByCommandName(String commandName){
        Method m = commandMapping.get(commandName);
        if (m == null) {
            log.warn("命令名称{}未找到对应处理器",commandName);
            throw new RuntimeException("命令名称" + commandName + "未找到对应处理器");
        }
        return m;
    }
}
