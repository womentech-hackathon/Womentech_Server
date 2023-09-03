//package com.womentech.server.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.womentech.server.exception.GeneralException;
//import com.womentech.server.exception.dto.ErrorResponse;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class ExceptionHandlerFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);
//        } catch (GeneralException e) {
//            setErrorResponse(request, response, e);
//        }
//    }
//
//    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, GeneralException e) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        response.setStatus(e.getErrorCode().getCode());
//        response.setContentType("application/json; charset=UTF-8");
//
//        response.getWriter().write(
//                objectMapper.writeValueAsString(ErrorResponse.of(
//                        e.getErrorCode(),
//                        e.getErrorCode().getMessage() + " - " + e.getMessage()
//                ))
//        );
//    }
//}
//
