package top.mothership.cb.cmd.biz;

import org.springframework.web.bind.annotation.RestController;
import top.mothership.cb.cmd.CbCmdListeningApi;
import top.mothership.cb.cmd.model.response.CbCmdResponse;
@RestController
public class CbCmdListener implements CbCmdListeningApi {
    @Override
    public CbCmdResponse cmd(String text) {
        return CbCmdResponse.builder().text("Hello Cabbage").build();
    }
}
