package com.proxym.libraryapp.book;

import java.util.Objects;

public class ISBN {
    long isbnCode;

    public ISBN() {
    }

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    public long getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISBN)) return false;
        ISBN isbn = (ISBN) o;
        return getIsbnCode() == isbn.getIsbnCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIsbnCode());
    }
}
