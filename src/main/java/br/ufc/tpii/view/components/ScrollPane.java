package br.ufc.tpii.view.components;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class ScrollPane extends JScrollPane {
    
    public ScrollPane(Component view) {
        super(view);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getVerticalScrollBar().setUnitIncrement(16);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

}
