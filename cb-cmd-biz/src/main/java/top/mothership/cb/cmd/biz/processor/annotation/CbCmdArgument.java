package top.mothership.cb.cmd.biz.processor.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CbCmdArgument {
    /**
     * 该参数的前缀字符，只能从CbCmdPrefix中取
     * @see top.mothership.cb.cmd.biz.constant.CbCmdPrefix
     * @return 该参数的前缀字符
     */
    String value() default"";


}
