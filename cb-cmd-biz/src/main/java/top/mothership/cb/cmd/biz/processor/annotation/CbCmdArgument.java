package top.mothership.cb.cmd.biz.processor.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CbCmdArgument {
    @AliasFor("prefix")
    String value() default"";

    @AliasFor("value")
    String prefix() default "";
}
