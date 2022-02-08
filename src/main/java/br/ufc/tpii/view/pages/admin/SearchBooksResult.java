package br.ufc.tpii.view.pages.admin;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.components.BookResult;
import br.ufc.tpii.view.components.GuestMenu;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.ScrollPane;
import br.ufc.tpii.view.components.UserMenu;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class SearchBooksResult extends Page {

    /** Responsável pela página que exibe os resultados da pesquisa de usuários.
     * 
     * Além disso, fornece o método estático popupUserData que faz aparecer
     * um popup exibindo os dados associados a um usuário.
     */

    public final static String TITLE = "Pesquisa >> Livros >> Resultado";
    @Override
    public String getTitle() { return TITLE; }

    final static int SPACEBETWEENRESULTS = 10;

    private String titleFilter, authorFilter;
    private int privilege;
    public SearchBooksResult(String titleFilter, String authorFilter, int privilege) {
        this.titleFilter = titleFilter;
        this.authorFilter = authorFilter;
        this.privilege = privilege;
    }
    
    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = this.privilege == 2 ? AdminMenu.withWrapper(app) : this.privilege == 1 ? UserMenu.withWrapper(app) : GuestMenu.withWrapper(app);
        String path = "Pesquisa >> Livro >> Resultado";
        JComponent content = Margin.horizontal(this.mainContent(app), MenuFactory.WRAPPERHORIZONTALMARGIN);
        LayoutTemplate.build(pane, menubar, content, path);
        return pane;
    }

    private JComponent mainContent(App app) {
        List<Book> books = app.getLibrary().getFilteredBooks(this.titleFilter, this.authorFilter);
        JComponent component = Box.createVerticalBox();
        int length = books.size();
        if (length == 0) {
            // não há usuários!
            return new Label("Nenhum livro encontrado");
        }
        boolean editable = this.privilege == 2;
        boolean selectable = false;
        boolean reservable = this.privilege == 1;
        User currentUser = app.getLogin().getUser();
        for (int i = 0; i < length; i++) {
            BookResult bookResultView = new BookResult(app, books.get(i), editable, selectable, reservable);
            if (i > 0) component.add(Margin.rigidVertical(SPACEBETWEENRESULTS));
            bookResultView.setAssociatedUser(currentUser);
            this.addView(bookResultView);
            component.add(bookResultView.paint());
        }
        JScrollPane scrollPane = new ScrollPane(component);
        return scrollPane;
    }

}
