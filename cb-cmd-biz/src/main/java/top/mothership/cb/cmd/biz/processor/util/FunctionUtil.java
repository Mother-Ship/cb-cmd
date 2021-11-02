package top.mothership.cb.cmd.biz.processor.util;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

@Slf4j
public class FunctionUtil {

    /***
     * 转换方法引用为方法名
     * @param fn 函数引用
     * @return 方法名
     */
    public static <T, R> String toName(ProcessorFunction<T, R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        return lambda.getImplMethodName();
    }

    /***
     * 获取类对应的Lambda
     * @param fn 方法引用
     * @return Lambda
     */
    private static SerializedLambda getSerializedLambda(Serializable fn) {

        SerializedLambda lambda = null;
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(fn);
        } catch (Exception e) {
            log.error("获取SerializedLambda异常, class=" + fn.getClass().getSimpleName(), e);
        }
        return lambda;
    }
}