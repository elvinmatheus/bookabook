package br.ufc.tpii.model;

import java.time.LocalDateTime;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;

public class Emprestimo  {
    
    private Book book;
    private LocalDateTime rentDate;
    private User user;
    
    private static int PRAZO = 7; // prazo em dias
    public static void changeExpireLimit(int days) {
        Emprestimo.PRAZO = days;
        App.get().control().invoke(new RefreshCmd(RefreshID.SettingsChanged));
    }
    public static int getExpireLimit() {
        return Emprestimo.PRAZO;
    }

    private static int MAXQUANTITY = 3;
    public static void changeMaxQuantity(int quantity) {
        Emprestimo.MAXQUANTITY = quantity;
        App.get().control().invoke(new RefreshCmd(RefreshID.SettingsChanged));
    }
    public static int getMaxQuantity() {
        return Emprestimo.MAXQUANTITY;
    }

    private static double MULTA = 3.00;
    public static void changeMulta(double amount) {
        Emprestimo.MULTA = amount;
        App.get().control().invoke(new RefreshCmd(RefreshID.SettingsChanged));
    }
    public static double getMulta() {
        return Emprestimo.MULTA;
    }

    /**
     * Associa o empréstimo de um livro a um usuário
     * @param book - livro a ser emprestado
     * @param user - usuário associado a esse empréstimo
     */
    public Emprestimo(Book book, User user) {
        this.book = book;
        this.rentDate = LocalDateTime.now();
        this.user = user;
    }

    /* Devolve o livro à biblioteca */
    public void cancel() {
        this.user.getData().devolver(this.book);
    }
    public void resetTime() {
        this.rentDate = LocalDateTime.now();
    }
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpireDate());
    }
    public Book getFreshBook() {
        return App.get().getLibrary().findByISBN(this.book.getIsbn());
    }
    public Book getBook() {
        return this.book;
    }
    public User getUser() {
        return this.user;
    }
    public LocalDateTime getExpireDate() {
        return this.rentDate.plusDays(PRAZO);
    }

    public Emprestimo copy() {
        Emprestimo e = new Emprestimo(book, user);
        e.rentDate = LocalDateTime.from(this.rentDate);
        return e;
    }
}
