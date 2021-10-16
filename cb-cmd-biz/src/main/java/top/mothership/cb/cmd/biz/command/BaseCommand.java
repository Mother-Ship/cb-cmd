package top.mothership.cb.cmd.biz.command;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.model.Sender;

@Data
@SuperBuilder
public class BaseCommand {
    private String commandName;
    private Sender sender;
}
