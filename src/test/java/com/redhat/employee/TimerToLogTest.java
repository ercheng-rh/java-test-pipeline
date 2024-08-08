package com.redhat.employee;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@SpringBootTest(classes = Application.class)
public class TimerToLogTest {

    @Autowired
    CamelContext context;

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:output")
    MockEndpoint mockLogger;

    @BeforeEach
    void beforeEach() throws Exception {
        MockEndpoint.resetMocks(context);
        AdviceWith.adviceWith(context, "timer-to-log", a -> {
            a.replaceFromWith("direct:timer");
            a.weaveById("log-output").replace().to("mock:output");
        });
    }

    @Test
    public void testTimerToLog() throws Exception {
        mockLogger.expectedMessageCount(1);
        producerTemplate.sendBody("direct:timer", "hello world");
        mockLogger.assertIsSatisfied();
    }
}
