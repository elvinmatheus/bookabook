package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.ufc.tpii.controller.commands.ReserveBookCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;

public class ReserveHandler implements ActionListener {

    /** Simplesmente chama o comando de reservar o livro.
     * Quando chamado sem argumentos, pega o livro e o usuário do contexto.
     */

    private User user; // usuário que está reservando
    private Book book; // livro sendo reservado
    public ReserveHandler(User user, Book book) {
        this.user = user;
        this.book = book;
    }
    public ReserveHandler() {
        App app = App.get();
        this.user = app.getUserContext();
        this.book = app.getBookContext();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        App.get().control().invoke(new ReserveBookCmd(this.book, this.user));
    }
}
