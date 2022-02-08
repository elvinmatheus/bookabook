package br.ufc.tpii.view.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DereserveBookCmd;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.ReserveBookCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.fixed.FixedButton;
import br.ufc.tpii.view.components.layout.PackLayout;
import br.ufc.tpii.view.components.layout.StretchLayout;

import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.view.pages.admin.RegisterBooks;

public class BookResult extends View {

    /** 
     * SearchUsersResults exibe uma lista de BookResult.
     * 
     * É composto por alguns dados do usuário à esquerda e dois botões à direita.
     * Os botões são "Abrir registro" e "Editar".
     */

    final static int SPACEBETWEENBOTTOMLABELS = 20;
    final static int BOTTOMLABELSRIGHTMARGIN = 25;
    final static int SPACEBETWEENBUTTONS = 15;
    final static int BUTTONSVERTICALMARGIN = 18;
    final static int BUTTONSHORIZONTALMARGIN = 25;
    final static int INSIDEVERTICALMARGINLEFTCONTENT = 10;
    final static int INSIDEHORIZONTALMARGINLEFTCONTENT = 10;

    final static Color WRAPPERBUTTONSGRAY = new Color(209, 209, 209);
    final static Color BUTTONBGGRAY = new Color(181, 181, 181);
    final static Color BUTTONLABELCOLOR = new Color(0, 0, 0);
    final static Color LEFTBACKGROUNDCOLOR = new Color(255, 255, 255);

    final static String DEFAULTTOTALTEXT = "Exemplares: ";
    final static String DEFAULTAVAILABLETEXT = "Disponíveis: ";
    final static String DEFAULTRENTTEXT = "Emprestados: ";
    final static String DEFAULTRESERVEDTEXT = "Reservados: ";

    final static String BUTTONRESERVETEXT = "Reservar";
    final static String BUTTONDERESERVETEXT = "Remover reserva";

    /* Constantes para o POPUP */
    final static int TOPMARGIN = 40;
    final static int BOTTOMMARGIN = 40;
    final static int LEFTMARGIN = 30;
    final static int RIGHTMARGIN = 30;

    private JCheckBox checkbox = null;
    private User user = null;
    private App app;
    private Book book;
    private boolean editable, selectable, reservable;
    private ActionListener checkboxHandler;

    public BookResult(App app, Book book) {
        this(app, book, true, false, false, null);
    }
    public BookResult(App app, Book book, boolean editable, boolean selectable, boolean reservable) {
        this(app, book, editable, selectable, reservable, null);
    }
    public BookResult(App app, Book book, boolean editable, boolean selectable, boolean reservable, ActionListener checkboxHandler) {
        super(app);
        this.app = app;
        this.book = book;
        this.editable = editable;
        this.selectable = selectable;
        this.reservable = reservable;
        this.checkboxHandler = checkboxHandler;
        this.totalLabel = new Label("");
        this.availableLabel = new Label("");
        this.rentLabel = new Label("");
        this.reservedLabel = new Label("");
        this.refresh(RefreshID.CLEAR);
    }

