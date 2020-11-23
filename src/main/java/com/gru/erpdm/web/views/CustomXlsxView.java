package com.gru.erpdm.web.views;

import com.gru.erpdm.utils.ExportUtils;
import com.gru.erpdm.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("xlsxView")
public class CustomXlsxView extends AbstractXlsxStreamingView {

    @Value("${reports.path}")
    private String reportsPath;

    private final ExportUtils exportUtils;

    public CustomXlsxView(ExportUtils exportUtils) {
        this.exportUtils = exportUtils;
    }

    @Override
    public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {


        assert workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook;

        String fileName = MapUtils.getString(model, "fileName");
        String templateName = MapUtils.getString(model, "templateName");

        exportUtils.applyFileNameForRequest(fileName, workbook, request, response);
        exportUtils.applyContentTypeForRequest(workbook, response);

        // sample datas
        List<User> users = new ArrayList<>();
        users.add(new User("developk1", "김상현1", 33, "ㅇㅇㅇㅇㅇㅇㅇㅇ"));
        users.add(new User("developk2", "김상현2", 32, "ㄴㄴㄴㄴㄴㄴㄴㄴ"));
        users.add(new User("developk3", "김상현3", 31, "ㅊㅊㅊㅊㅊㅊㅊㅊ"));
        users.add(new User("developk4", "김상현4", 30, "ㅁㅁㅁㅁㅁㅁㅁㅁ"));
        users.add(new User("developk5", "김상현5", 29, "ㅋㅋㅋㅋㅋㅋㅋㅋ"));
        users.add(new User("developk6", "김상현6", 28, "ㅂㅂㅂㅂㅂㅂㅂㅂ"));

        Context context = new Context();
        context.putVar("list", users);

        try(InputStream inputStream = new ClassPathResource(reportsPath + "/" + templateName + ".xlsx").getInputStream()) {
            try(OutputStream outputStream = response.getOutputStream()) {
                JxlsHelper.getInstance().processTemplate(inputStream, outputStream, context);
            }
        }

    }

}
