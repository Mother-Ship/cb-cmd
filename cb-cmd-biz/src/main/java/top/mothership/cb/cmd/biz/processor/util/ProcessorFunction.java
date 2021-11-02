package top.mothership.cb.cmd.biz.processor.util;

import java.io.Serializable;
import java.util.function.Function;

public interface ProcessorFunction<T,R> extends Function<T,R> , Serializable {
}
