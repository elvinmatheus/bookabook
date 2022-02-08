package br.ufc.tpii.controller.commands;

import javax.swing.JOptionPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;

public class RegisterBookCmd implements Command {

    /** Cadastra um livro */

    @Override
    public String log() {
        return "RegisterBookCmd: " + (this.book == null ? "null" : this.book.getTitle());
    }

    private Book book;
    private User user;
    public RegisterBookCmd(Book book) {
        this(book, App.get().getLogin().getUser());
    }
    /* user é quem está cadastrando esse livro */
    public RegisterBookCmd(Book book, User user) {
        this.book = book; // livro a ser cadastrado
        this.user = user;
    }

    @Override
    public void execute() {
        App app = App.get();
        // alteramos o modelo (Library)
        boolean changedLibrary = app.getLibrary().addBook(book);
        if (!changedLibrary) {
            // livro já estava catalogado
            app.control().invoke(new DisplayPopupCmd("Livro já está catalogado", JOptionPane.ERROR_MESSAGE));
            return;
        }
        // registramos o relatório e exibimos uma popup de sucesso
        app.control().invoke(new ReportCmd<>(book.copy(), Reports.Type.BOOK_REGISTER, this.user.copy()));
        app.control().invoke(new DisplayPopupCmd("Livro catalogado com sucesso."));
    }
    
}
