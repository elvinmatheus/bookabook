package br.ufc.tpii.model;

import java.util.List;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;

public class Book {
    private String title, subtitle, edition, isbn, whereWasPublished;
    private List<String> authors;
    private int yearOfPublishment;
    private int howManyTotal = 0, howManyAvailable = 0, howManyReserved = 0;

    /* Método construtor */
    public Book(String title, String subtitle, String edition, String isbn, String whereWasPublished, List<String> authors, int yearOfPublishment, int total) {
        this.title = title;
        this.subtitle = subtitle;
        this.edition = edition;
        this.isbn = isbn;
        this.whereWasPublished = whereWasPublished;
        this.authors = authors;
        this.yearOfPublishment = yearOfPublishment;
        this.setHowManyTotal(total);
        this.setHowManyAvailable(total);
        this.setHowManyReserved(0);
    }

    /** Getters */
    public String getTitle() { return this.title; }
    public String getSubtitle() { return this.subtitle; }
    public String getEdition() { return this.edition; }
    public List<String> getAuthors() { return this.authors; }
    public String getIsbn() { return this.isbn; }
    public String getWhereWasPublished() { return this.whereWasPublished; }
    public int getYearOfPublishment() { return this.yearOfPublishment; }

    public int getHowManyTotal() { return this.howManyTotal; }
    public void setHowManyTotal(int howMany) {
        this.howManyTotal = howMany;
        App.get().control().invoke(new RefreshCmd(RefreshID.BookTotal, this)); // notificamos as views observadoras
    }
    public int getHowManyAvailable() { return this.howManyAvailable; }
    public void setHowManyAvailable(int howMany) {
        this.howManyAvailable = howMany;
        App.get().control().invoke(new RefreshCmd(RefreshID.BookAvailable, this)); // notificamos as views observadoras
    }
    public int getHowManyReserved() { return this.howManyReserved; }
    public void setHowManyReserved(int howMany) {
        this.howManyReserved = howMany;
        App.get().control().invoke(new RefreshCmd(RefreshID.BookReserved, this)); // notificamos as views observadoras
    }
    public int getHowManyRented() {
        return this.getHowManyTotal() - this.getHowManyAvailable() - this.getHowManyReserved();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Book)) return false;
        Book b = (Book)obj;
        return b.isbn.equals(this.isbn);
    }

    /* 
     * retorna true sse this possui os mesmos metadados de @param book,
     * desconsiderando os atributos relativos ao estoque (howManyTotal,
     * howManyAvailable e howManyReserved)
     */
    public boolean isForAllIntentsAndPurposes(Book book) {
        if (book == null) return false;
        return title.equals(book.title) &&
            subtitle.equals(book.subtitle) &&
            edition.equals(book.edition) &&
            isbn.equals(book.isbn) &&
            whereWasPublished.equals(book.whereWasPublished) &&
            authors.equals(book.authors) &&
            yearOfPublishment == book.yearOfPublishment;

    }

    public Book update(Book bookData) {
        if (bookData == null) throw new NullPointerException("Dados do livro não podem ser null");
        this.title = bookData.title;
        this.subtitle = bookData.subtitle;
        this.edition = bookData.edition;
        this.isbn = bookData.isbn;
        this.whereWasPublished = bookData.whereWasPublished;
        this.authors = bookData.authors;
        this.yearOfPublishment = bookData.yearOfPublishment;
        int newTotal = bookData.getHowManyTotal();
        int newAvailable = newTotal - this.getHowManyRented() - this.getHowManyReserved();
        this.setHowManyTotal(newTotal);
        this.setHowManyAvailable(newAvailable);
        App.get().control().invoke(new RefreshCmd(RefreshID.LibraryUpdateBook));
        return this;
    }

    @Override
    public int hashCode() {
        return this.isbn.hashCode();
    }

    public Book copy() {
        Book b = new Book(title, subtitle, edition, isbn, whereWasPublished, authors, yearOfPublishment, this.getHowManyTotal());
        b.setHowManyAvailable(this.getHowManyAvailable());
        b.setHowManyReserved(this.getHowManyReserved());
        return b;
    }

    // public void update()
}

