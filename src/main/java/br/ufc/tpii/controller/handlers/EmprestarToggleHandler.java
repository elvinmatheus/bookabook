package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.ufc.tpii.controller.commands.DevolverCmd;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.EmprestarCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Controller;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;

public class EmprestarToggleHandler implements ActionListener {

    /** Concede um empréstimo de um livro a um usuário.
     * Se o livro ou o usuário for especificado como null,
     * eles seriam obtidos por meio do contexto.
     * Dessa forma, esse handler consegue ser dinâmico,
     * consegue se referir a outros usuários/livros, basta alterar o contexto.
     */

    private User user;
    private Book book;
    /* "Toggle" porque empresta quando o livro não está emprestado e devolve qunado o livro já está emprestado */
    public EmprestarToggleHandler(User user, Book book) {
        this.user = user;
        this.book = book;
    }
    public EmprestarToggleHandler() {}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        App app = App.get();
        Controller control = app.control();
        User u = this.user;
        Book b = this.book;
        if (u == null) {
            u = app.getUserContext();
        }
        if (b == null) {
            b = app.getBookContext();
        }
        if (u.getData().hasBookRented(b)) {
            control.invoke(new DevolverCmd(b, u));
            control.invoke(new DisplayPopupCmd("Livro devolvido com sucesso"));
        } else {
            control.invoke(new EmprestarCmd(b, u));
            control.invoke(new DisplayPopupCmd("Livro emprestado com sucesso"));
        }
    }
}
