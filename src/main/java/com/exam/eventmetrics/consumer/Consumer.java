package com.exam.eventmetrics.consumer;

import com.exam.eventmetrics.engine.DateTimeProcessor;
import com.exam.eventmetrics.engine.EventProcessor;
import com.exam.eventmetrics.pojoentites.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    private static Integer counter=1;

    @Autowired
    private EventProcessor eventProcessor;

    @Autowired
    private DateTimeProcessor dateTimeProcessor;

    @KafkaListener(topics = "eventmetrics", groupId = "group_json",
            containerFactory = "userKafkaListenerFactory", autoStartup = "true")
    public void consumeEvents(Event event) {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Consumed Message: " + counter++);
        eventProcessor.processMessage(event.getPayload());
        dateTimeProcessor.processDateTimeOfEvents(event);
        System.out.println("----------------------------------------------------------------------");
    }

}
