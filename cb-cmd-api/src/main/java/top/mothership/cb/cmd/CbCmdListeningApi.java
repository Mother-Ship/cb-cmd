package top.mothership.cb.cmd;

import org.springframework.web.bind.annotation.RequestMapping;
import top.mothership.cb.cmd.model.response.CbCmdResponse;

@RequestMapping("/rpc")
public interface CbCmdListeningApi {
    @RequestMapping("/cmd")
    CbCmdResponse cmd(String text);
}
