package br.ufc.tpii.view.pages.admin;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.controller.handlers.NotifyTextChangeListener;
import br.ufc.tpii.controller.handlers.SaveSettingsHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class Settings extends Page {
    
    public final static String TITLE = "Configurações";
    @Override
    public String getTitle() { return TITLE; }

    private List<JTextField> fields;
    private JButton saveButton;

    @Override
    public JComponent paint() {
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String[] labelsText = new String[] {
            "Alterar tempo de empréstimo:",
            "Alterar quantidade de empréstimos:",
            "Alterar valor da multa:"
        };
        String[] buttonsText = new String[] {"Cancelar", "Salvar"};
        SearchContentTemplate template = new SearchContentTemplate(labelsText, buttonsText, false, true);
        JComponent content = template.build();
        this.fields = template.getTextFields();
        this.saveButton = template.getButtons()[1];
        this.initFieldsListeners(this.fields);
        ActionListener cancelHandler = e -> app.control().invoke(new RefreshCmd(RefreshID.INIT));
        ActionListener saveHandler = new SaveSettingsHandler(this.fields);
        ActionListener[] handlers = new ActionListener[] {cancelHandler, saveHandler};
        template.setHandlers(handlers);
        String path = "Administração >> Configurações";
        LayoutTemplate.build(pane, menubar, content, path);
        return pane;
    }
    private boolean hasChanged(List<JTextField> fields) {
        return !(fields.get(0).getText().equals(String.valueOf(Emprestimo.getExpireLimit()))
            && fields.get(1).getText().equals(String.valueOf(Emprestimo.getMaxQuantity()))
            && fields.get(2).getText().equals(String.valueOf(Emprestimo.getMulta())));
    }

    private void initFieldsListeners(List<JTextField> fields) {
        DocumentListener listener = new NotifyTextChangeListener(x -> {
            this.saveButton.setEnabled(this.hasChanged(fields));
        });
        for (JTextField f: fields) {
            f.getDocument().addDocumentListener(listener);
        }
    }
    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (RefreshID.INIT == changeID || RefreshID.SettingsChanged == changeID) {
            this.fields.get(0).setText(String.valueOf(Emprestimo.getExpireLimit()));
            this.fields.get(1).setText(String.valueOf(Emprestimo.getMaxQuantity()));
            this.fields.get(2).setText(String.valueOf(Emprestimo.getMulta()));
            this.saveButton.setEnabled(false);
        }

        super.refresh(changeID, args);
    }

}
