package br.ufc.tpii.view.pages.admin;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.User;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.ScrollPane;
import br.ufc.tpii.view.components.UserResult;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class SearchUsersResult extends Page {

    /** Responsável pela página que exibe os resultados da pesquisa de usuários.
     * 
     * Além disso, fornece o método estático popupUserData que faz aparecer
     * um popup exibindo os dados associados a um usuário.
     */

    public final static String TITLE = "Pesquisa >> Usuários >> Resultado";
    @Override
    public String getTitle() { return TITLE; }

    final static int SPACEBETWEENRESULTS = 10;
    /* Constantes para o POPUP */
    final static int TOPMARGIN = 40;
    final static int BOTTOMMARGIN = 40;
    final static int LEFTMARGIN = 30;
    final static int RIGHTMARGIN = 30;

    private String nameFilter, matriculaFilter;
    public SearchUsersResult(String nameFilter, String matriculaFilter) {
        this.nameFilter = nameFilter;
        this.matriculaFilter = matriculaFilter;
    }
    
    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String path = "Pesquisa >> Usuários >> Resultado";
        JComponent content = Margin.horizontal(this.mainContent(app), MenuFactory.WRAPPERHORIZONTALMARGIN);
        LayoutTemplate.build(pane, menubar, content, path);
        return pane;
    }

    private JComponent mainContent(App app) {
        Collection<User> users = app.getLogin().getFilteredUsers(this.nameFilter, this.matriculaFilter);
        JComponent component = Box.createVerticalBox();
        Iterator<User> it = users.iterator();
        if (users.size() == 0) {
            // não há usuários!
            return new Label("Nenhum usuário encontrado");
        }
        View userResultView = new UserResult(app, it.next());
        component.add(userResultView.paint());
        this.addView(userResultView);
        while (it.hasNext()) {
            component.add(Margin.rigidVertical(SPACEBETWEENRESULTS));
            userResultView = new UserResult(app, it.next());
            component.add(userResultView.paint());
            this.addView(userResultView);
        }
        JScrollPane scrollPane = new ScrollPane(component);
        return scrollPane;
    }

}
