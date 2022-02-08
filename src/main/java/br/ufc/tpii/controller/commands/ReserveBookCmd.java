package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;

public class ReserveBookCmd implements Command {

    /** Reserva um livro */

    Book book; // livro a ser reservado
    User user; // usuário a reservar o livro
    public ReserveBookCmd(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    @Override
    public void execute() {
        this.book.setHowManyAvailable(this.book.getHowManyAvailable() - 1);
        this.book.setHowManyReserved(this.book.getHowManyReserved() + 1);
        this.user.getData().reserve(this.book);
    }

    @Override
    public String log() {
        Object data[] = new Object[] {this.book};
        return Arrays.toString(data);
    }
    
}
