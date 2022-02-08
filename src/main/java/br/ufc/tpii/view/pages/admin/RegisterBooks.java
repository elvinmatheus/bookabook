package br.ufc.tpii.view.pages.admin;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;

import br.ufc.tpii.controller.handlers.ClearHandler;
import br.ufc.tpii.controller.handlers.RegisterBookHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

import java.util.List;
import javax.swing.JTextField;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.controller.commands.RemoveBookCmd;

public class RegisterBooks extends Page {
    
    public final static String TITLE = "Catalogação";
    @Override
    public String getTitle() { return TITLE; }

    private List<JTextField> fields;
    private JButton[] buttons;
    private Book book;

    public RegisterBooks() {
        this(null);
    }

    public RegisterBooks(Book book) {
        this.book = book;
    }

    @Override
    public JComponent paint() {
        boolean edit = book != null;
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String[] labelsText = new String[] {
            "Título:", "Subtítulo:", "Autor 1:", "Autor 2:", "Autor 3:",
            "Edição:", "Ano de publicação:", "Local de publicação:", "Exemplares:", "ISBN:"
        };
        String[] buttonsText = edit ? new String[] {"Cancelar", "Atualizar", "Remover"} : new String[] {"Cancelar", "Cadastrar"};
        SearchContentTemplate template = new SearchContentTemplate(labelsText, buttonsText, null, false);
        JComponent content = template.build();
        this.fields = template.getTextFields();
        this.buttons = template.getButtons();
        ActionListener cancelHandler = edit ? (e -> this.refresh(RefreshID.CLEAR)) : new ClearHandler<>(template.getClearableFields());
        RegisterBookHandler registerBookHandler = new RegisterBookHandler(this.fields);
        if (edit) registerBookHandler.setBookToUpdate(book);
        ActionListener removeBookHandler = e -> {
            app.control().invoke(new RemoveBookCmd(book));
            app.control().invoke(new NavigateCmd(new SearchBooks()));
        };
        ActionListener[] handlers = edit ? new ActionListener[] {cancelHandler, registerBookHandler, removeBookHandler} : new ActionListener[] {cancelHandler, registerBookHandler};
        template.setHandlers(handlers);
        String path = edit ?  "Pesquisa >> Livros >> Resultado >> Editar" : "Catalogação";
        LayoutTemplate.build(pane, menubar, content, path);
        // this.registerButton = template.getButtons()[1];
        this.refresh(RefreshID.CLEAR);
        return pane;
    }


    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (this.book != null && (changeID == RefreshID.CLEAR || changeID == RefreshID.LibraryUpdateBook)) {
            String title = book.getTitle();
            String subtitle = book.getSubtitle();
            String author1 = "";
            String author2 = "";
            String author3 = "";
            List<String> authors = book.getAuthors();
            try { author1 = authors.get(0); } catch (IndexOutOfBoundsException excp) {}
            try { author2 = authors.get(1); } catch (IndexOutOfBoundsException excp) {}
            try { author3 = authors.get(2); } catch (IndexOutOfBoundsException excp) {}
            String edition = book.getEdition();
            int publishingYear = book.getYearOfPublishment();
            String whereWasPublished = book.getWhereWasPublished();
            int numberOfCopies = book.getHowManyTotal();
            String isbn = book.getIsbn();

            this.fields.get(0).setText(title);
            this.fields.get(1).setText(subtitle);
            this.fields.get(2).setText(author1);
            this.fields.get(3).setText(author2);
            this.fields.get(4).setText(author3);
            this.fields.get(5).setText(edition);
            this.fields.get(6).setText(String.valueOf(publishingYear));
            this.fields.get(7).setText(whereWasPublished);
            this.fields.get(8).setText(String.valueOf(numberOfCopies));
            this.fields.get(9).setText(isbn);
            this.fields.get(9).setEnabled(false);
            if (this.buttons.length == 3) {
                JButton removeButton = this.buttons[2];
                removeButton.setEnabled(this.book.getHowManyRented() == 0 && this.book.getHowManyReserved() == 0);
            }
        }

        super.refresh(changeID, args);
    }
}
