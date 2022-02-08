package br.ufc.tpii.view.pages.admin;

import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.controller.handlers.ClearHandler;
import br.ufc.tpii.controller.handlers.ReportListHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.components.Button;
import br.ufc.tpii.view.components.LabeledTextField;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class Reports extends Page {

    public final static String TITLE = "Relatórios";
    @Override
    public String getTitle() { return TITLE; }

    final static int CHOOSERMAXWIDTH = 200;
    final static int CHOOSERMAXHEIGHT = 20;
    final static int DATERANGEHORIZONTALGAP = 30;
    final static int SPACEBETWEENCHOOSERANDDATERANGE = 20;
    final static int SPACEBETWEENDATERANGEANDBUTTONS = 120;
    final static int SPACEBETWEENBUTTONS = 70;
    final static int LEFTRIGHTMAINCONTENTMARGIN = MenuFactory.WRAPPERHORIZONTALMARGIN;
    final static int POPUPMARGIN = 30;

    private List<JComponent> components = new ArrayList<>();

    private JComboBox<String> chooser;
    private JTextField fieldFrom, fieldTo;
    private JCheckBox fullReportCheckbox;
    private JButton generateButton;

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String path = "Administração >> Relatórios";
        JComponent content = this.content();
        LayoutTemplate.build(pane, menubar, content, path);
        app.control().invoke(new RefreshCmd(RefreshID.INIT));
        return pane;
    }

    private JComboBox<String> chooser() {
        JComboBox<String> chooser = new JComboBox<String>();
        chooser.addItem("Selecione o relatório...");
        chooser.addItem("Livros emprestados");
        chooser.addItem("Livros devolvidos");
        chooser.addItem("Todos os livros");
        chooser.addItem("Usuários cadastrados");
        chooser.addItem("Edição de cadastro de usuário");
        chooser.setMaximumSize(new Dimension(CHOOSERMAXWIDTH, CHOOSERMAXHEIGHT));
        this.components.add(chooser);
        chooser.addActionListener(e -> {
            int index = chooser.getSelectedIndex();
            this.refresh(RefreshID.CUSTOM1, index);
        });
        return chooser;
    }

    private JComponent selectDateRange() {
        JComponent component = Box.createHorizontalBox();
        LabeledTextField from = new LabeledTextField("De:");
        LabeledTextField to = new LabeledTextField("Até:");
        this.fullReportCheckbox = new JCheckBox("Relatório Completo");
        fullReportCheckbox.setOpaque(false);
        component.add(from);
        component.add(Margin.rigidHorizontal(DATERANGEHORIZONTALGAP));
        component.add(to);
        component.add(Margin.rigidHorizontal(DATERANGEHORIZONTALGAP));
        component.add(fullReportCheckbox);
        this.fieldFrom = from.getTextField();
        this.fieldTo = to.getTextField();
        this.components.add(fieldFrom);
        this.components.add(fieldTo);
        this.components.add(fullReportCheckbox);
        return component;
    }

    private JComponent buttons() {
        ActionListener openPopup = new ReportListHandler(this.chooser, this.fieldFrom, this.fieldTo, this.fullReportCheckbox);
        JComponent component = Box.createHorizontalBox();
        Button cancel = new Button("Cancelar", new ClearHandler<>(this.components));
        this.generateButton = new Button("Gerar", openPopup);
        component.add(Box.createHorizontalGlue());
        component.add(cancel);
        component.add(Margin.rigidHorizontal(SPACEBETWEENBUTTONS));
        component.add(generateButton);
        component.add(Box.createHorizontalGlue());
        return component;
    }

    private JComponent content() {
        JComponent wrapper2 = Box.createHorizontalBox();
        JComponent wrapper1 = Box.createHorizontalBox();
        JComponent component = Box.createVerticalBox();
        this.chooser = this.chooser();
        JComponent selectDateRange = this.selectDateRange();
        JComponent buttons = this.buttons();
        component.add(Box.createVerticalGlue());
        component.add(chooser);
        component.add(Margin.rigidVertical(SPACEBETWEENCHOOSERANDDATERANGE));
        component.add(selectDateRange);
        component.add(Margin.rigidVertical(SPACEBETWEENDATERANGEANDBUTTONS));
        component.add(buttons);
        component.add(Box.createVerticalGlue());
        wrapper1.add(Box.createHorizontalGlue());
        wrapper1.add(component);
        wrapper1.add(Box.createHorizontalGlue());
        wrapper1.setOpaque(true);
        wrapper1.setBackground(SearchContentTemplate.MAINWRAPPERCOLOR);
        wrapper2.add(Margin.rigidHorizontal(LEFTRIGHTMAINCONTENTMARGIN));
        wrapper2.add(wrapper1);
        wrapper2.add(Margin.rigidHorizontal(LEFTRIGHTMAINCONTENTMARGIN));
        return wrapper2;
    }

    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (RefreshID.INIT == changeID) {
            this.generateButton.setEnabled(false);
        }
        if (RefreshID.CUSTOM1 == changeID) {
            int index = (int)args[0];
            this.generateButton.setEnabled(index > 0);
        }
        super.refresh(changeID, args);
    }

}
