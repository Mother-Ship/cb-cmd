package top.mothership.cb.cmd.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Data
@Builder
@Jacksonized
public class CbCmdResponse {
    @ApiModelProperty("消息体文本")
    private String text;
    @ApiModelProperty("媒体信息")
    private List<MediaEntity> media;

    public static CbCmdResponse empty(){
        return CbCmdResponse.builder().build();
    }

    public boolean isEmpty(){
        return this.text == null && CollectionUtils.isEmpty(media);
    }
}
