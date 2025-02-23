package com.app.service;

import com.app.domain.Task;
import com.app.model.State;
import com.app.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final KafkaTemplate<Long, Long> kafkaTemplate;
    @Value("${app.kafka.task.topic}")
    private String topic;

    public Task create(Task task) {
        return repository.findOne(Example.of(task)).orElseGet(() -> {
            var saved = repository.saveAndFlush(task);
            kafkaTemplate.send(topic, saved.getId());
            return saved;
        });
    }

    public void process(Long taskId) {
        repository.findById(taskId).filter(task -> State.IN_PROGRESS.equals(task.getState())).ifPresent(task -> {
            var result = task.getResult();
            result.add((int) (Math.random() * task.getMax() + task.getMin()));
            if (result.size() == task.getCount()) {
                task.setState(State.DONE);
                repository.save(task);
            } else {
                repository.saveAndFlush(task);
                kafkaTemplate.send(topic, task.getId());
            }
        });
    }
}