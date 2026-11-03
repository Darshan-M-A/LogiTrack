package com.example.logitrack.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public String invalidStatus(IllegalArgumentException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "bad-request";
    }
}
