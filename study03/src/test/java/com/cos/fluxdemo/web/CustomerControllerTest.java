package com.cos.fluxdemo.web;

import com.cos.fluxdemo.domain.Customer;
import com.cos.fluxdemo.domain.CustomerRepository;
import com.cos.fluxdemo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 통합 테스트
//@SpringBootTest
//@AutoConfigureWebTestClient

@WebFluxTest
@Import(CustomerService.class)
class CustomerControllerTest {

    @MockBean // 가짜 객체 생성 후 Spring ApplicationContext 에 등록
    CustomerRepository customerRepository;

    @Autowired
    private WebTestClient webClient; // 비동기 http 요청 객체

    @Test
    public void selectOne() {

        Mono<Customer> givenData = Mono.just(new Customer("Jack", "Bauer"));

        // stub -> 행동 지시
        when(customerRepository.findById(1L)).thenReturn(givenData);

        webClient
            .get()
                .uri("/customer/{id}", 1L)
                .exchange() // Response return
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Jack")
                .jsonPath("$.lastName").isEqualTo("Bauer")
        ;

    }
}