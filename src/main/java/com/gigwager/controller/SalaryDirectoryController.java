package com.gigwager.controller;

import com.gigwager.model.CityData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SalaryDirectoryController {

    @GetMapping("/salary/directory")
    public String directory() {
        return "salary/directory";
    }
}
