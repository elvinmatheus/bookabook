package br.ufc.tpii.view.pages.admin;

import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.handlers.ClearHandler;
import br.ufc.tpii.controller.handlers.NotifyTextChangeListener;
import br.ufc.tpii.controller.handlers.RegisterUserHandler;
import br.ufc.tpii.framework.Page;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.AdminMenu;
import br.ufc.tpii.view.pages.pagestemplate.SearchContentTemplate;
import br.ufc.tpii.view.pages.pagestemplate.LayoutTemplate;

public class RegisterUsers extends Page {
    
    public final static String TITLE = "Cadastro de Usuários";
    @Override
    public String getTitle() { return TITLE; }

    private List<JTextField> fields;
    private JButton registerButton;
    private User user;
    public RegisterUsers() {
        this(null);
    }
    public RegisterUsers(User user) {
        this.user = user;
    }

    @Override
    public JComponent paint() {
        boolean edit = this.user != null;
        JComponent pane = Box.createVerticalBox();
        JComponent menubar = AdminMenu.withWrapper(app);
        String[] labelsText = new String[] {
            "Nome Completo:", "Data de Nascimento:", "Documento:", "Endereço:", "E-mail:",
            "Contato:", "Senha:", "Confirmar senha:"
        };
        String[] buttonsText = new String[] {"Cancelar", edit ? "Atualizar" : "Cadastrar"};
        SearchContentTemplate template = new SearchContentTemplate(labelsText, buttonsText, null, false);
        JComponent content = template.build();
        this.fields = template.getTextFields();
        // O observer a seguir irá ter acesso a todos os campos
        int matricula = edit ? this.user.getData().getMatricula() : -1;
        ActionListener registerHandler = new RegisterUserHandler(fields, false, edit, matricula);
        ActionListener cancelHandler = edit ? (e -> this.refresh(RefreshID.CLEAR)) : new ClearHandler<JTextField>(fields);
        ActionListener[] handlers = new ActionListener[] {cancelHandler, registerHandler};
        template.setHandlers(handlers);
        String path = edit ?  "Pesquisa >> Usuários >> Resultado >> Editar" : "Circulação >> Cadastro de Usuários";
        LayoutTemplate.build(pane, menubar, content, path);
        this.registerButton = template.getButtons()[1];
        for (int i = 0; i < this.fields.size(); i++) {
            this.fields.get(i).getDocument().addDocumentListener(new NotifyTextChangeListener(x -> {
                this.refresh(RefreshID.CUSTOM1); // CUSTOM1 significa que algum texto foi editado
            }));
        }
        this.refresh(RefreshID.CLEAR);
        return pane;
    }

    public boolean someFieldIsDifferent(UserData data) {
        String name = data.getName();
        String birth = data.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String document = data.getDocument();
        String address = data.getAddress();
        String email = data.getEmail();
        String contact = data.getContact();
        String[] texts = new String[] { name, birth, document, address, email, contact };
        for (int i = 0; i < texts.length; i++) {
            if (!this.fields.get(i).getText().strip().equals(texts[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void refresh(RefreshID changeID, Object... args) {
        if (this.user != null) {
            if (RefreshID.CLEAR == changeID || RefreshID.UpdateUserData == changeID) {
                UserData data = this.user.getData();
                String name = data.getName();
                String birth = data.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String document = data.getDocument();
                String address = data.getAddress();
                String email = data.getEmail();
                String contact = data.getContact();
                this.fields.get(0).setText(name);
                this.fields.get(1).setText(birth);
                this.fields.get(2).setText(document);
                this.fields.get(3).setText(address);
                this.fields.get(4).setText(email);
                this.fields.get(5).setText(contact);
                this.fields.get(6).setEnabled(false); // senha
                this.fields.get(7).setEnabled(false); // confirmar senha
            }
            if (RefreshID.CUSTOM1 == changeID) {
                UserData data = this.user.getData();
                // CUSTOM1 significa que algum texto foi editado
                this.registerButton.setEnabled(this.someFieldIsDifferent(data));
            }
        }
        
        super.refresh(changeID, args);
    }

}
