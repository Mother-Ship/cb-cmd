package top.mothership.cb.cmd.biz;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.mothership.cb.cmd.CbCmdListeningApi;
import top.mothership.cb.cmd.biz.command.BaseCommand;
import top.mothership.cb.cmd.biz.processor.util.CbCmdProcessorRegistry;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;
import top.mothership.cb.cmd.model.response.CbCmdResponse;

@RestController
public class CbCmdListener implements CbCmdListeningApi {
    @Autowired
    private CbCmdProcessorRegistry registry;

    @SneakyThrows
    @Override
    public CbCmdResponse cmd(RawCommand command) {

        //将命令发送者用户信息设置进上下文
        setContext(command);

        //找到对应处理方法
        CbCmdProcessorInfo info = registry.getProcessorInfo(command);

        //反射调用
        CbCmdResponse o = registry.invokeProcessor(info);

        return o;

    }

    private void setContext(RawCommand command) {

    }
}
