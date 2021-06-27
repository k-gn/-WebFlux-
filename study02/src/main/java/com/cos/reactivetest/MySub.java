package com.cos.reactivetest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

// Subscriber<구독할 데이터타입>
public class MySub implements Subscriber<Integer> {

    private Subscription s;
    private int bufferSize = 3; // 한번에 처리할 개수

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("구독자 : 구독정보 잘 받았어!");
        this.s = s;
        System.out.println("구독자 : 신문 n개씩 매일매일 줘!"); // onNext로 데이터를 계속 주는걸 Flux 라고 한다. (Flux : 0 ~ N 개의 데이터 전달)
        s.request(bufferSize); // 소비자가 한번에 처리할 수 있는 개수를 요청 (백프레셔)
    }

    @Override
    public void onNext(Integer integer) {
        System.out.println("구독 데이터 : " + integer);
        bufferSize--;
        if(bufferSize == 0) {
            System.out.println("하루 지남");
            bufferSize = 3;
            s.request(bufferSize); // 하루마다 n개씩 주는중
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("구독 중 에러");
    }

    @Override
    public void onComplete() {
        System.out.println("구독 완료");
    }
}
