package com.cos.fluxdemo.domain;

import com.cos.fluxdemo.DBInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
@Import(DBInit.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void test() {
//        customerRepository.findById(1L).subscribe((c) -> System.out.println(c));
        StepVerifier
            .create(customerRepository.findById(2L))
            .expectNextMatches((c) -> {
                return c.getFirstName().equals("Chloe");
            })
            .expectComplete()
            .verify();
    }
}