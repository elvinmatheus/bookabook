package br.ufc.tpii.framework;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import br.ufc.tpii.controller.RefreshID;

public abstract class View {

    protected App model;
    List<View> subviews;
    public View(App model) {
        this.model = model;
        this.subviews = new ArrayList<View>();
    }

    /** Modifica frame, adicionando componentes a ele de forma a construir a página.
     * 
     * Pede para a View (Page) ser exibida conforme dados do Model (App)
     */
    public abstract JComponent paint();

    /**
     * Atualiza essa view (e seus subcomponentes) sobre uma alteração do modelo.
     * @param changeID - Identificação da alteração do modelo, utilizado
     * para que as views possam reagir apenas a certos tipos de alterações.
     */
    public void refresh(RefreshID changeID, Object... args) {
        for (View v : this.subviews) {
            v.refresh(changeID, args);
        }
    }

    public void addView(View v) {
        this.subviews.add(v);
    }
    public void removeView(View v) {
        this.subviews.remove(v);
    }
    public void removeAllViews() {
        this.subviews.clear();
    }    
}