    private JFrame frame;
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }


    /* Atribui o usuário associado às funcionalidades "Editar" e "Reservar" desse objeto */
    public void setAssociatedUser(User user) {
        this.user = user;
    }

    public JCheckBox getCheckBox() { return this.checkbox; }


    private JComponent left() {
        JComponent component = PackLayout.createVerticalBox();
        Label title = new Label("Título: " + book.getTitle());
        Label author = new Label("Autor: " + book.getAuthors().get(0));
        Label year = new Label("Ano: " + book.getYearOfPublishment());
        component.add(Margin.rigidVertical(INSIDEVERTICALMARGINLEFTCONTENT));
        component.add(Margin.glueRight(title));
        component.add(Margin.glueRight(author));
        component.add(Margin.glueRight(year));
        component.add(Box.createVerticalGlue());
        component.add(this.bottom());
        component.add(Margin.rigidVertical(INSIDEVERTICALMARGINLEFTCONTENT));
        component.setOpaque(true);
        component.setBackground(LEFTBACKGROUNDCOLOR);
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Margin.rigidHorizontal(INSIDEHORIZONTALMARGINLEFTCONTENT));
        wrapper.add(component);
        wrapper.add(Box.createHorizontalGlue());
        wrapper.setOpaque(true);
        wrapper.setBackground(LEFTBACKGROUNDCOLOR);
        return wrapper;
    }

    private Button editButton = null, reserveButton = null;
    private JComponent right(boolean editable, boolean reservable) {
        JComponent component = StretchLayout.createVerticalBox();
        ActionListener viewHandler = e -> {
            this.popupBookData(this.frame);
        };
        Button view = new Button("Abrir registro", viewHandler, BUTTONLABELCOLOR, BUTTONBGGRAY);
        component.add(Margin.rigidVertical(BUTTONSVERTICALMARGIN));
        component.add(view);
        if (editable) {
            ActionListener editHandler = e -> {
                if (this.user == null) {
                    throw new RuntimeException("Tentativa de editar um livro, mas não há usuário associado");
                }
                app.control().invoke(new NavigateCmd(new RegisterBooks(this.book)));
            };
            this.editButton = new Button("Editar", editHandler, BUTTONLABELCOLOR, BUTTONBGGRAY);
            component.add(Margin.rigidVertical(SPACEBETWEENBUTTONS));
            component.add(this.editButton);
        }
        if (reservable) {
            ActionListener reserveHandler = e -> {
                if (this.user == null) {
                    throw new RuntimeException("Tentativa de reservar um livro, mas não há usuário associado");
                }
                UserData data = user.getData();
                if (data.hasBookRented(book)) {
                    return; // livro está emprestado
                }
                if (data.hasBookReserved(book)) {
                    // vamos desreservar!
                    app.control().invoke(new DereserveBookCmd(book, this.user));
                    app.control().invoke(new DisplayPopupCmd("Reserva removida com sucesso!"));
                    return;
                }
                app.control().invoke(new ReserveBookCmd(book, this.user));
                app.control().invoke(new DisplayPopupCmd("Livro reservado com sucesso!"));
            };
            // escolhemos o maior dos tamanhos de label do botão
            // pois o botão tem tamanho fixo
            String reserveText = BUTTONRESERVETEXT.length() > BUTTONDERESERVETEXT.length() ?
                                 BUTTONRESERVETEXT : BUTTONDERESERVETEXT;
            this.reserveButton = new FixedButton(reserveText, reserveHandler, BUTTONLABELCOLOR, BUTTONBGGRAY);
            component.add(Margin.rigidVertical(SPACEBETWEENBUTTONS));
            component.add(this.reserveButton);
        }
        component.add(Margin.rigidVertical(BUTTONSVERTICALMARGIN));
        component.setOpaque(true);
        component.setBackground(WRAPPERBUTTONSGRAY);
        JComponent wrapper = Margin.horizontal(Margin.glueVertical(component), BUTTONSHORIZONTALMARGIN);
        wrapper.setOpaque(true);
        wrapper.setBackground(WRAPPERBUTTONSGRAY);
        return wrapper;
    }

    private Label totalLabel, availableLabel, rentLabel, reservedLabel;
    private JComponent bottom() {
        JComponent bottom = Box.createHorizontalBox();
        bottom.add(totalLabel);
        bottom.add(Margin.rigidHorizontal(SPACEBETWEENBOTTOMLABELS));
        bottom.add(availableLabel);
        bottom.add(Margin.rigidHorizontal(SPACEBETWEENBOTTOMLABELS));
        bottom.add(rentLabel);
        bottom.add(Margin.rigidHorizontal(SPACEBETWEENBOTTOMLABELS));
        bottom.add(reservedLabel);
        bottom.add(Margin.rigidHorizontal(BOTTOMLABELSRIGHTMARGIN));
        return bottom;
    }
    
    public void popupBookData() {
        this.popupBookData(null);
    }
    public void popupBookData(JFrame frame) {
        if (frame == null) frame = app.getFrame();
        String title = book.getTitle();
        String subtitle = book.getSubtitle();
        List<String> authors = book.getAuthors();
        String author1 = "", author2 = "", author3 = "";
        /* ignoramos as exceções, pois nesse caso elas já estão tratadas (a String author correspondente será vazia "") */
        try { author1 = authors.get(0); } catch (IndexOutOfBoundsException e) {}
        try { author2 = authors.get(1); } catch (IndexOutOfBoundsException e) {}
        try { author3 = authors.get(2); } catch (IndexOutOfBoundsException e) {}
        String edition = book.getEdition();
        int year = book.getYearOfPublishment();
        String whereWasPublished = book.getWhereWasPublished();
        String isbn = book.getIsbn();;
        JComponent component = Box.createVerticalBox();
        Label titleLabel = new Label("Título: " + title);
        Label subtitleLabel = new Label("Subtítulo: " + subtitle);
        Label author1Label = new Label("Autor 1: " + author1);
        Label author2Label = new Label("Autor 2: " + author2);
        Label author3Label = new Label("Autor 3: " + author3);
        Label editionLabel = new Label("Edição: " + edition);
        Label yearLabel = new Label("Ano de publicação: " + year);
        Label whereLabel = new Label("Local de publicação: " + whereWasPublished);
        Label isbnLabel = new Label("ISBN: " + isbn);
        component.add(Margin.rigidVertical(TOPMARGIN));
        component.add(Margin.glueRight(titleLabel));
        component.add(Margin.glueRight(subtitleLabel));
        component.add(Margin.glueRight(author1Label));
        component.add(Margin.glueRight(author2Label));
        component.add(Margin.glueRight(author3Label));
        component.add(Margin.glueRight(editionLabel));
        component.add(Margin.glueRight(yearLabel));
        component.add(Margin.glueRight(whereLabel));
        component.add(Margin.glueRight(isbnLabel));
        component.add(Margin.rigidVertical(3));
        BookResult bk = new BookResult(app, book);
        component.add(bk.bottom());
        component.add(Margin.rigidVertical(3));
        component.add(Margin.rigidVertical(BOTTOMMARGIN));
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Margin.rigidHorizontal(LEFTMARGIN));
        wrapper.add(component);
        wrapper.add(Margin.rigidHorizontal(RIGHTMARGIN));
        JDialog dialog = new JDialog(frame);
        dialog.add(wrapper);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private JComponent rightComponent;
    @Override
    public JComponent paint() {
        JComponent component = Box.createHorizontalBox();
        if (selectable) {
            this.checkbox = new JCheckBox();
            if (checkboxHandler != null) this.checkbox.addActionListener(checkboxHandler);
            component.add(this.checkbox);
        }
        JComponent bookRegister = PackLayout.createHorizontalBox();
        JComponent left = this.left();
        this.rightComponent = this.right(editable, reservable);
        bookRegister.add(left);
        bookRegister.add(this.rightComponent);
        component.add(bookRegister);
        this.refresh(RefreshID.CLEAR);
        return component;
    }

    private boolean hasMounted = false;
    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (RefreshID.MOUNT == changeID) {
            this.hasMounted = true;
        }
        if (RefreshID.CLEAR == changeID || RefreshID.MOUNT == changeID) {
            int total = book.getHowManyTotal();
            int available = book.getHowManyAvailable();
            int rent = book.getHowManyRented();
            int reserved = book.getHowManyReserved();
            this.totalLabel.setText(DEFAULTTOTALTEXT + total);
            this.availableLabel.setText(DEFAULTAVAILABLETEXT + available);
            this.rentLabel.setText(DEFAULTRENTTEXT + rent);
            this.reservedLabel.setText(DEFAULTRESERVEDTEXT + reserved);
        }
        if (this.user != null) {
            boolean bookIsReserved = this.user.getData().hasBookReserved(this.book);
            boolean bookIsRented = this.user.getData().hasBookRented(this.book);
            if (args != null && args.length == 1 && args[0] == this.book) {
                if (RefreshID.BookTotal == changeID) {
                    int total = book.getHowManyTotal();
                    this.totalLabel.setText(DEFAULTTOTALTEXT + total);
                }
                if (RefreshID.BookAvailable == changeID) {
                    int available = book.getHowManyAvailable();
                    this.availableLabel.setText(DEFAULTAVAILABLETEXT + available);
                }
                if (RefreshID.BookReserved == changeID) {
                    int reserved = book.getHowManyReserved();
                    this.reservedLabel.setText(DEFAULTRESERVEDTEXT + reserved);
                }
                int rent = book.getHowManyRented();
                this.rentLabel.setText(DEFAULTRENTTEXT + rent);
            }
            if (this.hasMounted && this.reserveButton != null) {
                this.reserveButton.setText(bookIsReserved ? "Remover reserva" : "Reservar");
                this.reserveButton.setEnabled(!bookIsRented);
            }
        }
        super.refresh(changeID, args);
    }

}
