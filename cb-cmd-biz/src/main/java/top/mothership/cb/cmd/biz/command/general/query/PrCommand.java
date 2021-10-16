package top.mothership.cb.cmd.biz.command.general.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.biz.command.BaseCommand;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PrCommand extends BaseCommand {
    private Integer day;
    private Integer mode;

}
