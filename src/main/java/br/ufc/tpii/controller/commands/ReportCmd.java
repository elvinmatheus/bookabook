package br.ufc.tpii.controller.commands;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;

import br.ufc.tpii.model.Reports;


public class ReportCmd<T> implements Command {

    /** Insere um relatório na base de dados dos relatórios */

    private T report;
    private Reports.Type type;
    private Object[] data;
    /** Especifique dados extras em data, caso seja necessário
     * para registrar uma situação.
     * 
     * Para USER_REGISTER, especifique em data o ADMIN responsável pelo cadastro do usuário
     * Para BOOK_REGISTER, especifique em data o ADMIN responsável pelo cadastro do livro
     */
    public ReportCmd(T report, Reports.Type type, Object... data) {
        this.report = report;
        this.type = type;
        this.data = data;
    }

    @Override
    public String log() {
        return "ReportCmd: " + this.type.toString();
    }

    @Override
    public void execute() {
        App app = App.get();
        app.getReports().add(this.report, this.type, this.data);
    }
    
    
}
