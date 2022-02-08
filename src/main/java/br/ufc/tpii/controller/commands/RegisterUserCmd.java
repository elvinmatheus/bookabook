package br.ufc.tpii.controller.commands;

import javax.swing.JOptionPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Admin;
import br.ufc.tpii.model.Crypto;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;

public class RegisterUserCmd implements Command {

    /** Cadastra um usuário */

    private User user; // usuário a ser cadastrado
    private User registrador; // usuário que fez o cadastro (admin)
    /** data - dados do usuário cadastrado
     * password - senha do usuário cadastrado
     * isAdmin - se o usuário cadastrado é admin
     */
    public RegisterUserCmd(UserData data, String password, boolean isAdmin) {
        // se registrador não foi especificado, então pegamos o admin logado atualmente
        this(data, password, isAdmin, App.get().getLogin().getUser());
    }
    /** registrador é o administrador que está cadastrando esse usuário */
    public RegisterUserCmd(UserData data, String password, boolean isAdmin, User registrador) {
        User user;
        if (isAdmin) {
            user = new Admin(data, Crypto.crypt(password));
        } else {
            user = new User(data, Crypto.crypt(password));
        }
        this.user = user;
        this.registrador = registrador;
    }

    @Override
    public String log() {
        return "RegisterUserCmd: " + this.user.getData().getEmail();
    }

    @Override
    public void execute() {
        App app = App.get();
        Login login = app.getLogin();
        String okMessage = "Usuário cadastrado com sucesso";
        try {
            // alteramos o modelo para que esse usuário seja cadastrado
            login.addUser(this.user);
        } catch (RuntimeException e) {
            app.control().invoke(new DisplayPopupCmd(e.getMessage(), JOptionPane.ERROR_MESSAGE));
            return;
        }
        // guardamos o usuário cadastrado e o registrador para propósito do relatório
        User userAfter = this.user.copy();
        User r = registrador == null ? null : registrador.copy();
        app.control().invoke(new ReportCmd<>(userAfter, Reports.Type.USER_REGISTER, r));
        app.control().invoke(new DisplayPopupCmd(okMessage, JOptionPane.INFORMATION_MESSAGE));
    }
    
}
