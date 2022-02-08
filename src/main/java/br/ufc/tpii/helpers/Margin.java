package br.ufc.tpii.helpers;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;

/** Classe helper que adiciona margem ao criar um wrapper Box que contém um componente invisível */
public class Margin {

    public static Box horizontalLeft(JComponent c, int margin) {
        Box wrapper = Box.createHorizontalBox();
        wrapper.add(Box.createRigidArea(new Dimension(margin, 1)));
        wrapper.add(c);
        return wrapper;
    }
    public static Box horizontalRight(JComponent c, int margin) {
        Box wrapper = Box.createHorizontalBox();
        wrapper.add(c);
        wrapper.add(Box.createRigidArea(new Dimension(margin, 1)));
        return wrapper;
    }
    public static Box horizontal(JComponent c, int margin) {
        Box wrapper = Box.createHorizontalBox();
        wrapper.add(Box.createRigidArea(new Dimension(margin, 1)));
        wrapper.add(c);
        wrapper.add(Box.createRigidArea(new Dimension(margin, 1)));
        return wrapper;
    }
    public static Box verticalLeft(JComponent c, int margin) {
        Box wrapper = Box.createVerticalBox();
        wrapper.add(Box.createRigidArea(new Dimension(1, margin)));
        wrapper.add(c);
        return wrapper;
    }
    public static Box verticalRight(JComponent c, int margin) {
        Box wrapper = Box.createVerticalBox();
        wrapper.add(c);
        wrapper.add(Box.createRigidArea(new Dimension(1, margin)));
        return wrapper;
    }
    public static Box vertical(JComponent c, int margin) {
        Box wrapper = Box.createVerticalBox();
        wrapper.add(Box.createRigidArea(new Dimension(1, margin)));
        wrapper.add(c);
        wrapper.add(Box.createRigidArea(new Dimension(1, margin)));
        return wrapper;
    }

    public static Component rigidHorizontal(int width) {
        return Box.createRigidArea(new Dimension(width, 1));
    }
    public static Component rigidVertical(int height) {
        return Box.createRigidArea(new Dimension(1, height));
    }

    public static Box glueRight(JComponent c) {
        Box b = Box.createHorizontalBox();
        b.add(c);
        b.add(Box.createHorizontalGlue());
        return b;
    }
    public static Box glueLeft(JComponent c) {
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(c);
        return b;
    }

    public static Box glueHorizontal(JComponent c) {
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(c);
        b.add(Box.createHorizontalGlue());
        return b;
    }

    public static Box glueBottom(JComponent c) {
        Box b = Box.createVerticalBox();
        b.add(c);
        b.add(Box.createVerticalGlue());
        return b;
    }
    public static Box glueTop(JComponent c) {
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(c);
        return b;
    }

    public static Box glueVertical(JComponent c) {
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalGlue());
        b.add(c);
        b.add(Box.createVerticalGlue());
        return b;
    }
}
