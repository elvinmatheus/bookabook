package br.ufc.tpii.framework;

import br.ufc.tpii.helpers.Logger;

public class Controller {

    private Logger logger;
    public Controller(Logger logger) {
        this.logger = logger;
    }

    /* Design Pattern: Invoker | https://pt.wikipedia.org/wiki/Command */
    /* Invoca comandos */
    public void invoke(Command cmd) {
        // os comandos são definidos em controller/commands
        cmd.execute();
        this.logger.log(cmd);
        // try {
        //     cmd.execute();
        // } catch (Exception e) {
        //     // a aplicação não vai crashar em caso de erro inesperado
        //     // vai exibir um alerta de erro inesperado para o usuário
        //     if (e != null) {
        //         invoke(new DisplayPopupCmd("Erro inesperado: " + e.getMessage(), JOptionPane.ERROR_MESSAGE));
        //     }
        // }
    }
}
