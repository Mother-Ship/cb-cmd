package top.mothership.cb.cmd.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Sender {
    @ApiModelProperty("命令发送者QQ")
    private Long qqId;
}
