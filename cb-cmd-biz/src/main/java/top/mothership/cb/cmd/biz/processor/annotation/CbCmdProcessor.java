package top.mothership.cb.cmd.biz.processor.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CbCmdProcessor{
    String value() default "";

    /**
     * 是否由命令处理器自己处理参数，用于参数文本中含有【参数前缀符】的复杂参数
     * @return
     */
    boolean rawParameter() default false;
}
