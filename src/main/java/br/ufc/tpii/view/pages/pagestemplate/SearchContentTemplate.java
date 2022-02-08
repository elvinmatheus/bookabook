package br.ufc.tpii.view.pages.pagestemplate;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.view.components.Button;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.base.MenuFactory;
import br.ufc.tpii.view.components.fixed.LimitedJTextField;
import br.ufc.tpii.view.components.layout.PackLayout;

public class SearchContentTemplate {
    /* Template para construção de layouts de pesquisa.

    Esse layout é basicamente uma sequência vertical de labels + textFields,
    seguidos por botões dispostos horizontalmente.
    
    Opcionalmente coloca um checkbox à esquerda de cada label. */

    final static int VERTICALGAPMAINCONTENT = 15;
    final static int LEFTRIGHTMARGINMAINCONTENT = 50;
    final static int LEFTRIGHTMARGINWRAPPER = MenuFactory.WRAPPERHORIZONTALMARGIN;
    final static int BOTTOMMARGINMAINWRAPPER = 80;
    final static int SPACEBETWEENCHECKANDLABEL = 2;
    final static int SPACEBETWEENLABELANDFIELD = 10;
    final static int SPACEBETWEENBUTTONS = 30;
    final static int SPACEBETWEENBUTTONSANDMAINCONTENT = 30;
    final static int FIELDMINWIDTH = 200;
    final static int FIELDMAXWIDTH = 500;
    final static int DEFAULTVERTICALMARGIN = 20;

    final static Color BGCOLOR = new Color(187, 187, 187);
    final static Color MENUCOLOR = new Color(66, 66, 69);
    final static Color LABELCOLOR = new Color(220, 220, 220);
    public static Color MAINWRAPPERCOLOR = new Color(255, 252, 252);

    private String[] labelsText;
    private String[] buttonsText;
    private ActionListener[] handlers;
    private boolean checked;
    private int horizontalMargin;
    private boolean useGlue;
    private Button[] buttons;
    private List<JTextField> textFields;
    private List<JCheckBox> checkComponents;

    public Button[] getButtons() { return this.buttons; }
    public List<JTextField> getTextFields() { return this.textFields; }
    public List<JCheckBox> getCheckBoxs() { return this.checkComponents; }
    /** Retorna todos os campos que podem ser limpados (checkboxes e textFields) */
    public List<JComponent> getClearableFields() {
        List<JComponent> list = new ArrayList<>(this.textFields);
        list.addAll(this.checkComponents);
        return list;
    }

    public SearchContentTemplate(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked) {
        this(labelsText, buttonsText, handlers, checked, LEFTRIGHTMARGINWRAPPER, true);
    }
    public SearchContentTemplate(String[] labelsText, String[] buttonsText, boolean checked, boolean useGlue) {
        this(labelsText, buttonsText, null, checked, LEFTRIGHTMARGINWRAPPER, useGlue);
    }

    public SearchContentTemplate(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked, int horizontalMargin, boolean useGlue) {
        this.labelsText = labelsText;
        this.buttonsText = buttonsText;
        this.handlers = handlers;
        this.checked = checked;
        this.horizontalMargin = horizontalMargin == -1 ? LEFTRIGHTMARGINWRAPPER : horizontalMargin;
        this.useGlue = useGlue;
        this.buttons = new Button[buttonsText == null ? 0 : buttonsText.length];
        this.textFields = new ArrayList<>();
        this.checkComponents = new ArrayList<>();
    }
    public void setHandlers(ActionListener[] handlers) {
        this.handlers = handlers;
        for (int i = 0; i < handlers.length; i++) {
            this.buttons[i].addActionListener(handlers[i]);
        }
    }

    /* Constrói um componente com labels seguidos de campos de texto.
    Se checked for true, também adiciona checkboxes à esquerda dos labels */
    public static JComponent mainContent(String[] labelsText, boolean checked) {
        return new SearchContentTemplate(labelsText, null, null, checked).mainContent();
    }

