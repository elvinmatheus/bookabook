package br.ufc.tpii.view.pages.pagestemplate;

import javax.swing.Box;
import javax.swing.JComponent;

import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.view.components.Label;
import br.ufc.tpii.view.components.base.MenuFactory;

public class LayoutTemplate {

    /** Template para construção do layout padrão:
     * 
     * O layout padrão é basicamente a barra de menu seguida do caminho (exemplo: Pesquisa >> Usuários),
     * seguido do conteúdo (parte principal e central de uma página), seguido de um barra final (foot) com informações de Termos de Serviço por exemplo.
     */

    final static int BOTTOMMARGINMAINWRAPPER = 80;
    final static int LEFTRIGHTMARGINWRAPPER = MenuFactory.WRAPPERHORIZONTALMARGIN;


    public static JComponent pathComponent(String text) {
        /** Constrói um component para exibir o caminho (exemplo: Pesquisa >> Usuários) */
        JComponent component = Box.createHorizontalBox();
        Label label = new Label(text);
        component.add(Margin.rigidHorizontal(LEFTRIGHTMARGINWRAPPER));
        component.add(label);
        component.add(Box.createHorizontalGlue());
        return component;
    }

    public static void build(JComponent pane, JComponent menubar, JComponent content, String path) {
        /** frame é o componente ao qual será construído o template, de acordo com
         * o que foi fornecido comoo argumento (menubar, content, path).
         */
        pane.add(menubar);
        pane.add(pathComponent(path));
        pane.add(content);
        pane.add(Margin.rigidVertical(BOTTOMMARGINMAINWRAPPER));
    }
    
}
