package com.proxym.libraryapp.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();

    public void saveAll(List<Book> books){

        for (Book book : books) {
            availableBooks.put(book.isbn, book);
        }
    }

    public void save(Book book){

        availableBooks.put(book.isbn, book);
    }

    public Book findBook(long isbnCode) {

        ISBN isbn = new ISBN(isbnCode);
        return availableBooks.get(isbn);
    }

    public void removeBook(long isbnCode) {
        ISBN isbn = new ISBN(isbnCode);
        availableBooks.remove(isbn);
    }

    public boolean isBookAvailable(Book book) {
        return book != null && availableBooks.containsKey(book.getIsbn());
    }



}
