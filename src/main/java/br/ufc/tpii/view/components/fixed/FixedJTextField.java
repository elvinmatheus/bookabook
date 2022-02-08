package br.ufc.tpii.view.components.fixed;

import java.awt.Dimension;

import javax.swing.JTextField;

public class FixedJTextField extends JTextField {

    private Dimension size;

    public FixedJTextField(Dimension size) {
        super();
        this.size = size;
    }
    public FixedJTextField(Dimension size, String text) {
        super(text);
        this.size = size;
    }
    public FixedJTextField(int width) {
        super();
        this.size = new Dimension(width, super.getPreferredSize().height);
    }
    public FixedJTextField(int width, String text) {
        super(text);
        this.size = new Dimension(width, super.getPreferredSize().height);
    }
    
    public FixedJTextField(String text) {
        super(text);
        this.size = null;
    }

    @Override
    public Dimension getMaximumSize() {
        if (this.size == null) {
            return super.getPreferredSize();
        }
        return this.size;
    }
    @Override
    public Dimension getMinimumSize() {
        return this.getMaximumSize();
    }
    @Override
    public Dimension getPreferredSize() {
        return this.getMaximumSize();
    }
}
