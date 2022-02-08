package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;

public class DevolverCmd implements Command {

    /** Realiza a devolução de um livro */

    Book book; // livro que foi emprestado
    User user; // usuário que pegou o livro emprestado
    public DevolverCmd(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    @Override
    public void execute() {
        App app = App.get();
        this.book.setHowManyAvailable(this.book.getHowManyAvailable() + 1);
        Emprestimo emprestimo = this.user.getData().devolver(this.book).copy();
        app.control().invoke(new ReportCmd<>(emprestimo, Reports.Type.DEVOLUCAO));
    }

    @Override
    public String log() {
        Object data[] = new Object[] {this.book};
        return Arrays.toString(data);
    }
    
}
