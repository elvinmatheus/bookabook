package br.ufc.tpii.view.pages;

import javax.swing.Box;
import javax.swing.JComponent;

import br.ufc.tpii.framework.Page;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.view.components.Label;

public class Help extends Page {
	public final static String TITLE = "Ajuda";
	@Override
    public String getTitle() { return TITLE; }

    public static JComponent mainContent() {
        JComponent component = Box.createVerticalBox();
        component.add(Margin.rigidVertical(20));
        component.add(Margin.rigidHorizontal(20));
        Label mainText = new Label("<html><h1>Ajuda</h1></div></html>");
        component.add(mainText);
        Label question1 = new Label("<html><p>Pergunta 1: Como fazer o seu cadastro?<br/>Resposta: Separe as informações necessárias e entregue-as para o funcionário da biblioteca <br/>que fará o seu cadastro.</p><br/></html>");
        component.add(question1);
        Label question2 = new Label("<html><p>Pergunta 2: Como emprestar um livro?<br/>Resposta: Escolha um livro, entregue-o para o funcionário da biblioteca e informe o seu número de matrícula.</p><br/></html>");
        component.add(question2);
        Label question3 = new Label("<html><p>Pergunta 3: Como devolver um livro?<br/>Resposta: Entregue o livro no prazo estipulado para o funcionário da biblioteca que realizará a devolução.</p><br/></html>");
        component.add(question3);
        component.add(Margin.rigidVertical(20));
        component.add(Margin.rigidVertical(10));
        component.add(Margin.rigidVertical(10));
        component.add(Box.createVerticalGlue());

        return component;
    }

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent component = Box.createVerticalBox();
        component.add(Home.header(app, pane, false, ""));
        JComponent wrap1 = Box.createHorizontalBox();
        wrap1.add(Margin.rigidHorizontal(70));
        wrap1.add(Help.mainContent());
        wrap1.add(Box.createHorizontalGlue());
        component.add(wrap1);
        pane.add(component);
        return pane;
    }
}
