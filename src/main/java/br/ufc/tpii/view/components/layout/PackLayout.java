package br.ufc.tpii.view.components.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/* LayoutManager que funciona como o BoxLayout,
mas limita o seu tamanho máximo como o máximo dos tamanhos mínimos de seus subcomponentes.

Assim, você pode usar Box.createGlue() sem fazer com que o tamanho máximo desse componente
seja ilimitado. */
public class PackLayout extends BoxLayout {
    
    public PackLayout(Container target, int axis) {
        super(target, axis);
    }


    @Override
    public Dimension maximumLayoutSize(Container target) {
        Dimension dim = super.maximumLayoutSize(target);
        Component[] components = target.getComponents();
        double maxsize = 0;
        int axis = this.getAxis();
        for (Component component : components) {
            double size;
            if (axis == PackLayout.Y_AXIS) {
                size = component.getMinimumSize().getWidth();
            } else {
                size = component.getMinimumSize().getHeight();
            }
            if (size > maxsize) {
                maxsize = size;
            }
        }
        if (axis == PackLayout.X_AXIS) {
            dim.setSize(dim.getWidth(), maxsize);
        } else {
            dim.setSize(maxsize, dim.getHeight());
        }
        return dim;
    }

    

    public static JComponent createHorizontalBox() {
        JComponent box = new JPanel();
        PackLayout layout = new PackLayout(box, PackLayout.X_AXIS);
        box.setLayout(layout);
        return box;
    }
    public static JComponent createVerticalBox() {
        JComponent box = new JPanel();
        PackLayout layout = new PackLayout(box, PackLayout.Y_AXIS);
        box.setLayout(layout);
        return box;
    }

}
