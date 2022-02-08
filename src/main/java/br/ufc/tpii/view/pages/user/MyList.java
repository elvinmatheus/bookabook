package br.ufc.tpii.view.pages.user;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.EmprestimoItem;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.ScrollPane;
import br.ufc.tpii.view.pages.Home;

public class MyList extends Page {

    public final static String TITLE = "Minha Lista";
    @Override
    public String getTitle() { return TITLE; }

    private final static int SPACEBETWEENITEMS = 5;

    private final static Dimension scrollMinDim = new Dimension(300, 120);
    private final static int LEFTCONTENTMARGIN = 30;
    private final static int RIGHTCONTENTMARGIN = 60;
    private final static int VERTICALCONTENTMARGIN = 30;

    private User user;
    public MyList(User user) {
        this.user = user;
    }

    private JComponent itemsList;
    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        UserData data = user.getData();
        JComponent menubar = Home.header(app, pane, false, data.getName());

        this.itemsList = Box.createVerticalBox();
        this.itemsList.add(this.mainContent(app, user));
        pane.add(menubar);
        pane.add(MyList.mainWrapper(app, user, this.itemsList, 0));
        //pane.add(Home.foot());
        return pane;
    }

    public static JComponent mainWrapper(App app, User user, JComponent content, int selectIndex) {
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Profile.leftMenu(app, user, selectIndex));
        wrapper.add(Margin.rigidHorizontal(LEFTCONTENTMARGIN));
        wrapper.add(Margin.vertical(Margin.glueHorizontal(content), VERTICALCONTENTMARGIN));
        wrapper.add(Margin.rigidHorizontal(RIGHTCONTENTMARGIN));
        return wrapper;
    }

    private JScrollPane scrollPane;
    private JComponent mainContent(App app, User user) {
        JComponent content = Box.createVerticalBox();
        List<Book> reservedBooks = user.getData().getReservedBooks();
        if (reservedBooks.size() == 0) {
            content.add(new Label("Não há livros reservados"));
            return content;
        }
        for (int i = 0; i < reservedBooks.size(); i++) {
            Book book = reservedBooks.get(i);
            View view = new EmprestimoItem(app, book, user);
            this.addView(view);
            if (i > 0) content.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            content.add(view.paint());
        }
        this.scrollPane = new ScrollPane(content);
        this.scrollPane.setMinimumSize(scrollMinDim);
        return this.scrollPane;
    }
    
    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (RefreshID.UserUnreserveBook == changeID || RefreshID.UserReserveBook == changeID) {
            this.itemsList.removeAll();
            this.itemsList.add(this.mainContent(app, user));
            this.itemsList.revalidate();
        }
        super.refresh(changeID, args);
    }
}
