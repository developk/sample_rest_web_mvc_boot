package com.gru.erpdm.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@DisplayName("엑셀 컨트롤러 테스트 클래스")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExcelControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${reports.save.test.path}")
    private String savePath;

    private RequestCallback requestCallback;
    private ResponseExtractor<Void> responseExtractor;

    @BeforeEach
    public void setupRequests() {
//        this.testRestTemplate.getRestTemplate().getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        for(HttpMessageConverter<?> messageConverter : this.testRestTemplate.getRestTemplate().getMessageConverters()) {
            if(messageConverter instanceof AllEncompassingFormHttpMessageConverter) {
                ((AllEncompassingFormHttpMessageConverter) messageConverter).setCharset(StandardCharsets.UTF_8);
                ((AllEncompassingFormHttpMessageConverter) messageConverter).setMultipartCharset(StandardCharsets.UTF_8);
            }
        }

        this.requestCallback = request -> {
//            request.getHeaders().add("Content-Type", "application/vnd.ms-excel; UTF-8");
//            request.getHeaders().add("Content-Disposition", "attachment; filename=\"템플릿_테스트.xls\"");
//            MediaType type = new MediaType("application", "octet-stream", StandardCharsets.UTF_8);
//            MediaType all = new MediaType("*", "*", StandardCharsets.UTF_8);
//            request.getHeaders().setAccept(Arrays.asList(type, all));
//            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
//            formHttpMessageConverter.setCharset(Charset.forName("EUC-KR"));
//            formHttpMessageConverter.setCharset(StandardCharsets.UTF_8);
//
//            formHttpMessageConverter.write(parts, MediaType.APPLICATION_FORM_URLENCODED, request);
        };

        this.responseExtractor = response -> {
            String filename = response.getHeaders().getContentDisposition().getFilename();
            Path path = Paths.get(savePath + "/" + URLDecoder.decode(filename, StandardCharsets.UTF_8));
            Files.copy(response.getBody(), path, StandardCopyOption.REPLACE_EXISTING);
            return null;
        };
    }

    @Test
    @DisplayName("xls 다운로드 테스트")
    public void report01_xls_test() {
        String uri = "http://localhost:" + port + "/export/report01/xls";
        this.testRestTemplate.execute(URI.create(uri), HttpMethod.GET, null, this.responseExtractor);
    }

    @Test
    @DisplayName("xlsx 다운로드 테스트")
    public void report01_xlsx_test() {
        String uri = "http://localhost:" + port + "/export/report01/xlsx";
        this.testRestTemplate.execute(URI.create(uri), HttpMethod.GET, null, this.responseExtractor);
    }



}
