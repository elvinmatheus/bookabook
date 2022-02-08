package br.ufc.tpii.view.components;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.admin.EmprestimosEDevolucoes;
import br.ufc.tpii.view.pages.admin.RegisterAdmins;
import br.ufc.tpii.view.pages.admin.RegisterBooks;
import br.ufc.tpii.view.pages.admin.RegisterUsers;
import br.ufc.tpii.view.pages.admin.Reports;
import br.ufc.tpii.view.pages.admin.Reservations;
import br.ufc.tpii.view.pages.admin.SearchBooks;
import br.ufc.tpii.view.pages.admin.SearchUsers;
import br.ufc.tpii.view.pages.admin.Settings;

public class AdminMenu extends JMenuBar {

    /** Define como construir o menu de administrador. */

    /** Retorna o menu de pesquisa */
    private JMenu pesquisa(App app) {
        JMenuItem livros = MenuFactory.createMenuItem("Livros", e -> app.control().invoke(new NavigateCmd(new SearchBooks())));
        JMenuItem usuarios = MenuFactory.createMenuItem("Usuários", e -> app.control().invoke(new NavigateCmd(new SearchUsers())));
        JMenu menu = MenuFactory.createMenu("Pesquisa", livros, usuarios);
        return menu;
    }

    private JMenu circulacao(App app) {
        JMenuItem cadastro = MenuFactory.createMenuItem("Cadastro de Usuários", e -> app.control().invoke(new NavigateCmd(new RegisterUsers())));
        JMenuItem emprestimos = MenuFactory.createMenuItem("Empréstimos e Devoluções", e -> app.control().invoke(new NavigateCmd(new EmprestimosEDevolucoes())));
        JMenuItem reservas = MenuFactory.createMenuItem("Reservas", e -> app.control().invoke(new NavigateCmd(new Reservations())));
        JMenu menu = MenuFactory.createMenu("Circulação", cadastro, emprestimos, reservas);
        return menu;
    }

    private Button catalogacao(App app) {
        Button menu = MenuFactory.createButton("Catalogação", e -> app.control().invoke(new NavigateCmd(new RegisterBooks())));
        return menu;
    }

    private JMenu administracao(App app) {
        JMenuItem relatorios = MenuFactory.createMenuItem("Relatórios", e -> app.control().invoke(new NavigateCmd(new Reports())));
        JMenuItem configs = MenuFactory.createMenuItem("Configurações", e -> app.control().invoke(new NavigateCmd(new Settings())));
        JMenuItem cadastroDeAdmins = MenuFactory.createMenuItem("Cadastro de Admins", e -> app.control().invoke(new NavigateCmd(new RegisterAdmins())));
        JMenu menu = MenuFactory.createMenu("Administração", relatorios, configs, cadastroDeAdmins);
        return menu;
    }

    private Button ajuda(App app) {
        Button button = MenuFactory.helpButton(app);
        return button;
    }

    private Button sair(App app) {
        Button button = MenuFactory.exitButton(app);
        return button;
    }

    public AdminMenu(App app) {
        BoxLayout bLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(bLayout);
        JMenu pesquisaMenu = this.pesquisa(app);
        JMenu circulacaoMenu = this.circulacao(app);
        Button catalogMenu = this.catalogacao(app);
        JMenu adminMenu = this.administracao(app);
        Button helpBttn = this.ajuda(app);
        Button exitBttn = this.sair(app);
        this.add(pesquisaMenu);
        this.add(circulacaoMenu);
        this.add(catalogMenu);
        this.add(adminMenu);
        this.add(helpBttn);
        this.add(Box.createHorizontalGlue());
        this.add(exitBttn);
        this.setBackground(MenuFactory.MENUCOLOR);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height);
    }

    public static JComponent withWrapper(App app) {
        return MenuFactory.wrap(new AdminMenu(app));
    }
}
