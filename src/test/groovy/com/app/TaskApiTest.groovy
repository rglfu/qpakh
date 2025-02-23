package com.app

import com.app.controller.TaskController
import com.app.domain.Task
import com.app.mapper.TaskMapper
import com.app.model.State
import com.app.model.TaskDto
import com.app.repository.TaskRepository
import com.app.service.TaskService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.spockframework.spring.SpringSpy
import org.spockframework.spring.UnwrapAopProxy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@SpringBootTest
@AutoConfigureMockMvc
class TaskApiTest extends Specification {
    static final BASE_URL = TaskController.PATH
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    MockMvc mvc
    @Autowired
    TaskRepository repository
    @Autowired
    TaskMapper mapper
    @SpringSpy
    @UnwrapAopProxy
    TaskService service

    def createAndGet() {
        given:
            def future = new CompletableFuture<Task>().completeOnTimeout(null, 20, TimeUnit.SECONDS)
            10 * service.process(*_) >> {
                delegate.callRealMethod()
                repository.findById(it[0]).filter {
                    State.DONE == it.getState()
                }.ifPresent {
                    future.complete(it)
                }
            }
        when: 'create new task'
            def dto = new TaskDto(min: 1, max: new Random().nextInt(), count: 10)
            def request = objectMapper.writeValueAsString(dto)
            dto.state = State.IN_PROGRESS
        then:
            def response = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .content(request).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath('$', Matchers.samePropertyValuesAs(dto, "id", "result"), TaskDto))
                    .andExpect(MockMvcResultMatchers.jsonPath('$.result', Matchers.hasSize(0)))
                    .andReturn().getResponse().getContentAsString()
        when: 'get task with "in progress" state'
            def id = objectMapper.readValue(response, TaskDto).id
            dto.id = id
        then:
            mvc.perform(MockMvcRequestBuilders.get("$BASE_URL/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath('$', Matchers.samePropertyValuesAs(dto, "result"), TaskDto))
        when: 'wait till task is done and get it with "done" state'
            dto.state = State.DONE
        then:
            future.get()?.id == id
            mvc.perform(MockMvcRequestBuilders.get("$BASE_URL/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath('$', Matchers.samePropertyValuesAs(dto, "result"), TaskDto))
                    .andExpect(MockMvcResultMatchers.jsonPath('$.result', Matchers.hasSize(dto.count)))
    }

    @Unroll
    def createSame() {
        when:
            def taskDto = new TaskDto(min: 1, max: 10, count: 10)
            def task = mapper.to(taskDto)
            task.state = state
            def id = repository.save(task).id
        then:
            mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .content(objectMapper.writeValueAsString(taskDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath('$.id', same ? Matchers.is(id) : Matchers.greaterThan(id), Long))
        where:
            state             || same
            State.IN_PROGRESS || true
            State.DONE        || false
    }

    @Unroll
    def createInvalid() {
        expect:
            mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                    .content(objectMapper.writeValueAsString(new TaskDto(min: min, max: max, count: count)))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
        where:
            min  | max  | count
            null | 10   | 10
            0    | null | 10
            0    | 10   | null
            0    | 10   | 0
            0    | 10   | -1

    }

    def getNonexistent() {
        expect:
            mvc.perform(MockMvcRequestBuilders.get("$BASE_URL/{id}", -1))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    def cleanup() {
        repository.deleteAllInBatch()
    }
}