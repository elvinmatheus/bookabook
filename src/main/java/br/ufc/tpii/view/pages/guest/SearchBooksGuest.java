package br.ufc.tpii.view.pages.guest;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComponent;

import br.ufc.tpii.model.User;
import br.ufc.tpii.controller.handlers.ClearHandler;
import br.ufc.tpii.controller.handlers.SearchBooksHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.view.components.GuestMenu;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class SearchBooksGuest extends Page {
    
    public final static String TITLE = "Pesquisa Bibliográfica";
    @Override
    public String getTitle() { return TITLE; }

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = GuestMenu.withWrapper(app);
        String[] labelsText = new String[] {"Título:", "Autor:"};
        String[] buttonsText = new String[] {"Cancelar", "Buscar"};
        SearchContentTemplate template = new SearchContentTemplate(labelsText, buttonsText, true, true);
        JComponent content = template.build();
        ActionListener cancelHandler = new ClearHandler<>(template.getClearableFields());
        ActionListener searchHandler = new SearchBooksHandler(template.getTextFields(), template.getCheckBoxs(), User.GUESTPRIVILEGE);
        ActionListener[] handlers = new ActionListener[] {cancelHandler, searchHandler};
        template.setHandlers(handlers);
        String path = "Pesquisa >> Livros";
        LayoutTemplate.build(pane, menubar, content, path);
        return pane;
    }

}
