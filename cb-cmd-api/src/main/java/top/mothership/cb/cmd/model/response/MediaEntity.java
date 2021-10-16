package top.mothership.cb.cmd.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaEntity {
    @ApiModelProperty("媒体内容")
    private byte[] data;
    @ApiModelProperty("媒体类型，目前仅有图片支持与消息穿插")
    private String type;
    @ApiModelProperty("位于消息文本的索引位置")
    private int index;
}
