package com.uptheant.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;
import com.uptheant.demo.config.TestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class DemoApplicationTests {
    @Test
    void contextLoads() {
        
    }
}