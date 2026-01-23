package com.gigwager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                // Redirect 404s to the Salary Directory instead of a dead end
                // "Traffic Leakage Prevention"
                return "redirect:/salary/directory";
            } else if (statusCode == 500) {
                return "error"; // You might want a generic error page, or just redirect home
            }
        }
        return "redirect:/";
    }
}
