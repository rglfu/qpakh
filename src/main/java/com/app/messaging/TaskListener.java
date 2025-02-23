package com.app.messaging;

import com.app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskListener {
    private final TaskService service;

    @KafkaListener(topics = "${app.kafka.task.topic}", groupId = "${app.kafka.task.group}")
    public void listen(Long taskId) {
        service.process(taskId);
    }
}