package br.ufc.tpii.controller.handlers;

import java.util.function.Consumer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NotifyTextChangeListener implements DocumentListener {

    /** Executa o handler sempre que o texto for alterado.
     * É um DocumentListener (e não ActionListener), pois é um listener de campos de texto
     */

    private Consumer<Void> handler;
    public NotifyTextChangeListener(Consumer<Void> handler) {
        this.handler = handler;
    } 

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.handler.accept(null);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.handler.accept(null);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.handler.accept(null);
    }
    
}
