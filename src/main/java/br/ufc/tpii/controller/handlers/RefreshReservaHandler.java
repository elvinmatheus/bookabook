package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.model.User;

public class RefreshReservaHandler implements ActionListener {

    /** Pega informações dos campos de texto passados como argumento
     * e atualiza o contexto com base nesse texto (matrícula do usuário).
     * Alterar contexto é considerado "refresh", pois as views podem estar observando esse tipo
     * de alteração.
     * 
     * O novo usuário do contexto é o usuário com a matrícula especificada no campo de texto.
     * A nova lista de livros do contexto é a lista de livros reservados por esse usuário.
     */
    
    private JTextField field;
    public RefreshReservaHandler(JTextField field) {
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        App app = App.get();
        String matricula = this.field.getText();
        /* Obrigatoriedade dos campos */
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
        Login login = app.getLogin();
        User user = login.getUser(matriculaInt);
        app.control().invoke(new RefreshCmd(RefreshID.UserContext, user));
        app.control().invoke(new RefreshCmd(RefreshID.BookListContext, user.getData().getReservedBooks()));
    }

  
}
