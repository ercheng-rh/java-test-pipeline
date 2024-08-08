package com.redhat.employee;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TimerToLog extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("timer://foo?fixedRate=true&period=1000")
                .routeId("timer-to-log")
                .to("log:com.redhat.consulting.order?showAll=true&multiline=true").id("log-output");

    }
}
