package br.ufc.tpii.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JTextField;

public class FieldHandler implements ActionListener {

    /** Tem acesso a campos de texto e permite ler o conteúdo deles
     * no momento em que um evento foi acionado.
     * 
     * Exemplo de uso:
     * 
     * Obtenha uma lista de campos de texto: List<JTextField>
     * Obtenha um botão (por exemplo) que você quer que acione o evento.
     * 
     * Construa um FieldObserver com os argumentos apropriados,
     * em que handler pode ser um lambda. Por exemplo:
     * 
     * BOTAO.addActionListener(new FieldObserver(CAMPOS_DE_TEXTO, f -> {
     *      // pode executar aqui o que quiser!
     *      // os campos de texto são acessíveis por f, pois f é List<JTextField>
     *      String nome = f.get(0).getText(); // use isso para pegar o texto atual do primeiro campo de texto
     *      execute_algo(nome);
     * }));
     */

    private List<JTextField> fields;
    private Consumer<List<JTextField>> handler;
    public FieldHandler(List<JTextField> fields, Consumer<List<JTextField>> handler) {
        this.fields = fields;
        this.handler = handler;
    }
    protected void setHandler(Consumer<List<JTextField>> handler) { this.handler = handler; }

    @Override
    public void actionPerformed(ActionEvent e) {
        handler.accept(this.fields);
    }
    
}
