package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClearHandler<T extends JComponent> implements ActionListener {

    /** Limpa componentes (lista deles é recebido como argumento).
     * Limpar significa ir a um estado inicial (texto vazio, checkbox desmarcado,
     * combobox com índice 0 selecionado)
     */

    private List<T> components;
    public ClearHandler(List<T> components) {
        this.components = components;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.handler(this.components);
    }

    @SuppressWarnings("unchecked")
    public void handler(List<T> components) {
        for (JComponent c : components) {
            if (c instanceof JTextField) {
                ((JTextField)c).setText("");
            } else if (c instanceof JCheckBox) {
                ((JCheckBox)c).setSelected(false);
            } else if (c instanceof JComboBox) {
                ((JComboBox<String>)c).setSelectedIndex(0);
            } else if (c instanceof JTextArea) {
                ((JTextArea)c).setText("");
            }
        }
    }
    
    
}
