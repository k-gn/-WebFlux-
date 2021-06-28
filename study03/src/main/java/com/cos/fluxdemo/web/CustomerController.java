package com.cos.fluxdemo.web;

import com.cos.fluxdemo.domain.Customer;
import com.cos.fluxdemo.domain.CustomerRepository;
import com.cos.fluxdemo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    // Sink : 요청에 대한 응답 스트림을 merge 한 것
    // A 요청 -> Flux -> stream
    // B 요청 -> Flux -> stream
    // -> Flux.merge -> Sink

    // many().multicast(): 새로 푸시 된 데이터 만 구독자에게 전송하여 배압을 준수하는 싱크 ( "구독자의 구독 후"에서처럼 새로 푸시 됨).
    // many().unicast(): 위와 동일하며 첫 번째 구독자 레지스터가 버퍼링되기 전에 푸시 된 데이터가 왜곡됩니다.
    // many().replay(): 푸시 된 데이터의 지정된 기록 크기를 새 구독자에게 재생 한 다음 새 데이터를 계속해서 실시간으로 푸시하는 싱크입니다.
    // one(): 구독자에게 단일 요소를 재생하는 싱크
    // empty(): 가입자에게만 터미널 신호를 재생하지만 (오류 또는 완료) 여전히 Mono<T>(일반 유형에주의) 로 볼 수있는 싱크 <T>.
    // onBackpressureBuffer() : 배압 이슈가 발생했을 때 별도의 버퍼에 저장
    private final Sinks.Many<Customer> sink = Sinks.many().multicast().onBackpressureBuffer();

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        // Flux.just : 데이터를 순차적으로 꺼내서 던저준다.
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE) // 한번의 onNext 마다 flush 한다.
    public Flux<Integer> fluxstream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/customer")
    public Flux<Customer> findAll() {

        return customerRepository.findAll().log(); // log() - return 되면서 로그가 같이 남는다.
        // onSubscribe (구독) -> 구독정보 받음 -> request (별 다른 지정없을 시 전부 가져옴) -> onNext (request 갯수만큼 실행) -> onComplete
    }

    @GetMapping("/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id) { // 한 건 리턴 시 Mono
        return customerRepository.findById(id).log();
    }

    @GetMapping("/customer/sse") // SSE - 응답 스트림 유지
    public Flux<ServerSentEvent<Customer>> findAllSSE() {
        return sink.asFlux().map(c -> ServerSentEvent.builder(c).build()).doOnCancel(() -> {
            sink.asFlux().blockLast();
            // 재요청이 안되는 문제 -> 연결 취소해도 취소된줄 모른다.
            // 따라서 연결 끊을 시 마지막 데이터라고 알려줌 -> onComplete 호출, 중간에 끊어도 다시 연결 가능
        }); // produces = MediaType.TEXT_EVENT_STREAM_VALUE 생략 가능 (text/event-stream 포맷 = 서버에 지속적인 연결)
    }

    @PostMapping("/customer")
    public Mono<Customer> save() {
        return customerRepository.save(new Customer("gildong", "Hong")).doOnNext(c -> {
            sink.tryEmitNext(c);
        });
    }

    // javascript 등으로 text/event-stream 포맷의 데이터를 구독할 수 있다. (EventSource)
    // 그리고 구독한 데이터를 받는 이벤트를 만들고, 페이지를 설계할 수 있다.
    // 지금은 화면이 로딩되는 화면이지만 원래는 응답이 완료된 웹페이지를 보여주고 내부적으로 로딩되면서 비동기 통신을 한다.
}
