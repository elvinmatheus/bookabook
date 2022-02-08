package br.ufc.tpii.view.components;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import br.ufc.tpii.controller.commands.DereserveBookCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.User;
import br.ufc.tpii.view.components.layout.PackLayout;

public class EmprestimoItem extends View {

    /** Essa classe é responsável por renderizar um item
     * de um empréstimo, que é definido com informações de um Livro + Usuário.
     * 
     * Note que uma RESERVA também é definida com um Livro + Usuário,
     * e as aparências são parecidas. Logo, essa classe permite
     * representar tanto reservas quanto empréstimos.
     * 
     * O construtor Emprestimoitem(App, Book, User) deve ser usado para reservas
     * e EmprestimoItem(App, Emprestimo) deve ser usado para empréstimos.
     * 
     * Por padrão, reservas são exibidas com um botão DELETAR,
     * já empréstimos não. Isso pode ser alterado com setShouldBeDeletable.
     * 
     */

    final static int LEFTCONTENTMARGIN = 10;
    final static int VERTICALCONTENTMARGIN = 6;
    final static int ICONVMARGIN = 6;
    final static int ICONHMARGIN = 3;

    final static Color LABELCOLOR = new Color(240, 240, 240);
    final static Color EXPIREDATECOLOR = new Color(230, 230, 230);
    final static Color BGCOLOR = new Color(66, 66, 69);
    final static Color DELETEBGCOLOR = new Color(231, 75, 75);
    final static Color INFOBGCOLOR = new Color(196, 196, 196);

    private Book book;
    private User user;
    private LocalDateTime expireDate = null;

    public EmprestimoItem(App model, Book book, User user) {
        super(model);
        this.book = book;
        this.user = user;
        this.shouldBeDeletable = true; // é uma reserva
        this.shouldShowExpireDate = false;
    }
    public EmprestimoItem(App model, Emprestimo emprestimo, boolean fresh) {
        super(model);
        this.book = fresh ? emprestimo.getFreshBook() : emprestimo.getBook();
        this.user = emprestimo.getUser();
        this.expireDate = emprestimo.getExpireDate();
        this.shouldBeDeletable = false; // é um empréstimo
        this.shouldShowExpireDate = true;
    }

    private JFrame frame = null;
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    private boolean shouldShowExpireDate;
    public void setShouldShowExpireDate(boolean should) {
        this.shouldShowExpireDate = should;
    }
    private boolean shouldBeDeletable;
    public void setShouldBeDeletable(boolean should) {
        this.shouldBeDeletable = should;
    }

    @Override
    public JComponent paint() {
        JComponent component = PackLayout.createHorizontalBox();
        component.add(this.mainContent(this.book));
        // só há botão de deletar se isso é uma reserva
        if (this.expireDate == null && this.shouldBeDeletable) component.add(this.deleteButton());
        component.add(this.infoButton());
        return component;
    }

    private String getTextFromBook(Book book) {
        return book.getTitle() + "; " + book.getAuthors().get(0);
    }

    private Label mainLabel;
    public JComponent mainContent(Book book) {
        JComponent content = Box.createHorizontalBox();
        String labelText = this.getTextFromBook(book);
        this.mainLabel = new Label(labelText);
        this.mainLabel.setForeground(LABELCOLOR);
        content.add(Margin.rigidHorizontal(LEFTCONTENTMARGIN));
        content.add(Margin.glueVertical(this.mainLabel));
        content.add(Box.createHorizontalGlue());
        content.setBackground(BGCOLOR);
        content.setOpaque(true);
        if (this.expireDate == null) return content;
        JComponent wrapper = Box.createVerticalBox();
        Label expireLabel = new Label("Prazo: " + this.expireDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), EXPIREDATECOLOR);
        wrapper.add(content);
        if (this.shouldShowExpireDate) wrapper.add(Margin.glueLeft(Margin.horizontalRight(expireLabel, 10)));
        wrapper.setBackground(BGCOLOR);
        wrapper.setOpaque(true);
        return wrapper;
    }

    public JComponent deleteButton() {
        ImageIcon img = new ImageIcon("./images/outline_delete_black_24dp.png");
        ActionListener deleteHandler = e -> {
            this.model.control().invoke(new DereserveBookCmd(this.book, this.user));
        };
        Button icon = new Button(img, deleteHandler, DELETEBGCOLOR);
        JComponent wrapper = Margin.vertical(Margin.horizontal(icon, ICONHMARGIN), ICONVMARGIN);
        wrapper.setBackground(DELETEBGCOLOR);
        wrapper.setOpaque(true);
        return wrapper;
    }
    
    public JComponent infoButton() {
        ActionListener infoHandler = e -> {
            BookResult bk = new BookResult(model, book);
            bk.setFrame(this.frame);
            bk.paint();
            if (frame != null) {
                bk.popupBookData(frame);
            } else {
                bk.popupBookData();
            }
        };
        return this.infoButton(infoHandler);
    }
    public JComponent infoButton(ActionListener handler) {
        ImageIcon img = new ImageIcon("./images/outline_info_black_24dp.png");
        Button icon = new Button(img, handler, INFOBGCOLOR);
        JComponent wrapper = Margin.vertical(Margin.horizontal(icon, ICONHMARGIN), ICONVMARGIN);
        wrapper.setBackground(INFOBGCOLOR);
        wrapper.setOpaque(true);
        return wrapper;
    }

  
    
}
