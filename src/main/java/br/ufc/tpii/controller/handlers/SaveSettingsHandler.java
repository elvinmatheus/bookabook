package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTextField;

import br.ufc.tpii.controller.commands.ChangeSettings;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.framework.App;

public class SaveSettingsHandler implements ActionListener {

    /** Lê os campos de texto das configurações
     * e chama os comandos para alterar o modelo conforme os campos de texto.
     * Antes disso, checa se os campos de texto possuem conteúdo válido.
     */

    private List<JTextField> fields;
    public SaveSettingsHandler(List<JTextField> fields) {
        this.fields = fields;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        App app = App.get();
        String prazoStr = this.fields.get(0).getText();
        int prazo, maxQuantity;
        double multa;
        try {
            prazo = Integer.parseInt(prazoStr);
            if (prazo < 0) throw new NumberFormatException();
        } catch (NumberFormatException exc) {
            app.control().invoke(new DisplayPopupCmd("Não foi possível salvar, pois o prazo especificado não é um número válido. Recebido: " + prazoStr));
            return;
        }

        String maxQuantityStr = this.fields.get(1).getText();
        try {
            maxQuantity = Integer.parseInt(maxQuantityStr);
            if (maxQuantity < 0) throw new NumberFormatException();
        } catch (NumberFormatException exc) {
            app.control().invoke(new DisplayPopupCmd("Não foi possível salvar, pois a quantidade máxima de empréstimos especificada não é um número válido. Recebido: " + maxQuantityStr));
            return;
        }

        String multaStr = this.fields.get(2).getText();
        try {
            multa = Double.parseDouble(multaStr);
            if (multa < 0) throw new NumberFormatException();
        } catch (NumberFormatException exc) {
            app.control().invoke(new DisplayPopupCmd("Não foi possível salvar, pois a multa especificada não é um valor válido. Recebido: " + maxQuantityStr));
            return;
        }

        app.control().invoke(ChangeSettings.prazoEmprestimo(prazo));
        app.control().invoke(ChangeSettings.maxQuantityEmprestimo(maxQuantity));
        app.control().invoke(ChangeSettings.multa(multa));
        app.control().invoke(new DisplayPopupCmd("Alterações salvas com sucesso"));
    }
}
