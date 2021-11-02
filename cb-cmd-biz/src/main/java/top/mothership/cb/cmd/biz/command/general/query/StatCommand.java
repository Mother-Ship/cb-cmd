package top.mothership.cb.cmd.biz.command.general.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data

public class StatCommand extends BaseCommand {
    @CbCmdArgument("#")
    private Integer day;

    @CbCmdArgument(":")
    private Integer mode;

    @NotNull(message = "请指定osu! id")
    @CbCmdArgument
    private String osuId;
}
