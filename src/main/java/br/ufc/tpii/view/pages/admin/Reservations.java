package br.ufc.tpii.view.pages.admin;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.EmprestarCmd;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.controller.handlers.RefreshReservaHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.components.BookResult;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.ScrollPane;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;

public class Reservations extends Page {
    
    public final static String TITLE = "Reservas";
    @Override
    public String getTitle() { return TITLE; }

    final static int INFOVERTICALCONTENTMARGIN = 50;
    final static int INFOCONTENTLEFTMARGIN = 50;
    final static int SPACEBETWEENLABELS = 3;
    final static int SPACEBETWEENBOOKRESULTS = 8;
    final static int SCROLLTOPMARGIN = 15;
    final static int SCROLLBOTTOMMARGIN = 10;

    final static Color INFOBGCOLOR = new Color(196, 196, 196);

    private JComponent reservedBooksList;

    private JLabel username = new Label("");
    private JLabel status = new Label("");
    private JLabel pending = new Label("");
    private JLabel reserves = new Label("");
    private JButton rentButton;
    private JScrollPane scrollPane;
    private JComponent infoComponent;
    private List<JCheckBox> checkBoxes;

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        /* Inicialização */
        this.reservedBooksList = Box.createVerticalBox();
        this.checkBoxes = new ArrayList<>();

        this.scrollPane = new ScrollPane(this.reservedBooksList);

        this.infoComponent = this.info();

