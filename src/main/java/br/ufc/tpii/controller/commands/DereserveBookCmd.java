package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;

public class DereserveBookCmd implements Command {

    /** Remove a reserva de um livro (que está associada com um usuário) */

    Book book; // livro do qual devemos remover a reserva
    User user; // usuário que fez essa reserva
    public DereserveBookCmd(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    @Override
    public void execute() {
        this.book.setHowManyAvailable(this.book.getHowManyAvailable() + 1);
        this.book.setHowManyReserved(this.book.getHowManyReserved() - 1);
        this.user.getData().removeReserved(book);
    }

    @Override
    public String log() {
        Object data[] = new Object[] {this.book};
        return Arrays.toString(data);
    }
    
}
