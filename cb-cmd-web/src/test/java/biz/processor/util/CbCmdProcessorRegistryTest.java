package biz.processor.util;

import biz.SpringBootLocalTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.mothership.cb.cmd.biz.command.general.query.PrCommand;
import top.mothership.cb.cmd.biz.processor.RecentProcessor;
import top.mothership.cb.cmd.biz.processor.util.CbCmdProcessorRegistry;
import top.mothership.cb.cmd.biz.processor.util.FunctionUtil;
import top.mothership.cb.cmd.biz.processor.util.ProcessorFunction;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;
import top.mothership.cb.cmd.model.response.CbCmdResponse;


class CbCmdProcessorRegistryTest extends SpringBootLocalTestCase {
    @Autowired
    private CbCmdProcessorRegistry cbCmdProcessorRegistry;

    @Test
    public void testGetProcessorInfo() {
        CbCmdProcessorInfo processorInfo =
                cbCmdProcessorRegistry.getProcessorInfo(RawCommand.builder().text("!pr :2").build());

        PrCommand command = new PrCommand();
        command.setCommandName("pr");
        command.setMode("2");

        Assertions.assertEquals(processorInfo,
                CbCmdProcessorInfo.builder()
                        .className(RecentProcessor.class.getName())
                        .methodName(FunctionUtil.toName(RecentProcessor::pr))
                        .parameter(command)
                        .parameterType(command.getClass())
                        .build()

        );
    }

}