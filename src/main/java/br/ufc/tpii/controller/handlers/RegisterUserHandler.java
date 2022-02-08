package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.RegisterUserCmd;
import br.ufc.tpii.controller.commands.UpdateUserCmd;

public class RegisterUserHandler implements ActionListener {

    /** Define as restrições para que um cadastro de usuário seja aceito.
     * 
     * Por exemplo, impõe regras para o formato de data (dd/MM/yyyy),
     * proíbe campos vazios, faz confirmação de senha, etc.
     * @param fields - Campos de texto (componentes) em ORDEM:
     * 
     * 0 - Nome Completo
     * 1 - Data de nascimento
     * 2 - Documento
     * 3 - Endereço
     * 4 - Email
     * 5 - Contato
     * 6 - Senha
     * 7 - Confirmação de senha
     * 
     * @param isAdmin - true se o cadastro é referente a um admin
     * @param edit - true se é uma atualização de cadastro (em vez de um novo cadastro)
     */

    private List<JTextField> fields;
    private boolean isAdmin, edit;
    private int matricula;
    public RegisterUserHandler(List<JTextField> fields, boolean isAdmin) {
        this(fields, isAdmin, false, -1);
    }
    public RegisterUserHandler(List<JTextField> fields, boolean isAdmin, boolean edit, int matricula) {
        this.fields = fields;
        this.isAdmin = isAdmin;
        this.edit = edit;
        this.matricula = matricula;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        App app = App.get();
        String username = this.fields.get(0).getText();
        if (username.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo de nome é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        String birthdateStr = this.fields.get(1).getText();
        String document = this.fields.get(2).getText();
        String address = this.fields.get(3).getText();
        String email = this.fields.get(4).getText();
        if (email.length() == 0) {
            app.control().invoke(new DisplayPopupCmd("Campo de email é obrigatório", JOptionPane.ERROR_MESSAGE));
            return;
        }
        String contact = this.fields.get(5).getText();
        String password = this.fields.get(6).getText();
        if (!this.edit && password.length() < 6) {
            app.control().invoke(new DisplayPopupCmd("Senha deve ter no mínimo 6 caracteres", JOptionPane.ERROR_MESSAGE));
            return;
        }
        String confirmPassword = this.fields.get(7).getText();
        if (!this.edit && !password.equals(confirmPassword)) {
            app.control().invoke(new DisplayPopupCmd("Senhas diferentes", JOptionPane.ERROR_MESSAGE));
            return;
        }
        LocalDate birthdate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            birthdate = LocalDate.parse(birthdateStr, formatter);
        } catch (DateTimeParseException exc) {
            app.control().invoke(new DisplayPopupCmd("Data deve estar no formato dd/MM/yyyy", JOptionPane.ERROR_MESSAGE));
            return;
        }
        UserData data = new UserData(username, address, contact, email, document, birthdate);
        if (this.edit) {
            User currentAdmin = app.getLogin().getUser();
            data.setMatricula(matricula);
            app.control().invoke(new UpdateUserCmd(data, matricula, currentAdmin));
            return;
        }
        // o botão Cadastrar foi pressionado! vamos tentar cadastrar o usuário
        // mensagens de erro e confirmação são de responsabilidade do próprio comando abaixo
        app.control().invoke(new RegisterUserCmd(data, password, RegisterUserHandler.this.isAdmin));
    }

}
