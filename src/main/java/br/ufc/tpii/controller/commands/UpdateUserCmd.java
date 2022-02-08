package br.ufc.tpii.controller.commands;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;

public class UpdateUserCmd implements Command {

    /** Atualiza informações de um usuário */
    
    private User registrador;
    private UserData data;
    private int matricula;
    public UpdateUserCmd(UserData data, int matricula, User registrador) {
        this.data = data;
        this.matricula = matricula;
        this.registrador = registrador;
    }

    @Override
    public String log() {
        return "UpdateUserCmd: " + this.data.getName();
    }

    @Override
    public void execute() {
        App app = App.get();
        User u = app.getLogin().getUser(matricula);
        User uBefore = u.copy();
        User r = this.registrador.copy();
        u.update(this.data);
        User userAfter = u.copy();
        app.control().invoke(new ReportCmd<>(userAfter, Reports.Type.USER_EDIT, r, uBefore));
    }
    
}
