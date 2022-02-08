package br.ufc.tpii.view.pages.admin;

import javax.swing.Box;
import javax.swing.JComponent;

import java.awt.event.ActionListener;

import br.ufc.tpii.model.User;
import br.ufc.tpii.controller.handlers.ClearHandler;
import br.ufc.tpii.controller.handlers.SearchBooksHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class SearchBooks extends Page {
    
    public final static String TITLE = "Pesquisa >> Livros";
    @Override
    public String getTitle() { return TITLE; }

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String[] labelsText = new String[] {"Título:", "Autor:"};
        String[] buttonsText = new String[] {"Cancelar", "Buscar"};
        SearchContentTemplate template = new SearchContentTemplate(labelsText, buttonsText, null, true);
        JComponent content = template.build();
        ActionListener cancelHandler = new ClearHandler<>(template.getClearableFields());
        ActionListener searchHandler = new SearchBooksHandler(template.getTextFields(), template.getCheckBoxs(), User.ADMINPRIVILEGE);
        ActionListener[] handlers = new ActionListener[] {cancelHandler, searchHandler};
        template.setHandlers(handlers);
        String path = "Pesquisa >> Livros";
        LayoutTemplate.build(pane, menubar, content, path);
        return pane;
    }

}
