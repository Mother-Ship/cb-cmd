package biz;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import top.mothership.cb.cmd.Application;

@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = "spring.profiles.active=" + "local")
public class SpringBootLocalTestCase {

}
