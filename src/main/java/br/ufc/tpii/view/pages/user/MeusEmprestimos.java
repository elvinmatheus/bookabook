package br.ufc.tpii.view.pages.user;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.EmprestimoItem;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.ScrollPane;
import br.ufc.tpii.view.pages.Home;

public class MeusEmprestimos extends Page {

    public final static String TITLE = "Meus Empréstimos";
    @Override
    public String getTitle() { return TITLE; }

    private final static int SPACEBETWEENITEMS = 5;

    private final static Dimension scrollMinDim = new Dimension(300, 120);

    private User user;
    private JFrame frame = null;

    public MeusEmprestimos(User user) {
        this(user, null);
    }
    public MeusEmprestimos(User user, JFrame frame) {
        this.user = user;
        this.frame = frame;
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
        pane.add(this.mainWrapper(app, user, this.itemsList));
        //pane.add(Home.foot());
        return pane;
    }

    private JComponent mainWrapper(App app, User user, JComponent content) {
        return MyList.mainWrapper(app, user, content, 1);
    }

    private JScrollPane scrollPane;
    private JComponent mainContent(App app, User user) {
        JComponent content = Box.createVerticalBox();
        List<Emprestimo> emprestimos = user.getData().getEmprestimos();
        if (emprestimos.size() == 0) {
            content.add(new Label("Não há empréstimos pendentes"));
            return content;
        }
        for (int i = 0; i < emprestimos.size(); i++) {
            Emprestimo emprestimo = emprestimos.get(i);
            boolean fresh = true;
            EmprestimoItem view = new EmprestimoItem(app, emprestimo, fresh);
            view.setFrame(this.frame);
            this.addView(view);
            if (i > 0) content.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            content.add(view.paint());
        }
        this.scrollPane = new ScrollPane(content);
        this.scrollPane.setMinimumSize(scrollMinDim);
        return this.scrollPane;
    }
    public static JComponent buildList(App app, User user) {
        return MeusEmprestimos.buildList(app, user, null);
    }
    public static JComponent buildList(App app, User user, JFrame frame) {
        MeusEmprestimos m = new MeusEmprestimos(user, frame);
        return m.mainContent(app, user);
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
