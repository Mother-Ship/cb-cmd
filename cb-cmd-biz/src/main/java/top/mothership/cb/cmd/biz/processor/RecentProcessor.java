package top.mothership.cb.cmd.biz.processor;

import org.springframework.stereotype.Component;
import top.mothership.cb.cmd.biz.command.general.query.PrCommand;
import top.mothership.cb.cmd.biz.processor.annotation.CbCmdProcessor;
import top.mothership.cb.cmd.enums.MediaEntityType;
import top.mothership.cb.cmd.model.response.CbCmdResponse;
@Component
public class RecentProcessor {

    @CbCmdProcessor("pr")
    public CbCmdResponse pr(PrCommand command){


        return CbCmdResponse.singleMedia(new byte[2], MediaEntityType.IMAGE);
    }
}
