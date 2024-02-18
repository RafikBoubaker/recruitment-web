package com.proxym.libraryapp.library;

import com.proxym.libraryapp.book.Book;
import com.proxym.libraryapp.book.BookRepository;
import com.proxym.libraryapp.member.Member;


import java.time.LocalDate;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryImpl implements Library {

    private BookRepository bookRepository;
    private Map<Member, List<Book>> memberBooks = new HashMap<>();
    private Map<Member, LocalDate> memberLateBooks = new HashMap<>();

    public LibraryImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        if (memberLateBooks.containsKey(member)) {
            throw new HasLateBooksException();
        }
        Book book = bookRepository.findBook(isbnCode);
        if (book != null) {
            book.setBorrowedAt(borrowedAt);
            bookRepository.removeBook(isbnCode);
           /* List<Book> memberBookList = memberBooks.get(member);
            if (memberBookList == null) {
                memberBookList = new ArrayList<>();
                memberBooks.put(member, memberBookList);
            }
            memberBookList.add(book);*/
            memberBooks.computeIfAbsent(member, k -> new ArrayList<>()).add(book);
            return book;
        }
        return null;
    }

    @Override
    public void returnBook(Book book, Member member) {
        List<Book> books = memberBooks.getOrDefault(member, new ArrayList<>());
        if (books.contains(book)) {
            books.remove(book);
            bookRepository.save(book);
            LocalDate borrowedAt = book.getBorrowedAt();
            LocalDate returnedAt = LocalDate.now();
            long daysBorrowed = Duration.between(borrowedAt.atStartOfDay(), returnedAt.atStartOfDay()).toDays();
            member.payBook((int) daysBorrowed);
        }
    }


    @Override
    public void markBookLate(Member member, Book book) {

        memberLateBooks.put(member, LocalDate.now());
        List<Book> borrowedBooks = memberBooks.getOrDefault(member, new ArrayList<>());
        borrowedBooks.remove(book);

    }


   /* public double calculateFee(Member member, Book book, int days) {
        if (member instanceof StudentMember) {

            StudentMember student = (StudentMember) member;
             int freeDays = (student.getYear() == 1) ? 15 : 0;
             int chargeableDays = Math.max(days - freeDays, 0);
              if (days > 30) {
                return (chargeableDays - 30) * 0.10 + 30 * 0.10;
            }  else {
                 return chargeableDays * 0.10;
             }
          } else if (member instanceof ResidentMember) {
            if (days > 60) {
                return 60 * 0.10 + (days - 60) * 0.20;
            } else {
                return days * 0.10;
             }
        }
          return 0.0;
    }*/



}
