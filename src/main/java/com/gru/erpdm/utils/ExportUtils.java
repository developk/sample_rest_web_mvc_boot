package com.gru.erpdm.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class ExportUtils {

    // 응답헤더 기록
    public void applyContentTypeForRequest(Workbook workbook, HttpServletResponse response) {
        if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; UTF-8");
        }
        if (workbook instanceof HSSFWorkbook) {
            response.setHeader("Content-Type", "application/vnd.ms-excel; UTF-8");
        }
        response.setCharacterEncoding("UTF-8");
    }

    // 파일명 설정
    public void applyFileNameForRequest(String fileName, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String encodedFileName = FileNameEncoder.encode(userAgent.getBrowser().getGroup(), fileName);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + appendFileExtension(encodedFileName, workbook) + "\"");
    }

    private String appendFileExtension(String fileName, Workbook workbook) {
        if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
            fileName += ".xlsx";
        }
        if (workbook instanceof HSSFWorkbook) {
            fileName += ".xls";
        }

        return fileName;
    }

    private enum FileNameEncoder {
        IE(Browser.IE, it -> {
            try {
                return URLEncoder.encode(it, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                return it;
            }
        }),
        FIREFOX(Browser.FIREFOX, getDefaultEncodeOperator()),
        OPERA(Browser.OPERA, getDefaultEncodeOperator()),
        CHROME(Browser.CHROME, getDefaultEncodeOperator()),
        UNKNOWN(Browser.UNKNOWN, UnaryOperator.identity()),
        BOT(Browser.BOT, UnaryOperator.identity());

        private final Browser browser;
        private UnaryOperator<String> encodeOperator;

        private static final Map<Browser, Function<String, String>> FILE_NAME_ENCODER_MAP;

        static {
            FILE_NAME_ENCODER_MAP = EnumSet.allOf(FileNameEncoder.class).stream()
                    .collect(Collectors.toMap(FileNameEncoder::getBrowser, FileNameEncoder::getEncodeOperator));
        }

        FileNameEncoder(Browser browser, UnaryOperator<String> encodeOperator) {
            this.browser = browser;
            this.encodeOperator = encodeOperator;
        }

        protected Browser getBrowser() {
            return browser;
        }

        protected UnaryOperator<String> getEncodeOperator() {
            return encodeOperator;
        }

        private static UnaryOperator<String> getDefaultEncodeOperator() {
            return it -> new String(it.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        public static String encode(Browser browser, String fileName) {
            return FILE_NAME_ENCODER_MAP.get(browser).apply(fileName);
        }
    }
}
