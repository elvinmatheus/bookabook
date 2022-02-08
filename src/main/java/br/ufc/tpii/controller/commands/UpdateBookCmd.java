package br.ufc.tpii.controller.commands;

import javax.swing.JOptionPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;

public class UpdateBookCmd implements Command {

    /** Atualiza informações de um livro */

    @Override
    public String log() {
        return String.format("UpdateBookCmd: %s -> %s", (this.bookToUpdate == null? "null": this.bookToUpdate.getTitle()), (this.bookData == null? "null": this.bookData.getTitle()));
    }

    private Book bookToUpdate, bookData;
    private User user;
    public UpdateBookCmd(Book bookToUpdate, Book bookData) {
        this(bookToUpdate, bookData, App.get().getLogin().getUser());
    }
    /* user é quem está cadastrando esse livro */
    public UpdateBookCmd(Book bookToUpdate, Book bookData, User user) {
        this.bookToUpdate = bookToUpdate;
        this.bookData = bookData;
        this.user = user;
    }

    @Override
    public void execute() {
        App app = App.get();
        boolean changedLibrary = app.getLibrary().updateBook(bookToUpdate, bookData);
        if (!changedLibrary) {
            app.control().invoke(new DisplayPopupCmd("Livro já está catalogado", JOptionPane.ERROR_MESSAGE));
            return;
        }
        app.control().invoke(new ReportCmd<>(bookData.copy(), Reports.Type.BOOK_REGISTER, this.user.copy()));
        app.control().invoke(new DisplayPopupCmd("Livro atualizado com sucesso."));
    }
    
}
