package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.RegisterBookCmd;
import br.ufc.tpii.controller.commands.UpdateBookCmd;;

public class RegisterBookHandler implements ActionListener {

    /* Lê os campos de texto e fazer um cadastro de livro */
    
    
    private List<JTextField> fields;
    private Book bookToUpdate;
    public RegisterBookHandler(List<JTextField> fields) {
        this(fields, null);
    }
    
    public RegisterBookHandler(List<JTextField> fields, Book bookToUpdate) {
        this.fields = fields;
        this.bookToUpdate = bookToUpdate;
    }

    public void setBookToUpdate(Book bookToUpdate) {
        this.bookToUpdate = bookToUpdate;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        boolean edit = bookToUpdate != null;

        App app = App.get();
        String title = this.fields.get(0).getText();
        String subtitle = this.fields.get(1).getText();
        String author1 = this.fields.get(2).getText();
        String author2 = this.fields.get(3).getText();
        String author3 = this.fields.get(4).getText();
        String edition = this.fields.get(5).getText();
        String publishmentYear = this.fields.get(6).getText();
        String whereWasPublished = this.fields.get(7).getText();
        String totalNum = this.fields.get(8).getText();
        String isbn = this.fields.get(9).getText();
        /* Obrigatoriedade dos campos */
        if (title.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo Título é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        if (author1.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo Autor 1 é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        if (totalNum.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo Exemplares é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        if (isbn.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo ISBN é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        /* Interpretação dos campos */
        int publishmentYearInt;
        try {
            publishmentYearInt = Integer.parseInt(publishmentYear);
            if (publishmentYearInt < 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) {
            app.control().invoke(new DisplayPopupCmd("Ano de publicação deve ser um número não negativo. Recebido: " + publishmentYear, JOptionPane.ERROR_MESSAGE));
            return;
        }
        int totalNumInt;
        try {
            totalNumInt = Integer.parseInt(totalNum);
            if (totalNumInt < 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) {
            app.control().invoke(new DisplayPopupCmd("Número de examplares deve ser um número não negativo. Recebido: " + totalNum, JOptionPane.ERROR_MESSAGE));
            return;
        }
        /* criamos a lista de autores */
        List<String> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        /* livro criado */
        Book book = new Book(title, subtitle, edition, isbn, whereWasPublished, authors, publishmentYearInt, totalNumInt);
        if (edit) {
            if (book.getHowManyTotal() < bookToUpdate.getHowManyTotal() - bookToUpdate.getHowManyAvailable()) {
                app.control().invoke(new DisplayPopupCmd("Não é possível reduzir a quantidade de exemplares se a nova quantidade não conseguir representar os livros pendentes (reservas ou empréstimos)"));
                return;
            }
            app.control().invoke(new UpdateBookCmd(bookToUpdate, book));
        }
        else {
            app.control().invoke(new RegisterBookCmd(book));
        }    
    }

  
}
