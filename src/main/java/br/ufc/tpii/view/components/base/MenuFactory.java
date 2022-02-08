package br.ufc.tpii.view.components.base;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.controller.commands.LogoutCmd;
import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.view.components.Button;
import br.ufc.tpii.view.pages.Help;

public abstract class MenuFactory {

    /** Define como construir vários itens relacionados a uma barra de menu
     * (botões, labels, menuItems...).
     * 
     * Além disso, aplica um espaçamento padrão dado uma barra de menu (JMenuBar)
     * via o método wrap.
     * 
     * Alguns botões padrões já são implementados aqui, como "Sair" e "Ajuda".
     */

    private final static int WRAPPERTOPMARGIN = 30;
    public final static int WRAPPERHORIZONTALMARGIN = 60;

    public final static Color MENUCOLOR = new Color(66, 66, 69);
    private final static Color MENUITEMCOLOR = MENUCOLOR;
    private final static Color LABELCOLOR = new Color(220, 220, 220);
    private final static Color MENUITEMLABELCOLOR = LABELCOLOR;
    private final static Color WRAPPERCOLOR = new Color(124, 114, 181);

    public static JComponent wrap(JMenuBar menubar) {
        Box wrapper2 = Box.createHorizontalBox();
        wrapper2.add(Margin.rigidHorizontal(WRAPPERHORIZONTALMARGIN));
        wrapper2.add(menubar);
        wrapper2.add(Margin.rigidHorizontal(WRAPPERHORIZONTALMARGIN));
        Box wrapper = Box.createVerticalBox();
        wrapper.add(Margin.rigidVertical(WRAPPERTOPMARGIN));
        wrapper.add(wrapper2);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, menubar.getMaximumSize().height + WRAPPERTOPMARGIN));
        wrapper.setOpaque(true);
        wrapper.setBackground(WRAPPERCOLOR);
        return wrapper;
    }

    public static JMenu createMenu(String text, JComponent... items) {
        JMenu menu = new JMenu(text);
        for (JComponent item : items) {
            menu.add(item);
        }
        menu.setOpaque(true);
        menu.setBackground(MENUCOLOR);
        menu.setForeground(LABELCOLOR);
        return menu;
    }

    public static JMenuItem createMenuItem(String text, ActionListener handler) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(MENUITEMLABELCOLOR);
        item.setBackground(MENUITEMCOLOR);
        item.addActionListener(handler);
        return item;
    }

    public static JMenuItem createMenuItem(String text) {
        return MenuFactory.createMenuItem(text, null);
    }

    public static Button createButton(String text, ActionListener handler) {
        Button button = new Button(text, handler, LABELCOLOR, MENUCOLOR);
        return button;
    }
    public static Button createButton(String text) {
        return MenuFactory.createButton(text, null);
    }

    public static Button exitButton(App app) {
        Button button = MenuFactory.createButton("Sair", e -> app.control().invoke(new LogoutCmd()));
        return button;
    }

    public static Button helpButton(App app) {
        Button button = MenuFactory.createButton("Ajuda", e -> app.control().invoke(new NavigateCmd(new Help())));
        return button;
    }
    
}
