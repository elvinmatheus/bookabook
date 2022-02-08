

package br.ufc.tpii.controller.commands;

import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Emprestimo;

public class ChangeSettings {

    /**
     * Define comandos para alterar as configurações da aplicação.
     * Esses comandos alteram parâmetros globais, como o prazo e a multa de um empréstimo,
     * além do limite de empréstimos por usuário.
     * 
     * Como são comandos, eles devem ser invocados pelo controller via o método "invoke"
     */
    

     /** Retorna um comando que altera o prazo do empréstimo para "days" dias */
    public static Command prazoEmprestimo(int days) {
        Command cmd = new Command(){
            @Override
            public void execute() {
                Emprestimo.changeExpireLimit(days);
            }
            @Override
            public String log() {
                return "PrazoEmprestimo: " + days;
            }
        };
        return cmd;
    }

    /** Retorna um comando que altera a quantidade máxima de empréstimos
     * por usuário para "quantity"
     */
    public static Command maxQuantityEmprestimo(int quantity) {
        Command cmd = new Command(){
            @Override
            public void execute() {
                Emprestimo.changeMaxQuantity(quantity);
            }
            @Override
            public String log() {
                return "MaxQuantityEmprestimo: " + quantity;
            }
        };
        return cmd;
    }

    /** Retorna um comando que altera a multa para "amount" */
    public static Command multa(double amount) {
        Command cmd = new Command(){
            @Override
            public void execute() {
                Emprestimo.changeMulta(amount);
            }
            @Override
            public String log() {
                return "Multa: " + amount;
            }
        };
        return cmd;
    }
}
