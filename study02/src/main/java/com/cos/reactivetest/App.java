package com.cos.reactivetest;

public class App {
    public static void main(String[] args) {
        MyPub pub = new MyPub();
        MySub sub = new MySub();

        pub.subscribe(sub); // 구독 시작

    }
}
