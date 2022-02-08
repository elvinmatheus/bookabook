package br.ufc.tpii.view.components.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class StretchLayout extends BoxLayout {

    /**
     * LayoutManager que faz com que os componentes filhos ocupem espaço
     * igual ao maior preferredSize entre todos os filhos.
     */
    
    public StretchLayout(Container target, int axis) {
        super(target, axis);
    }

    @Override
    public void layoutContainer(Container target) {
        super.layoutContainer(target);
        int size = this.getAxis() == BoxLayout.X_AXIS ? this.minimumLayoutSize(target).height : this.minimumLayoutSize(target).width;
        for (Component c : target.getComponents()) {
            if (this.getAxis() == BoxLayout.X_AXIS) {
                c.setSize(c.getWidth(), size);
            } else {
                c.setSize(size, c.getHeight());
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        int height = 0;
        int width = 0;
        if (this.getAxis() == StretchLayout.X_AXIS) {
            for (Component c : target.getComponents()) {
                Dimension min = c.getMinimumSize();
                int csize = c.getPreferredSize().height;
                if (csize > height) {
                    height = csize;
                }
                width += min.width;
            }
        } else {
            for (Component c : target.getComponents()) {
                Dimension min = c.getMinimumSize();
                int csize = c.getPreferredSize().width;
                if (csize > width) {
                    width = csize;
                }
                height += min.height;
            }
        }
        return new Dimension(width, height);
    }

    public static JComponent createVerticalBox() {
        JComponent box = new JPanel();
        StretchLayout layout = new StretchLayout(box, StretchLayout.Y_AXIS);
        box.setLayout(layout);
        return box;
    }
    public static JComponent createHorizontalBox() {
        JComponent box = new JPanel();
        StretchLayout layout = new StretchLayout(box, StretchLayout.X_AXIS);
        box.setLayout(layout);
        return box;
    }

}
