package com.app

import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfiguration {
    @Bean
    Faker faker() {
        new Faker(new Random(0))
    }
}