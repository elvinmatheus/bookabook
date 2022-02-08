package br.ufc.tpii.view.components.fixed;

import java.awt.Dimension;

import javax.swing.JTextField;

public class LimitedJTextField extends JTextField {

    private Dimension min, max;

    private void setMinMax(Dimension min, Dimension max) {
        this.min = min;
        this.max = max;
    }
    private void setMinMax(int min, int max) {
        this.min = new Dimension(min, super.getPreferredSize().height);
        this.max = new Dimension(max, super.getPreferredSize().height); 
    }

    public LimitedJTextField(Dimension min, Dimension max) {
        super();
        setMinMax(min, max);
    }
    public LimitedJTextField(Dimension min, Dimension max, String text) {
        super(text);
        setMinMax(min, max);
    }
    public LimitedJTextField(int min, int max) {
        super();
        setMinMax(min, max);
    }
    public LimitedJTextField(int min, int max, String text) {
        super(text);
        setMinMax(min, max);
    }
    public LimitedJTextField(String text) {
        super(text);
        setMinMax(null, null);
    }

    @Override
    public Dimension getMaximumSize() {
        if (this.max == null) {
            return super.getPreferredSize();
        }
        return this.max;
    }
    @Override
    public Dimension getMinimumSize() {
        if (this.min == null) {
            return super.getPreferredSize();
        }
        return this.min;
    }
    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }
}
