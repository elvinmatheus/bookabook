package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.view.pages.Home;

public class LogoutCmd implements Command {

    /** Desloga um usuário */

    @Override
    public void execute() {
        App app = App.get();
        Login login = app.getLogin();
        if (!login.isLoggedIn()) {
            // não estava logado!
            app.control().invoke(new NavigateCmd(new Home()));
            return;
        }
        // alteramos o estado para representar que esse usuário não mais está logado
        login.setUser(null);
        login.setIsLoggedIn(false);
        login.setIsAdmin(false);
        // redirecionamos à página inicial
        app.control().invoke(new NavigateCmd(new Home()));
    }
    
    @Override
    public String log() {
        Object data[] = new Object[] {};
        return Arrays.toString(data);
    }
}
