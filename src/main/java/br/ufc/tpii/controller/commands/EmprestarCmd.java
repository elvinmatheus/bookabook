package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;

public class EmprestarCmd implements Command {

    /** Realiza um empréstimo */

    Book book; // livro a ser emprestado
    User user; // usuário para o qual o livro será emprestado
    public EmprestarCmd(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    @Override
    public void execute() {
        App app = App.get();
        UserData data = user.getData();
        // checamos se esse livro já está emprestado por esse usuário
        if (data.hasBookRented(this.book)) {
            throw new RuntimeException("Tentativa de emprestar um livro que já está emprestado");
        }
        // checamos se esse empréstimo ultrapassará o limite de empréstimos por usuário
        if (data.getEmprestimos().size() >= Emprestimo.getMaxQuantity()) {
            throw new RuntimeException("Tentativa de ultrapassar o máximo de empréstimos permitidos");
        }
        // checamos se o livro está reservado já por esse usuário
        // situação na qual desfazemos a reserva
        if (data.getReservedBooks().contains(this.book)) {
            this.book.setHowManyReserved(this.book.getHowManyReserved() - 1);
            data.removeReserved(this.book);
        } else {
            // aqui, o livro não estava reservado, então há 1 livro disponível a menos
            this.book.setHowManyAvailable(this.book.getHowManyAvailable() - 1);
        }
        data.emprestar(this.book);
        Emprestimo emprestimo = data.getEmprestimos().get(data.getEmprestimos().size() - 1).copy();
        app.control().invoke(new ReportCmd<>(emprestimo, Reports.Type.EMPRESTIMO));
    }

    @Override
    public String log() {
        Object data[] = new Object[] {this.book};
        return Arrays.toString(data);
    }
    
}
