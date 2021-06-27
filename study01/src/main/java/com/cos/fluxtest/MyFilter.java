package com.cos.fluxtest;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {

    private EventNotify eventNotify;

    public MyFilter(EventNotify eventNotify) {
        this.eventNotify = eventNotify;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 실행됨");

        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setContentType("text/event-stream; charset=utf-8");
        // 일반적인 마임타입은 flush 해도 읽지 않고 쌓아놓고 한번에 내보낸다.
        // event-stream 타입은 flush 할때마다 내보낸다.
        PrintWriter out = resp.getWriter();

        // Reactive Streams 라이브러리로 표준을 지켜서 응답할 수 있다. (소비가 끝나면 종료)
        // 기존 웹은 데이터가 10건이면 10건을 모아서 한번에 주지만, 애는 백프레셔로 적용한 수만큼 지속적으로 준다.
        for(int i=0; i<5; i++) {
            out.println("응답 : " + i);
            out.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // SSE Emitter 라이브러리를 쓰면 편하게 쓸 수 있다. (스트림 계속 유지)
        while(true) {
            try {
                if(eventNotify.isChange()) {
                    System.out.println("change event");
                    int lastIndex = eventNotify.getEvents().size() - 1;
                    out.println("응답 : " + eventNotify.getEvents().get(lastIndex));
                    out.flush();
                    eventNotify.setChange(false);
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // WebFlux -> Reactive Streams 가 적용된 Stream 인데 비동기 단일 스레드 동작 + 백프레셔가 적용된 데이터만큼 간헐적 응답이 가능 + 데이터 소비가 끝나면 종료
    // 여기에 SSE 를 적용 시 데이터 소비가 끝나도 Stream 을 계속 유지 (추가적인 응답 가능)
    // Servlet MVC -> Reactive Streams 가 적용된 Stream 인데 멀티 스레드 동작
}
