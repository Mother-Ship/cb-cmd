package top.mothership.cb.cmd.biz.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.model.Sender;

@Data
@NoArgsConstructor
public class BaseCommand {
    private String commandName;
}
