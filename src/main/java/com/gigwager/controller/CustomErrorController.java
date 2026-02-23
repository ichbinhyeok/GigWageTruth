package com.gigwager.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, org.springframework.ui.Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                // Return custom 404 JTE template, force noindex
                model.addAttribute("noIndex", true);
                return "error/404";
            } else if (statusCode == 500) {
                model.addAttribute("noIndex", true);
                return "error"; // You might want a generic error page, or just redirect home
            }
        }
        return "redirect:/";
    }
}
