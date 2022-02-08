package br.ufc.tpii.view.pages.admin;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.controller.handlers.EmprestarToggleHandler;
import br.ufc.tpii.controller.handlers.RefreshEmprestimoHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;

public class EmprestimosEDevolucoes extends Page {
    
    public final static String TITLE = "Empréstimos e Devoluções";
    @Override
    public String getTitle() { return TITLE; }

    final static int INFOVERTICALCONTENTMARGIN = 50;
    final static int INFOCONTENTLEFTMARGIN = 50;
    final static int SPACEBETWEENLABELS = 3;

    final static Color INFOBGCOLOR = new Color(196, 196, 196);

    private JLabel title = new Label("");
    private JLabel authors = new Label("");
    private JLabel publishmentdate = new Label("");
    private JLabel username = new Label("");
    private JLabel status = new Label("");
    private JLabel pending = new Label("");
    private JComponent infoComponent = this.info();
    private JButton rentButton;


    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        /* Top */
        String[] labelsText = new String[] {
            "Cód. do Livro:", "Matrícula Usuário:"
        };
        String[] topButtonText = new String[] {"Buscar"};
        SearchContentTemplate inputTemplate = new SearchContentTemplate(labelsText, topButtonText, null, false, -1, false);
        JComponent searchContent = inputTemplate.build();
        ActionListener refreshHandler = new RefreshEmprestimoHandler(inputTemplate.getTextFields());
        ActionListener[] searchHandlers = new ActionListener[] {refreshHandler};
        inputTemplate.setHandlers(searchHandlers);
        /* Bottom */
        String[] bottomButtonsText = new String[] {"Cancelar", "Emprestar/Devolver"};
        ActionListener cancelHandler = e -> {
            this.app.control().invoke(new RefreshCmd(RefreshID.UserContext, (User)null));
            this.app.control().invoke(new RefreshCmd(RefreshID.BookContext, (Book)null));
        };
        ActionListener rentToggleHandler = new EmprestarToggleHandler();
        ActionListener[] bottomHandlers = new ActionListener[] {cancelHandler, rentToggleHandler};
        SearchContentTemplate buttonsTemplate = new SearchContentTemplate(new String[0], bottomButtonsText, bottomHandlers, false);
        JComponent buttons = buttonsTemplate.build();
        this.rentButton = buttonsTemplate.getButtons()[1];
        pane.add(menubar);
        pane.add(LayoutTemplate.pathComponent("Circulação >> Empréstimos e Devoluções"));
        pane.add(searchContent);
        pane.add(this.infoComponent);
        pane.add(buttons);
        this.app.control().invoke(new RefreshCmd(RefreshID.UserContext, (User)null));
        this.app.control().invoke(new RefreshCmd(RefreshID.BookContext, (Book)null));
        return pane;
    }

    private void addLabels(JComponent component) {
        component.add(Margin.rigidVertical(INFOVERTICALCONTENTMARGIN));
        component.add(this.title);
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(this.authors);
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(this.publishmentdate);
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(this.username);
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(this.status);
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(this.pending);
    }
    

    private JComponent info() {
        JComponent wrapper2 = Box.createHorizontalBox();
        JComponent wrapper1 = Box.createHorizontalBox();
        JComponent content = Box.createVerticalBox();
        addLabels(content);
        content.add(Margin.rigidVertical(INFOVERTICALCONTENTMARGIN));
        wrapper1.add(Margin.rigidHorizontal(INFOCONTENTLEFTMARGIN));
        wrapper1.add(content);
        wrapper1.add(Box.createHorizontalGlue());
        wrapper1.setOpaque(true);
        wrapper1.setBackground(INFOBGCOLOR);
        wrapper2.add(Margin.rigidHorizontal(MenuFactory.WRAPPERHORIZONTALMARGIN));
        wrapper2.add(wrapper1);
        wrapper2.add(Margin.rigidHorizontal(MenuFactory.WRAPPERHORIZONTALMARGIN));
        return wrapper2;
    }
    @Override
    public void refresh(RefreshID changeID, Object... args) {
        User user = this.app.getUserContext();
        Book book = this.app.getBookContext();
        boolean userFound = user != null;
        boolean bookFound = book != null;

        String titleText = "Título: ";
        String authorsText = "Autor(es): ";
        String yearText = "Data de Publicação: ";
        String userText = "Usuário: ";
        String statusText = "Situação: ";
        String pendingText = "Devoluções pendentes: ";

        if (RefreshID.UserContext == changeID
            || RefreshID.UserEmprestar == changeID
            || RefreshID.UserDevolver == changeID) {
            if (userFound) {
                UserData data = user.getData();
                int numOfEmprestimos = data.getEmprestimos().size();
                userText += data.getName();
                statusText += user.status();
                pendingText += numOfEmprestimos;
            } else {
                userText += "NÃO ENCONTRADO";
            }
            this.username.setText(userText);
            this.status.setText(statusText);
            this.pending.setText(pendingText);
        }
        if (RefreshID.BookContext == changeID) {
            if (bookFound) {
                titleText += book.getTitle();
                authorsText += String.join(", ", book.getAuthors());
                yearText += book.getYearOfPublishment();
            } else {
                titleText += "NÃO ENCONTRADO";
            }
            this.title.setText(titleText);
            this.authors.setText(authorsText);
            this.publishmentdate.setText(yearText);
        }

        if (bookFound && userFound) {
            if (user.getData().hasBookRented(book)) {
                this.rentButton.setText("Devolver");
                this.rentButton.setEnabled(true);
            } else {
                int numOfEmprestimos = user.getData().getEmprestimos().size();
                this.rentButton.setText("Emprestar");
                this.rentButton.setEnabled(numOfEmprestimos < Emprestimo.getMaxQuantity());
            }
        } else {
            this.rentButton.setText("Emprestar/Devolver");
            this.rentButton.setEnabled(false);
        }
        super.refresh(changeID, args);
    }
}
