package top.mothership.cb.cmd.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import top.mothership.cb.cmd.model.Sender;

@Data
@SuperBuilder
public class RawCommand {
    private String text;
    private Sender sender;
}
