package com.gru.erpdm.web.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@DisplayName("인덱스 컨트롤러 테스트 클래스")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("인덱스 페이지 로딩 테스트 메서드")
    public void index_test() throws Exception {
        String body = this.testRestTemplate.getForObject("http://localhost:" + port + "/", String.class);
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

}
