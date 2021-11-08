package biz.processor.util;

import biz.SpringBootJunitTestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.mothership.cb.cmd.biz.command.general.query.StatCommand;
import top.mothership.cb.cmd.biz.processor.StatProcessor;
import top.mothership.cb.cmd.biz.processor.util.CbCmdProcessorRegistry;
import top.mothership.cb.cmd.biz.processor.util.FunctionUtil;
import top.mothership.cb.cmd.biz.processor.util.model.CbCmdProcessorInfo;
import top.mothership.cb.cmd.model.RawCommand;


class CbCmdProcessorRegistryTest extends SpringBootJunitTestCase {
    @Autowired
    private CbCmdProcessorRegistry cbCmdProcessorRegistry;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void testGetProcessorInfo() {
        CbCmdProcessorInfo processorInfo =
                cbCmdProcessorRegistry.getProcessorInfo(RawCommand.builder().text("!stat Mother Ship :3 #2").build());

        StatCommand command = new StatCommand();
        command.setOsuId("Mother Ship");
        command.setMode("3");
        command.setDay("2");

        Assertions.assertEquals(
                objectMapper.writeValueAsString(CbCmdProcessorInfo.builder()
                        .className(StatProcessor.class.getName())
                        .methodName(FunctionUtil.toName(StatProcessor::stat))
                        .parameter(command)
                        .parameterType(command.getClass())
                        .build()),
                objectMapper.writeValueAsString(processorInfo)

        );
    }

}