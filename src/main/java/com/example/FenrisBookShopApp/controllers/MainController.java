package com.example.FenrisBookShopApp.controllers;

import com.example.FenrisBookShopApp.entities.book.BookEntity;
import com.example.FenrisBookShopApp.services.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainController {
    private final BookService bookService;

    @Autowired
    public MainController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 20).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 20).getContent();
    }

    @ModelAttribute("popular")
    public List<BookEntity> popular() {
        return bookService.getPageOfPopularBooks(0, 20).getContent();
    }

    @GetMapping(value = "", name = "app.main")
    public String mainPage() {
        return "index";
    }
}
