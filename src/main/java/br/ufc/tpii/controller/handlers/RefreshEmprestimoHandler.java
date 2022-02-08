package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Library;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.model.User;

public class RefreshEmprestimoHandler implements ActionListener {

    /** Pega informações dos campos de texto passados como argumento
     * e atualiza o contexto com base nesse texto (matrícula do usuário e isbn do livro).
     * Alterar contexto é considerado "refresh", pois as views podem estar observando esse tipo
     * de alteração.
     * 
     * O novo usuário do contexto é o usuário com a matrícula especificada no campo de texto.
     * O novo livro do contexto é o livro cujo isbn foi especificado no campo de texto.
     */
    
    private List<JTextField> fields;
    public RefreshEmprestimoHandler(List<JTextField> fields) {
        this.fields = fields;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        App app = App.get();
        String isbn = this.fields.get(0).getText();
        String matricula = this.fields.get(1).getText();
        /* Obrigatoriedade dos campos */
        if (isbn.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo Cód. do livro é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        if (matricula.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo Matrícula Usuário é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        /* Interpretação dos campos */
        int matriculaInt;
        try {
            matriculaInt = Integer.parseInt(matricula);
            if (matriculaInt < 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) {
            app.control().invoke(new DisplayPopupCmd("Matrícula deve ser um número não negativo. Recebido: " + matricula, JOptionPane.ERROR_MESSAGE));
            return;
        }
        Library lib = app.getLibrary();
        Login login = app.getLogin();
        Book book = lib.findByISBN(isbn);
        User user = login.getUser(matriculaInt);
        // alteramos o contexto
        // de forma que views observadoras poderão reagir apropriadamente
        app.control().invoke(new RefreshCmd(RefreshID.UserContext, user));
        app.control().invoke(new RefreshCmd(RefreshID.BookContext, book));
    }

  
}
