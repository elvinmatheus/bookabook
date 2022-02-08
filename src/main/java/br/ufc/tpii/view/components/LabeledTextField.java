package br.ufc.tpii.view.components;

import javax.swing.JComponent;
import javax.swing.JTextField;

import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.view.components.fixed.LimitedJTextField;
import br.ufc.tpii.view.components.layout.PackLayout;

public class LabeledTextField extends JComponent {

    /** Define um componente que é um label seguido de um campo de texto,
     * com espaçamento padrão. Esse componente segue um fluxo horizontal (PackLayout.X_AXIS).
     */

    final static int SPACEBETWEENLABELANDTEXT = 10;
    final static int DEFAULTMINFIELDWIDTH = 100;
    final static int DEFAULTMAXFIELDWIDTH = 120;

    private JTextField textField;

    public LabeledTextField(String text) {
        PackLayout layout = new PackLayout(this, PackLayout.X_AXIS);
        this.setLayout(layout);
        Label label = new Label(text);
        LimitedJTextField field = new LimitedJTextField(DEFAULTMINFIELDWIDTH, DEFAULTMAXFIELDWIDTH);
        this.add(label);
        this.add(Margin.rigidHorizontal(SPACEBETWEENLABELANDTEXT));
        this.add(field);
        this.textField = field;
    }

    public JTextField getTextField() {
        return this.textField;
    }
    
}
