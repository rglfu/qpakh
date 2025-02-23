package com.app.controller;

import com.app.mapper.TaskMapper;
import com.app.model.TaskDto;
import com.app.repository.TaskRepository;
import com.app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = TaskController.PATH)
@RequiredArgsConstructor
public class TaskController {
    public static final String PATH = "/api/v1/tasks";
    private final TaskRepository repository;
    private final TaskService service;
    private final TaskMapper mapper;

    @GetMapping(value = "/{id}", produces = "application/json")
    public TaskDto get(@PathVariable("id") Long id) {
        return mapper.to(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(produces = "application/json")
    public TaskDto create(@RequestBody @Valid TaskDto dto) {
        return mapper.to(service.create(mapper.to(dto)));
    }
}