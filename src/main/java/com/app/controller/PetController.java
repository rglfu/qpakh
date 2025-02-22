package com.app.controller;

import com.app.mapper.PetMapper;
import com.app.model.PetDto;
import com.app.repository.PetRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = PetController.PATH)
@RequiredArgsConstructor
public class PetController {
    public static final String PATH = "/api/v1/pets";
    private final PetRepository repository;
    private final PetMapper mapper;

    @GetMapping(produces = "application/json")
    public List<PetDto> index() {
        return mapper.to(repository.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public PetDto get(@PathVariable("id") Long id) {
        return mapper.to(repository.getReferenceById(id));
    }

    @PostMapping(produces = "application/json")
    public PetDto get(@RequestBody @Valid PetDto dto) {
        return mapper.to(repository.save(mapper.to(dto)));
    }
}