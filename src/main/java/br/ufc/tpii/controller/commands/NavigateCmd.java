package br.ufc.tpii.controller.commands;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.view.pages.Home;
import br.ufc.tpii.view.pages.admin.SearchBooks;
import br.ufc.tpii.view.pages.user.Profile;

public class NavigateCmd implements Command {

    /** Navega até uma página */

    private Page page;

    /** page é a página a ser pintada na tela */
    public NavigateCmd(Page page) {
        this.page = page;
    }

    @Override
    public void execute() {
        App app = App.get();
        Login login = app.getLogin();
        String destPageName = app.getCurrentPage().getClass().getSimpleName();
        String currPageName = this.page.getClass().getSimpleName();
        if (destPageName.equals(currPageName)) {
            return; // não fazemos nada quando já estamos na página de destino
        }
        if (this.page instanceof Home && login.isLoggedIn()) {
            this.page = login.isAdmin() ? new SearchBooks() : new Profile();
        }
        app.getFrame().setTitle(this.page.getTitle());
        app.navigate(this.page); // usamos navigate de GUI
    }

    @Override
    public String log() {
        return "NavigateCmd: " + this.page.getTitle(); 
    }
}
