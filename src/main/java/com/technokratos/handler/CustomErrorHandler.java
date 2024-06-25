package com.technokratos.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class CustomErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle404(NoHandlerFoundException ex) {
        log.error("404 error occurred: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Page not found");
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied error occurred: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Access denied");
        return mav;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error occurred: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Authentication failed");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        log.error("An error occurred: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "An unexpected error occurred");
        return mav;
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handle500Error(Throwable ex) {
        log.error("A severe error occurred: ", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "A severe error occurred");
        return mav;
    }
}
