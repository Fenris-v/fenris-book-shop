package com.example.FenrisBookShopApp.config;

import com.example.FenrisBookShopApp.dto.SearchQueryDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Service
public class SearchAddingHandlerInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
            @NotNull Object handler, ModelAndView modelAndView
    ) {
        Pattern regex = Pattern.compile("^/search/.*", Pattern.CASE_INSENSITIVE);
        boolean searchRoute = regex.matcher(request.getRequestURI()).matches();

        if (!searchRoute && modelAndView != null) {
            modelAndView.getModelMap().addAttribute("searchQueryDto", new SearchQueryDto());
        }
    }
}
