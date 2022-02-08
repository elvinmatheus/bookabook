package br.ufc.tpii.view.pages.user;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.Button;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.layout.StretchLayout;
import br.ufc.tpii.view.pages.Home;

public class Profile extends Page {

    public final static String TITLE = "Perfil";
    @Override
    public String getTitle() { return TITLE; }

    final static Color PROFILEMENUGRAY = new Color(178, 178, 178);
    final static Color PROFILEMENULIGHTGRAY = new Color(238, 236, 236);
    final static Color PROFILEMENULABELCOLOR = new Color(0, 0, 0);
    final static Color PROFILEMARKEDMENULABELCOLOR = new Color(67, 70, 134);
    public final static Color HEADERWELCOMECOLOR = new Color(214, 224, 235);

    final static Font PROFILETITLEFONT = new Font("sansserif", Font.PLAIN, 36);
    final static Font PROFILEBUTTONFONT = new Font("sansserif", Font.PLAIN, 28);
    final static Font CONTENTTITLEFONT = new Font("sansserif", Font.PLAIN, 30);
    public final static Font WELCOMEFONT = new Font("sansserif", Font.PLAIN, 28);

    final static int PROFILEMENUWIDTH = 191;
    final static int PROFILETITLEMARGIN = 6;
    final static int PROFILEBUTTONVERTICALMARGIN = 10;
    final static int PROFILEBUTTONHORIZONTALMARGIN = 25;

    final static int CONTENTTOPMARGIN = 20;
    final static int CONTENTLEFTMARGIN = 18;
    final static int CONTENTTITLEBOTTOMMARGIN = 10;

    final static Border PROFILEMENUBORDER = BorderFactory.createLineBorder(PROFILEMENUGRAY, 1);
    final static Border PROFILEMENUBUTTONBORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1), BorderFactory.createEmptyBorder(PROFILEBUTTONVERTICALMARGIN, PROFILEBUTTONHORIZONTALMARGIN, PROFILEBUTTONVERTICALMARGIN, PROFILEBUTTONHORIZONTALMARGIN));

    private User user;
    public Profile() {
        this(App.get().getLogin().getUser());
    }
    public Profile(User user) {
        this.user = user;
    }

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        UserData data = user.getData();
        JComponent menubar = Home.header(app, pane, false, data.getName());

        JComponent content = this.mainWrapper(app, user);
        pane.add(menubar);
        pane.add(content);
        return pane;
    }

    private JComponent mainContent(App app) {
        User user = app.getLogin().getUser();
        UserData data = user.getData();
        JComponent content = Box.createVerticalBox();
        JComponent profileData = Margin.glueRight(new Label("Dados pessoais", null, null, CONTENTTITLEFONT));
        JComponent emailLabel = Margin.glueRight(new Label("Email: " + data.getEmail()));
        JComponent contactLabel = Margin.glueRight(new Label("Contato: " + data.getContact()));
        profileData.setBackground(Color.blue);
        content.add(Margin.rigidVertical(CONTENTTOPMARGIN));
        content.add(profileData);
        content.add(Margin.rigidVertical(CONTENTTITLEBOTTOMMARGIN));
        content.add(emailLabel);
        content.add(contactLabel);
        content.add(Box.createVerticalGlue());
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Margin.rigidHorizontal(CONTENTLEFTMARGIN));
        wrapper.add(content);
        wrapper.add(Box.createHorizontalGlue());
        return wrapper;
    }
    
    private JComponent mainWrapper(App app, User user) {
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Profile.leftMenu(app, user, -1));
        wrapper.add(this.mainContent(app));
        return wrapper;
    }

    public static JComponent leftMenu(App app, User user, int selectedIndex) {
        JComponent menu = new JPanel();
        StretchLayout layoutManager = new StretchLayout(menu, StretchLayout.Y_AXIS);
        menu.setLayout(layoutManager);
        Label profileLabel = new Label("Perfil", PROFILEMENULABELCOLOR, PROFILEMENUGRAY, PROFILETITLEFONT);
        profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profileLabel.setBorder(new EmptyBorder(PROFILETITLEMARGIN, PROFILETITLEMARGIN, PROFILETITLEMARGIN, PROFILETITLEMARGIN));
        ActionListener myListHandler = e -> app.control().invoke(new NavigateCmd(new MyList(user)));
        ActionListener meusEmprestimosHandler = e -> app.control().invoke(new NavigateCmd(new MeusEmprestimos(user)));
        Button myList = new Button("Minha lista", myListHandler, selectedIndex == 0 ? PROFILEMARKEDMENULABELCOLOR : PROFILEMENULABELCOLOR, PROFILEMENULIGHTGRAY, PROFILEBUTTONFONT);
        Button pending = new Button("Meus empréstimos", meusEmprestimosHandler, selectedIndex == 1 ? PROFILEMARKEDMENULABELCOLOR : PROFILEMENULABELCOLOR, PROFILEMENULIGHTGRAY, PROFILEBUTTONFONT);
        /* Pintamos as bordas */
        myList.setBorder(PROFILEMENUBUTTONBORDER); myList.setBorderPainted(true);
        pending.setBorder(PROFILEMENUBUTTONBORDER); pending.setBorderPainted(true);
        menu.add(profileLabel);
        menu.add(myList);
        menu.add(pending);
        menu.add(Box.createVerticalGlue());
        menu.setBorder(PROFILEMENUBORDER);
        return menu;
    }
    
}
