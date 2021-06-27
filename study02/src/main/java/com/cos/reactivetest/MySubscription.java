package com.cos.reactivetest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;

// 구독정보 (구독자, 어떤 데이터를 구독할지를 가지고 있다)
public class MySubscription implements Subscription {

    private Subscriber s;
    private Iterator<Integer> its;

    public MySubscription(Subscriber<? super Integer> s, Iterable<Integer> its) {
        this.s = s;
        this.its = its.iterator();
    }

    @Override
    public void request(long n) {
        while (n > 0) {
            if(its.hasNext()) {
                s.onNext(its.next()); // 데이터가 있으면 구독자에게 전달
            }else {
                s.onComplete(); // 데이터가 없으면 끝
                break;
            }
            n--;
        }
    }

    @Override
    public void cancel() {

    }
}
