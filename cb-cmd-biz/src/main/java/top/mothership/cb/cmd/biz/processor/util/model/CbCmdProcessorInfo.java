package top.mothership.cb.cmd.biz.processor.util.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.processor.util.ProcessorFunction;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
@Builder
public class CbCmdProcessorInfo {
    private String className;
    private String methodName;
    private Class<?> parameterType;
    private Object parameter;
}
