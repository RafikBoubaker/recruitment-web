package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.impl.CityLibrary;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
        bookRepository = new BookRepository();
        library = new CityLibrary(bookRepository, "La bibliothèque de Paris");

        //TODO add books
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){

    }

    @Test
    public void max_borrow_time_is_1_months_for_students(){

    }

    @Test
    public void max_borrow_time_is_2_months_for_other_members(){

    }

    @Test
    public void student_tarif_is_free_if_book_returned_on_time(){

    }

    @Test
    public void non_student_tarif_is_10_cents_per_day(){

    }

    @Test
    public void member_cannot_borrow_book_if_they_have_late_books(){

    }
}