package top.mothership.cb.cmd.biz.command.general.query;

import lombok.Data;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;

import javax.validation.constraints.NotNull;

import static top.mothership.cb.cmd.biz.constant.CbCmdPrefix.*;


@Data
public class StatCommand {
    @CbCmdArgument(COMMENT)
    private String day;

    @CbCmdArgument(COLON)
    private String mode;

    @NotNull(message = "请指定osu! id")
    @CbCmdArgument
    private String osuId;
}
