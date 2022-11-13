package com.example.FenrisBookShopApp.controllers.categories;

import com.example.FenrisBookShopApp.dto.book.BooksPageDto;
import com.example.FenrisBookShopApp.entities.book.BookEntity;
import com.example.FenrisBookShopApp.services.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class RestBooksController {
    private final BookService bookService;

    @Autowired
    public RestBooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("recommended")
    public BooksPageDto getBooksPage(@RequestParam("offset") int page, @RequestParam("limit") int limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(page, limit).getContent());
    }

    @GetMapping("recent")
    public BooksPageDto getRecentBooksPage(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam("offset") int page,
            @RequestParam("limit") int limit
    ) {
        return new BooksPageDto(bookService.getPageOfRecentBooksBetweenDates(page, limit, from, to).getContent());
    }

    @GetMapping("popular")
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") int page, @RequestParam("limit") int limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(page, limit).getContent());
    }

    @GetMapping("genre/{genreId}")
    public BooksPageDto getBooksPageByGenre(
            @PathVariable int genreId,
            @RequestParam("offset") int page,
            @RequestParam("limit") int limit
    ) {
        List<BookEntity> books = bookService.getPageBySlug(page, limit, genreId).getContent();
        return new BooksPageDto(books);
    }
}
