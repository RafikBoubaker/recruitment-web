package com.proxym.libraryapp.library;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proxym.libraryapp.book.Book;
import com.proxym.libraryapp.book.BookRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.proxym.libraryapp.member.Member;
import com.proxym.libraryapp.member.ResidentMember;
import com.proxym.libraryapp.member.StudentMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;
    private static List<Book> books;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new JavaTimeModule())
                .build();
        File booksJson = new File("src/test/resources/books.json");
        books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {});

        bookRepository = new BookRepository();
        bookRepository.saveAll(books);
        library = new LibraryImpl(bookRepository);

    }

    @Test
    void member_can_borrow_a_book_if_book_is_available() {

        Member member = new StudentMember("Jack Sparrow", 2);
        Book book = books.get(0);
        Book borrowedBook = library.borrowBook(book.getIsbn().getIsbnCode(), member, LocalDate.now());

        Assertions.assertNotNull(borrowedBook);
        Assertions.assertFalse(bookRepository.isBookAvailable(book));
    }

    @Test
    void borrowed_book_is_no_longer_available() {
        Member member = new ResidentMember("Jack Sparrow");
        Book book = books.get(0);
        library.borrowBook(book.getIsbn().getIsbnCode(), member, LocalDate.now());

        Assertions.assertFalse(bookRepository.isBookAvailable(book));
    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        Member resident = new ResidentMember("Jack Resident");
        Book book = books.get(0);
        LocalDate borrowedAt = LocalDate.now().minusDays(30);
        library.borrowBook(book.getIsbn().getIsbnCode(), resident, borrowedAt);
        library.returnBook(book, resident);

      Assertions.assertEquals(-3.0, resident.getWallet());
    }

    @Test
    void students_pay_10_cents_the_first_30days() {

        Member student = new StudentMember("Jack Student", 2);
        Book book = books.get(0);
        LocalDate borrowedAt = LocalDate.now().minusDays(30);
        library.borrowBook(book.getIsbn().getIsbnCode(), student, borrowedAt);
        library.returnBook(book, student);

        Assertions.assertEquals(-3.0, student.getWallet());

    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days() {

        Member firstYearStudent = new StudentMember("newJack", 1);
        Book book = books.get(0);
        LocalDate borrowedAt = LocalDate.now().minusDays(15);
        library.borrowBook(book.getIsbn().getIsbnCode(), firstYearStudent, borrowedAt);
        library.returnBook(book, firstYearStudent);

        Assertions.assertEquals(0.0, firstYearStudent.getWallet());

    }

    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        Member resident = new ResidentMember("Jack Resident");
        Book book = books.get(0);
        LocalDate borrowedAt = LocalDate.now().minusDays(61);
        library.borrowBook(book.getIsbn().getIsbnCode(), resident, borrowedAt);
        library.returnBook(book, resident);

        double delta = 0.001; // -->ptit tolerance

        Assertions.assertEquals(-6.2, resident.getWallet(),delta);
    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books() {


        Member member = new StudentMember("Jack Sparrow", 2);
        Book book1 = books.get(0);
        Book book2 = books.get(1);
        LocalDate borrowedAt = LocalDate.now().minusDays(40); // Simulate borrowing 40 days ago
        library.borrowBook(book1.getIsbn().getIsbnCode(), member, borrowedAt);
        library.markBookLate(member, book1);

        boolean isBorrowed;
        try {
            library.borrowBook(book2.getIsbn().getIsbnCode(), member, LocalDate.now());
            isBorrowed = true;
        } catch (HasLateBooksException e) {
            isBorrowed = false;
        }

        Assertions.assertFalse(isBorrowed);
    }
}
