package br.ufc.tpii.framework;

import javax.swing.JFrame;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;

public class GUI {
    /** Exibe o modelo como uma interface gráfica */

    private JFrame frame; // frame principal da aplicação
    private Page currentPage; // página atual (atualizamos a cada navigate)
    private App app; // model
    public GUI(App app) {
        this.app = app;
        this.frame = new JFrame();
        // queremos que o frame principal encerre a aplicação quando o usuário clicar X nele
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    /** Navega até a página passada como argumento, redesenhando o frame principal */
    public void navigate(Page page) {
        this.currentPage = page;
        page.init(this.app);
        this.frame.setContentPane(page.paint());
        this.app.control().invoke(new RefreshCmd(RefreshID.INIT));
        this.frame.validate();
        this.app.control().invoke(new RefreshCmd(RefreshID.MOUNT));
        this.frame.repaint();
    }
    
    public Page getCurrentPage() {
        return this.currentPage;
    }

    public JFrame getFrame() { return this.frame; }

}
