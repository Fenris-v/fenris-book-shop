package com.example.FenrisBookShopApp.services.book;

import com.example.FenrisBookShopApp.entities.book.BookEntity;
import com.example.FenrisBookShopApp.repositories.book.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookEntity> getPageOfRecommendedBooks(int pageNumber, int limit) {
        return bookRepository.findAll(PageRequest.of(pageNumber, limit));
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String query, int pageNumber, int limit) {
        return bookRepository.findBookByTitleContainingIgnoreCaseOrderByTitle(query, PageRequest.of(pageNumber, limit));
    }

    public Page<BookEntity> getPageOfRecentBooks(int pageNumber, int limit) {
        LocalDateTime date = LocalDate.now().atStartOfDay().minusMonths(1);
        return bookRepository.findBooksRecent(date, PageRequest.of(pageNumber, limit));
    }

    public Page<BookEntity> getPageOfPopularBooks(int pageNumber, int limit) {
        return bookRepository.getPopularBooks(PageRequest.of(pageNumber, limit));
    }

    public Page<BookEntity> getPageByGenreId(int pageNumber, int limit, Long genreId) {
        return bookRepository.getByGenreId(genreId, PageRequest.of(pageNumber, limit));
    }


    public Page<BookEntity> getPageOfRecentBooksBetweenDates(int page, int limit, String from, String to) {
        if (from == null || to == null) {
            return getPageOfRecentBooks(page, limit);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return bookRepository.findBooksByPubDateBetweenOrderByPubDateDesc(LocalDate.parse(from, formatter).atStartOfDay(), LocalDate.parse(to, formatter).atTime(LocalTime.MAX), PageRequest.of(page, limit));
    }

    public Page<BookEntity> findBooksByAuthorId(@NotNull Long authorId) {
        return bookRepository.findBooksByAuthorId(authorId.intValue(), PageRequest.of(0, 20));
    }

    public Page<BookEntity> findBooksByTagId(@NotNull Long tagId) {
        return bookRepository.findBooksByTagId(tagId, PageRequest.of(0, 20));
    }

    public Page<BookEntity> findBooksByGenreId(@NotNull Long genreId) {
        return bookRepository.findBooksByGenreId(genreId.intValue(), PageRequest.of(0, 20));
    }

    public Page<BookEntity> getPageByTagId(int page, int limit, Long tagId) {
        return bookRepository.findBooksByTagId(tagId, PageRequest.of(page, limit));
    }
}
