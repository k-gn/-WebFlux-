package com.cos.fluxdemo.service;

import com.cos.fluxdemo.domain.Customer;
import com.cos.fluxdemo.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<Customer> findAll() {
        return customerRepository.findAll().log();
    }

    public Mono<Customer> findById(Long id) {
        return customerRepository.findById(id).log();
    }

    public Mono<Customer> save(Customer customer, Sinks.Many<Customer> sink) {
        return customerRepository.save(customer).doOnNext(c -> {
            sink.tryEmitNext(c);
        });
    }
}
