package br.ufc.tpii.view.components;

import java.awt.event.ActionListener;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class InfoButton extends Button {

    private static ImageIcon icon = new ImageIcon("./images/outline_info_black_24dp.png");
    public final static Color DEFAULTBGCOLOR = new Color(196, 196, 196);
    private final static int DEFAULTPADDING = 10;

    public InfoButton(ActionListener handler) {
        this(handler, DEFAULTBGCOLOR);
    }
    public InfoButton(ActionListener handler, Color background) {
        this(handler, background, DEFAULTPADDING);
    }
    public InfoButton(ActionListener handler, Color background, int padding) {
        super(icon, handler, background != null ? background : DEFAULTBGCOLOR);
        Border border = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
        this.setBorder(border);
    }

    
    
}
