package com.cos.fluxdemo.domain;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

// DB (RDBMS) 는 사실 비동기 처리를 못한다
// MongoDB 같은 DB는 비동기를 지원하지만 오라클이나 MySQL 같은 DB는 지원하지 않는다. == JPA 같은거 못쓴다.
// 하지만 최근에 나온 R2DBC 를 사용해서 해당 DB 들도 비동기 처리가 가능해졌다. 여긴 JpaRepository 대신 ReactiveRepository 를 사용한다.
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer WHERE last_name = :lastname")
    Flux<Customer> findByLastName(String lastName);

}
