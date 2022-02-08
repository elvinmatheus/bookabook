package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.view.pages.admin.SearchUsersResult;

public class SearchUsersHandler implements ActionListener {

    /** Responde ao click de "Buscar", quando o usuário tenta buscar uma lista de usuários
     * conforme certos filtros (Nome e Matrícula).
     */

    private List<JTextField> fields;
    private List<JCheckBox> checks;
    public SearchUsersHandler(List<JTextField> fields, List<JCheckBox> checks) {
        /** lerá esses campos quando o usuário clicar no botão Buscar */
        this.fields = fields;
        this.checks = checks;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String nameFilter = null, matriculaFilter = null;
        if (this.checks.get(0).isSelected()) {
            nameFilter = this.fields.get(0).getText();
        }
        if (this.checks.get(1).isSelected()) {
            matriculaFilter = this.fields.get(1).getText();
        }
        // vamos à pagina de busca
        // a página receberá os parâmetros de filtro como argumento
        App.get().control().invoke(new NavigateCmd(new SearchUsersResult(nameFilter, matriculaFilter)));
    }


    
    
}
