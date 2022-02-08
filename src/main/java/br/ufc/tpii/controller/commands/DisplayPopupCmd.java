package br.ufc.tpii.controller.commands;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;

/** Comando concreto.
 * 
 * Exibe um popup de erro.
 */
public class DisplayPopupCmd implements Command {

    JFrame frame; // frame (janela) pai desse popup (pode ser null, significando que esse popup não tem pai)
    String message, title; // mensagem e título da janela popup
    int messageType; // tipo de mensagem (definido por JOPtionPane)
    // exemplo: JOptionPane.INFORMATION_MESSAGE, JOptionPane.ERROR_MESSAGE

    private final static String defaultTitle = ""; // título padrão da janela popup exibida
    private final static int defaultMessageType = JOptionPane.INFORMATION_MESSAGE;

    /* Construtores simplesmente povoam os atributos */
    public DisplayPopupCmd(JFrame frame, String message, String title, int messageType) {
        this.frame = frame;
        this.message = message;
        this.title = title;
        this.messageType = messageType;
    }
    public DisplayPopupCmd(String message, int messageType) {
        this(App.get().getFrame(), message, DisplayPopupCmd.defaultTitle, messageType);
    }
    public DisplayPopupCmd(String message) {
        this(App.get().getFrame(), message, DisplayPopupCmd.defaultTitle, DisplayPopupCmd.defaultMessageType);
    }

    @Override
    public void execute() {
        // se não devemos ignorar esse popup, ou se essa mensagem é um erro
        // devemos mostrar o popup. Veja que JOptionPane.ERROR_MESSAGE sempre são exibidos
        // e o App permite ignorar os popups via shouldIgnorePopup,
        // útil para o pré-cadastro por exemplo (não queremos vários popups
        // assim que iniciamos a aplicação)
        if (!App.get().shouldIgnorePopup() || this.messageType == JOptionPane.ERROR_MESSAGE) {
            JOptionPane.showMessageDialog(this.frame, this.message, title, this.messageType);
        }
    }
    
    @Override
    public String log() {
        Object data[] = new Object[] {this.frame, this.message, this.title, this.messageType};
        return Arrays.toString(data);
    }
}
