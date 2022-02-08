package br.ufc.tpii.view.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import br.ufc.tpii.controller.commands.NavigateCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.components.layout.PackLayout;
import br.ufc.tpii.view.components.layout.StretchLayout;
import br.ufc.tpii.view.pages.admin.RegisterUsers;
import br.ufc.tpii.view.pages.user.MeusEmprestimos;

public class UserResult extends View {

    /** 
     * SearchUsersResults exibe uma lista de UserResult.
     * 
     * É composto por alguns dados do usuário à esquerda e dois botões à direita.
     * Os botões são "Abrir registro" e "Editar".
     */

    final static int SPACEBETWEENBOTTOMLABELS = 20;
    final static int BOTTOMLABELSRIGHTMARGIN = 25;
    final static int SPACEBETWEENBUTTONS = 15;
    final static int BUTTONSVERTICALMARGIN = 18;
    final static int BUTTONSHORIZONTALMARGIN = 25;
    final static int INSIDEVERTICALMARGINLEFTCONTENT = 10;
    final static int INSIDEHORIZONTALMARGINLEFTCONTENT = 10;

    final static Color WRAPPERBUTTONSGRAY = new Color(209, 209, 209);
    final static Color BUTTONBGGRAY = new Color(181, 181, 181);
    final static Color BUTTONLABELCOLOR = new Color(0, 0, 0);
    final static Color LEFTBACKGROUNDCOLOR = new Color(255, 255, 255);

    /* Constantes para o POPUP */
    final static int TOPMARGIN = 40;
    final static int BOTTOMMARGIN = 40;
    final static int LEFTMARGIN = 30;
    final static int RIGHTMARGIN = 30;

    private App app;
    private User user;
    private boolean editable;
    public UserResult(App app, User user) {
        this(app, user, true);
    }
    public UserResult(App app, User user, boolean editable) {
        super(app);
        this.app = app;
        this.user = user;
        this.editable = editable;
    }
    
    private JFrame frame = null;
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    private Label name, status, rent, reserved;
    private JComponent left(User user) {
        UserData data = user.getData();
        JComponent component = PackLayout.createVerticalBox();
        this.name = new Label("Nome: " + data.getName());
        this.status = new Label("Situação: " + user.status());
        this.rent = new Label("Livros emprestados: " + data.getEmprestimos().size());
        this.reserved = new Label("Livros reservados: " + data.getReservedBooks().size());
        JComponent bottom = Box.createHorizontalBox();
        bottom.add(rent);
        bottom.add(Margin.rigidHorizontal(SPACEBETWEENBOTTOMLABELS));
        bottom.add(reserved);
        bottom.add(Margin.rigidHorizontal(BOTTOMLABELSRIGHTMARGIN));
        component.add(Margin.rigidVertical(INSIDEVERTICALMARGINLEFTCONTENT));
        component.add(Margin.glueRight(name));
        component.add(Margin.glueRight(status));
        component.add(Box.createVerticalGlue());
        component.add(bottom);
        component.add(Margin.rigidVertical(INSIDEVERTICALMARGINLEFTCONTENT));
        component.setOpaque(true);
        component.setBackground(LEFTBACKGROUNDCOLOR);
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Margin.rigidHorizontal(INSIDEHORIZONTALMARGINLEFTCONTENT));
        wrapper.add(component);
        wrapper.add(Box.createHorizontalGlue());
        wrapper.setOpaque(true);
        wrapper.setBackground(LEFTBACKGROUNDCOLOR);
        return wrapper;
    }

    private JComponent right(App app, User user) {
        JComponent component = StretchLayout.createVerticalBox();
        ActionListener viewHandler = e -> {
            this.popupUserData(this.frame != null ? this.frame : app.getFrame(), user);
        };
        Button view = new Button("Abrir registro", viewHandler, BUTTONLABELCOLOR, BUTTONBGGRAY);
        component.add(Margin.rigidVertical(BUTTONSVERTICALMARGIN));
        component.add(view);
        if (this.editable) {
            ActionListener editHandler = e -> {
                app.control().invoke(new NavigateCmd(new RegisterUsers(user)));
            };
            Button edit = new Button("Editar", editHandler, BUTTONLABELCOLOR, BUTTONBGGRAY);
            component.add(Margin.rigidVertical(SPACEBETWEENBUTTONS));
            component.add(edit);
        }
        component.add(Margin.rigidVertical(BUTTONSVERTICALMARGIN));
        component.setOpaque(true);
        component.setBackground(WRAPPERBUTTONSGRAY);
        JComponent wrapper = Margin.horizontal(component, BUTTONSHORIZONTALMARGIN);
        wrapper.setOpaque(true);
        wrapper.setBackground(WRAPPERBUTTONSGRAY);
        return wrapper;
    }

    public void popupUserData(App app, User user) {
        this.popupUserData(app.getFrame(), user);
    }
    public void popupUserData(JFrame frame, User user) {
        if (frame == null) frame = app.getFrame();
        UserData data = user.getData();
        JComponent component = Box.createVerticalBox();
        Label name = new Label("Nome: " + data.getName());
        Label matricula = new Label("Matrícula: " + data.getMatricula());
        Label birth = new Label("Data de nascimento: " + data.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Label address = new Label("Endereço: "+ data.getAddress());
        Label email = new Label("Email: " + data.getEmail());
        Label contact = new Label("Contato: " + data.getContact());
        Label status = new Label("Situação: " + user.status());
        Label pending = new Label("Empréstimos pendentes:");
        component.add(Margin.rigidVertical(TOPMARGIN));
        component.add(Margin.glueRight(name));
        component.add(Margin.glueRight(matricula));
        component.add(Margin.glueRight(birth));
        component.add(Margin.glueRight(address));
        component.add(Margin.glueRight(email));
        component.add(Margin.glueRight(contact));
        component.add(Margin.rigidVertical(3));
        component.add(Margin.glueRight(status));
        component.add(Margin.rigidVertical(3));
        component.add(Margin.glueRight(pending));
        component.add(MeusEmprestimos.buildList(app, user, this.frame));
        component.add(Margin.rigidVertical(BOTTOMMARGIN));
        JComponent wrapper = Box.createHorizontalBox();
        wrapper.add(Margin.rigidHorizontal(LEFTMARGIN));
        wrapper.add(component);
        wrapper.add(Margin.rigidHorizontal(RIGHTMARGIN));
        JDialog dialog = new JDialog(frame);
        dialog.add(wrapper);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    @Override
    public JComponent paint() {
        JComponent component = PackLayout.createHorizontalBox();
        JComponent left = this.left(this.user);
        JComponent right = this.right(this.app, this.user);
        component.add(left);
        component.add(right);
        return component;
    }


}
