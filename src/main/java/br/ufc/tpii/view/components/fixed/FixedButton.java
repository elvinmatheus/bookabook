package br.ufc.tpii.view.components.fixed;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import br.ufc.tpii.view.components.Button;

public class FixedButton extends Button {
    /** Esse botão grava o primeiro tamanho que ele possuiu (na primeira vez em que foi renderizado).
     * ELe mantém esse mesmo tamanho para sempre.
     */
    public FixedButton(String text) {
        this(text, null);
    }
    public FixedButton(String text, ActionListener handler) {
        this(text, handler, null, null);
    }
    public FixedButton(String text, ActionListener handler, Color foreground, Color background) {
        super(text, handler, foreground, background);
    }
    private Dimension prefDim;

    @Override
    public void setPreferredSize(Dimension prefSize) {
        if (this.prefDim == null) {
            this.prefDim = prefSize;
        }
        super.setPreferredSize(prefSize);
    }
    @Override
    public Dimension getPreferredSize() {
        if (this.prefDim == null) {
            Dimension dim = super.getPreferredSize();
            if (dim != null) this.prefDim = dim;
            return dim;
        }
        return this.prefDim;
    }
    @Override
    public void setMinimumSize(Dimension prefSize) {
        if (this.prefDim == null) {
            this.prefDim = prefSize;
        }
        super.setMinimumSize(prefSize);
    }
    @Override
    public Dimension getMinimumSize() {
        if (this.prefDim == null) {
            Dimension dim = super.getMinimumSize();
            if (dim != null) this.prefDim = dim;
            return dim;
        }
        return this.prefDim;
    }

    @Override
    public void setMaximumSize(Dimension prefSize) {
        if (this.prefDim == null) {
            this.prefDim = prefSize;
        }
        super.setMaximumSize(prefSize);
    }
    @Override
    public Dimension getMaximumSize() {
        if (this.prefDim == null) {
            Dimension dim = super.getMaximumSize();
            if (dim != null) this.prefDim = dim;
            return dim;
        }
        return this.prefDim;
    }

    
}
