package top.mothership.cb.cmd.biz.command.general.query;

import lombok.Data;
import top.mothership.cb.cmd.biz.constant.CbCmdPrefix;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;


@Data
public class PrCommand {
    @CbCmdArgument(CbCmdPrefix.COLON)
    private String mode;
}
