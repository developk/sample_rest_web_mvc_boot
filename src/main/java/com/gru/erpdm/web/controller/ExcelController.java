package com.gru.erpdm.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("export")
public class ExcelController {

    @Value("${reports.01.name}")
    private String report01Name;

    @RequestMapping("report01/{excelType}")
    public String report01(@PathVariable Optional<String> excelType, Model model) {
        String fileType = excelType.isPresent() ? excelType.get() : "xlsx";

        model.addAttribute("templateName", report01Name);
        model.addAttribute("fileName", "한글_테스트_01");

        if ("xlsx".equals(fileType)) {
            return "xlsxView";
        } else if ("xls".equals(fileType)) {
            return "xlsView";
        }

        return "errors";
    }

}
