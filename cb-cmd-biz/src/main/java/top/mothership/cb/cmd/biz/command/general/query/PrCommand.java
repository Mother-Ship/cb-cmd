package top.mothership.cb.cmd.biz.command.general.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.constant.CbCmdPrefix;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdArgument;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor

public class PrCommand extends BaseCommand {
    @CbCmdArgument(CbCmdPrefix.COLON)
    private Integer mode;
}