    public JComponent mainContent() {
        JComponent component = new JPanel();
        PackLayout layout = new PackLayout(component, PackLayout.Y_AXIS);
        component.setLayout(layout);
        for (int i = 0; i < labelsText.length; i++) {
            // vamos construir a linha horizontal,
            // ela será composta por uma sequência: checkbox (opcional) + label + textfield
            Box line = Box.createHorizontalBox();
            if (checked) {
                // caller quer o checkbox! vamos adicioná-lo
                JCheckBox check = new JCheckBox();
                check.setOpaque(false);
                this.checkComponents.add(check);
                line.add(check);
                line.add(Margin.rigidHorizontal(SPACEBETWEENCHECKANDLABEL));
            }
            // adicinamos o label, e depois o textfield
            Label label = new Label(labelsText[i]);
            LimitedJTextField textField = new LimitedJTextField(FIELDMINWIDTH, FIELDMAXWIDTH);
            this.textFields.add(textField);
            line.add(label);
            line.add(Box.createHorizontalGlue()); // dá certo porcausa de PackLayout
            line.add(Margin.rigidHorizontal(SPACEBETWEENLABELANDFIELD));
            line.add(textField);
            if (i > 0) {
                component.add(Margin.rigidVertical(VERTICALGAPMAINCONTENT));
            }
            component.add(line);
        }
        component.setBackground(MAINWRAPPERCOLOR);
        return component;
    }

    /* Constrói um componente com labels seguidos de texto, e ao final uma lista de botões.
    Se checked for true, também adiciona checkboxes à esquerda dos labels */
    public static JComponent build(String[] labelsText, String[] buttonsText, boolean checked) {
        return build(labelsText, buttonsText, checked, LEFTRIGHTMARGINWRAPPER);
    }
    
    public static JComponent build(String[] labelsText, String[] buttonsText, boolean checked, int horizontalMargin) {
        return build(labelsText, buttonsText, null, checked, horizontalMargin, true);
    }
    public static JComponent build(String[] labelsText, String[] buttonsText, boolean checked, boolean useGlue) {
        return build(labelsText, buttonsText, null, checked, LEFTRIGHTMARGINWRAPPER, useGlue);
    }

    public static JComponent build(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked) {
        return build(labelsText, buttonsText, handlers, checked, LEFTRIGHTMARGINWRAPPER);
    }
    
    public static JComponent build(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked, int horizontalMargin) {
        return build(labelsText, buttonsText, handlers, checked, horizontalMargin, true);
    }
    public static JComponent build(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked, boolean useGlue) {
        return build(labelsText, buttonsText, handlers, checked, LEFTRIGHTMARGINWRAPPER, useGlue);
    }

    public static JComponent build(String[] labelsText, String[] buttonsText, ActionListener[] handlers, boolean checked, int horizontalMargin, boolean useGlue) {
        return new SearchContentTemplate(labelsText, buttonsText, handlers, checked, horizontalMargin, useGlue).build();
    }

    public JComponent build() {
        // Constrói um componente que é basicamente uma sequência vertical de labels + textfields,
        // seguido por botões dispostos horizontalmente
        // Além disso, há uma margem padrão, por isso essa declaração de wrappers abaixo.
        JComponent wrapper = Box.createVerticalBox(); // wrapper vertical
        Box buttonsComponent = Box.createHorizontalBox(); // botões na horizontal
        buttonsComponent.add(Box.createHorizontalGlue()); // vamos colocar cola no início e no fim para centralizar os botões
        for (int i = 0; i < buttonsText.length; i++) {
            Button bttn = new Button(buttonsText[i]);
            this.buttons[i] = bttn;
            if (handlers != null) {
                ActionListener handler = handlers[i];
                if (handler != null) {
                    bttn.addActionListener(handler);
                }
            }
            if (i > 0) {
                // espaçamento padrão entre botões
                buttonsComponent.add(Margin.rigidHorizontal(SPACEBETWEENBUTTONS));
            }
            // adicionamos o botão
            buttonsComponent.add(bttn);
        }
        buttonsComponent.add(Box.createHorizontalGlue()); // cola final, pra centralizar os botões
        // se useGlue for verdadeiro, vamos colocar uma cola no início e no final do wrapper vertical,
        // para que o conteúdo fique centralizado e expanda se houver espaço disponível
        if (useGlue) wrapper.add(Box.createVerticalGlue());
        wrapper.add(Margin.rigidVertical(DEFAULTVERTICALMARGIN));
        wrapper.add(this.mainContent()); // conteúdo principal: é a sequência de labels + textfields
        wrapper.add(Margin.rigidVertical(SPACEBETWEENBUTTONSANDMAINCONTENT));
        wrapper.add(buttonsComponent); // botões dispostos na horizontal
        if (useGlue) wrapper.add(Box.createVerticalGlue());
        wrapper.add(Margin.rigidVertical(DEFAULTVERTICALMARGIN));
        wrapper.setOpaque(true);
        wrapper.setBackground(MAINWRAPPERCOLOR);
        return Margin.horizontal(wrapper, this.horizontalMargin);
    }

}