        /* Layout */
        JComponent menubar = AdminMenu.withWrapper(app);
        /* Top */
        String[] labelsText = new String[] {
            "Matrícula Usuário:"
        };
        String[] topButtonText = new String[] {"Buscar"};
        SearchContentTemplate searchTemplate = new SearchContentTemplate(labelsText, topButtonText, false, false);
        JComponent searchContent = searchTemplate.build();
        ActionListener searchHandler = new RefreshReservaHandler(searchTemplate.getTextFields().get(0));
        ActionListener[] searchHandlers = new ActionListener[] {searchHandler};
        searchTemplate.setHandlers(searchHandlers);
        /* Bottom */
        String[] bottomButtonsText = new String[] {"Cancelar", "Emprestar"};
        ActionListener cancelHandler = e -> {
            this.app.control().invoke(new RefreshCmd(RefreshID.UserContext, (User)null));
            this.app.control().invoke(new RefreshCmd(RefreshID.BookListContext, (List<Book>)null));
        };
        ActionListener emprestarHandler = e -> {
            User user = this.app.getUserContext();
            if (user == null) return;
            List<Book> reservedBooks = user.getData().getReservedBooks();
            List<Book> listaDeLivrosParaEmprestar = new ArrayList<Book>();
            List<JCheckBox> checksToRemove = new ArrayList<JCheckBox>();
            for (int i = 0; i < reservedBooks.size(); i++) {
                JCheckBox checkbox = this.checkBoxes.get(i);
                if (checkbox.isSelected()) {
                    Book b = reservedBooks.get(i);
                    listaDeLivrosParaEmprestar.add(b);
                    // não vamos emprestar imediatamente, pois isso alteraria a ordem
                    // dos livros reservados, vamos deixar para emprestar depois desse loop
                    checksToRemove.add(checkbox);
                }
            }
            this.checkBoxes.removeAll(checksToRemove);
            for (Book b : listaDeLivrosParaEmprestar) {
                this.app.control().invoke(new EmprestarCmd(b, user));
            }
            this.app.control().invoke(new RefreshCmd(RefreshID.BookListContext, user.getData().getReservedBooks()));
            this.app.control().invoke(new DisplayPopupCmd("Livro(s) emprestado(s) com sucesso"));
        };
        ActionListener[] handlers = new ActionListener[] {cancelHandler, emprestarHandler};
        SearchContentTemplate buttonsTemplate = new SearchContentTemplate(new String[0], bottomButtonsText, handlers, false, -1, true);
        JComponent buttons = buttonsTemplate.build();
        this.rentButton = buttonsTemplate.getButtons()[1];
        pane.add(menubar);
        pane.add(LayoutTemplate.pathComponent("Circulação >> Reservas"));
        pane.add(searchContent);
        pane.add(this.infoComponent);
        pane.add(buttons);
        this.app.control().invoke(new RefreshCmd(RefreshID.UserContext, (User)null));
        return pane;
    }

    private void addLabels(JComponent component) {
        component.add(Margin.rigidVertical(INFOVERTICALCONTENTMARGIN));
        component.add(Margin.glueRight(this.username));
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(Margin.glueRight(this.status));
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(Margin.glueRight(this.pending));   
        component.add(Margin.rigidVertical(SPACEBETWEENLABELS));
        component.add(Margin.glueRight(this.reserves));   
    }

    private JComponent info() {
        JComponent wrapper2 = Box.createHorizontalBox();
        JComponent wrapper1 = Box.createHorizontalBox();
        JComponent content = Box.createVerticalBox();
        addLabels(content);
        content.add(Margin.rigidVertical(SCROLLTOPMARGIN));
        content.add(this.scrollPane);
        content.add(Margin.rigidVertical(SCROLLBOTTOMMARGIN));
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
    public void refresh(RefreshID changeID, Object ...args) {
        String userText = "Usuário: ";
        String statusText = "Situação: ";
        String pendingText = "Devoluções pendentes: ";
        String reservesText = "Reservas: ";
        User user = this.app.getUserContext();
        if (RefreshID.UserContext == changeID || RefreshID.UserEmprestar == changeID) {
            if (user != null) {
                UserData data = user.getData();
                userText += data.getName();
                statusText += user.status();
                pendingText += data.getEmprestimos().size();
            } else {
                userText += "NÃO ENCONTRADO";
                reservesText += "NÃO ENCONTRADO";
                this.scrollPane.setVisible(false);
            }
            this.username.setText(userText);
            this.status.setText(statusText);
            this.pending.setText(pendingText);
            this.reserves.setText(reservesText);
        }
        if (RefreshID.BookListContext == changeID) {
            List<Book> bookList = this.app.getBookListContext();
            this.checkBoxes.clear();
            this.reservedBooksList.removeAll();
            this.removeAllViews();
            if (bookList != null) {
                ActionListener checkboxHandler = e -> this.refreshForCheckBoxInteraction(user, e.getSource());
                for (Book reservedBook : bookList) {
                    boolean editable = false;
                    boolean selectable = true;
                    boolean reservable = false;
                    BookResult bookResultView = new BookResult(app, reservedBook, editable, selectable, reservable, checkboxHandler);
                    bookResultView.setAssociatedUser(user);
                    this.addView(bookResultView);
                    this.reservedBooksList.add(bookResultView.paint());
                    this.reservedBooksList.add(Margin.rigidVertical(SPACEBETWEENBOOKRESULTS));
                    this.checkBoxes.add(bookResultView.getCheckBox());
                }
                this.infoComponent.revalidate();
                this.refreshForCheckBoxInteraction(user, null);
            } else {
                this.checkBoxes.clear();
                reservesText += "NÃO ENCONTRADO";
            }
            this.scrollPane.setVisible(bookList != null && bookList.size() > 0);
        }
        super.refresh(changeID, args);
    }

    public void refreshForCheckBoxInteraction(User user, Object source) {
        int countSelectedCheckboxes = 0;
        for (JCheckBox check : this.checkBoxes) {
            if (check.isSelected()) {
                countSelectedCheckboxes += 1;
            }
        }
        if (source != null && source instanceof JCheckBox && user.getData().getEmprestimos().size() + countSelectedCheckboxes > Emprestimo.getMaxQuantity()) {
            ((JCheckBox)source).setSelected(false);
            countSelectedCheckboxes -= 1;
            this.app.control().invoke(new DisplayPopupCmd("Selecionar mais 1 livro ultrapassa o limite de empréstimos por usuário"));
        }
        boolean canRent = user != null && countSelectedCheckboxes > 0;
        this.rentButton.setEnabled(canRent);
    }

    
}
