package com.app

import com.app.controller.PetController
import com.app.domain.Pet
import com.app.repository.PetRepository
import net.datafaker.Faker
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class PetApiTest extends Specification {
    static final BASE_URL = PetController.PATH
    @Autowired
    MockMvc mvc
    @Autowired
    Faker faker
    @Autowired
    PetRepository repository

    def index() {
        given:
            def name = faker.animal().name()
            repository.save(new Pet(name: name))
        expect:
            mvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath('$[0].name', Matchers.is(name)))
    }
}