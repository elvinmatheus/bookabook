
package br.ufc.tpii.framework;

public abstract class Page extends View {

    protected App app;
    public Page() {
        super(App.get());
    }
    public Page(App app) {
        super(app);
        this.app = app;
    }
    public void init(App app) {
        this.app = app;
    }

    /** Retorna o título da página */
    public abstract String getTitle();
    
}
