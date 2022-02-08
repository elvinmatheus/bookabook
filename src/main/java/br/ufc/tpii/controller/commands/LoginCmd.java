package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.model.User;
import br.ufc.tpii.view.pages.admin.SearchBooks;
import br.ufc.tpii.view.pages.guest.SearchBooksGuest;
import br.ufc.tpii.view.pages.user.Profile;

public class LoginCmd implements Command {

    /** Realiza o login de um usuário */

    private User user; // usuário que irá logar
    private int userCode; // nível de privilégio do usuário

    public LoginCmd(User user) {
        this.user = user;
        this.userCode = user.getPrivilege();
    }

    @Override
    public void execute() {
        App app = App.get();
        Login login = app.getLogin();
        boolean isAdmin = this.userCode == 2;
        // altera o modelo para representar que esse usuário está logado
        login.setUser(this.user);
        login.setIsLoggedIn(true);
        login.setIsAdmin(isAdmin);
        // redireciona à página correta
        if (this.userCode == 1) {
            app.control().invoke(new NavigateCmd(new Profile()));
        } else if (this.userCode == 2) {
            app.control().invoke(new NavigateCmd(new SearchBooks()));
        } else {
            // está aqui por legacy,
            // mas na verdade LoginCmd não deve ser usado para logar um convidado
            app.control().invoke(new NavigateCmd(new SearchBooksGuest()));
        }
    }

    @Override
    public String log() {
        String email = this.user == null ? "" : this.user.getData().getEmail();
        Object data[] = new Object[] {email, this.userCode};
        return Arrays.toString(data);
    }
}
