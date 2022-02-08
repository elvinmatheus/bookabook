package br.ufc.tpii.view.components;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.Border;

import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.View;
import br.ufc.tpii.helpers.Margin;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Emprestimo;
import br.ufc.tpii.model.Relatorio;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;
import br.ufc.tpii.view.components.layout.PackLayout;

public class ReportList extends View {

    private final static Color BGCOLOR = new Color(220, 220, 220);
    private final static Color TITLECOLOR = new Color(10, 10, 10);
    private final static Color DESCRIPTIONCOLOR = TITLECOLOR;
    private final static Color ITEMBORDERCOLOR = new Color(10, 10, 10);

    final static int VPADDING = 8;
    final static int HPADDING = 4;
    final static Border ITEMBORDER = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ITEMBORDERCOLOR, 1), BorderFactory.createEmptyBorder(VPADDING, HPADDING, VPADDING, HPADDING));
    
    final static int SPACEBETWEENITEMS = 20;
    final static int SPACEBETWEENLABELANDREGISTER = 5;
    final static Dimension minPopupDimension = new Dimension(400, 200);
    final static int windowPadding = 30;
    final static int RIGHTITEMPADDING = 100;
    
    
    
    private Reports reports;
    private Reports.Type type;
    private Dimension prefScrollDimension = new Dimension(600, 300);
    private LocalDate from, to;

    public ReportList(App app, Reports.Type type, LocalDate from, LocalDate to) {
        this(app, type, from, to, null);
    }
    public ReportList(App app, Reports.Type type, LocalDate from, LocalDate to, Dimension prefDim) {
        super(app);
        this.reports = app.getReports();
        this.type = type;
        if (prefDim != null) this.prefScrollDimension = prefDim;
        this.from = from != null ? from : LocalDate.MIN;
        this.to = to != null ? to : LocalDate.MAX;
    }

    private ActionListener popupUserEdit(User registrador, User userBefore, User userAfter) {
        JFrame frame = new JFrame();
        String label1 = "Registro do admin que editou o cadastro do usuário: ";
        String label2 = "Registro do usuário antes da edição: ";
        String label3 = "Registro do usuário depois da edição: ";
        String[] labels = new String[] {label1, label2, label3};
        
        UserResult uItem = null;
        if (registrador != null) {
            uItem = new UserResult(this.model, registrador, false);
            uItem.setFrame(frame);
        }

        UserResult uItem2 = new UserResult(this.model, userBefore, false);
        uItem2.setFrame(frame);

        UserResult uItem3 = new UserResult(this.model, userAfter, false);
        uItem3.setFrame(frame);

        JComponent[] views = new JComponent[] {uItem == null ? null : uItem.paint(), uItem2.paint(), uItem3.paint()};
    
        return this.popup(frame, labels, views);
    }

    private ActionListener popupUserRegister(User registrador, User newUser) {
        JFrame frame = new JFrame();
        String label1 = "Registro do admin que cadastrou o usuário: ";
        String label2 = "Registro do usuário cadastrado: ";
        String[] labels = new String[] {label1, label2};
        
        UserResult uItem = null;
        if (registrador != null) {
            uItem = new UserResult(this.model, registrador, false);
            uItem.setFrame(frame);
        }

        UserResult uItem2 = new UserResult(this.model, newUser, false);
        uItem2.setFrame(frame);

        JComponent[] views = new JComponent[] {uItem == null ? null : uItem.paint(), uItem2.paint()};
    
        return this.popup(frame, labels, views);
    }
    private ActionListener popupBookRegister(Book book, User user) {
        JFrame frame = new JFrame();
        String label1 = "Registro do admin cadastrou o livro: ";
        String label2 = "Registro do livro: ";
        String[] labels = new String[] {label1, label2};

        UserResult uItem = new UserResult(this.model, user, false);
        uItem.setFrame(frame);

        EmprestimoItem eItem = new EmprestimoItem(this.model, book, user);
        eItem.setFrame(frame);
        eItem.setShouldBeDeletable(false);
    
        JComponent[] views = new JComponent[] {uItem.paint(), eItem.paint()};
    
        return this.popup(frame, labels, views);
    }
    private ActionListener popupEmprestimo(Emprestimo emprestimo) {
        User user = emprestimo.getUser();
        JFrame frame = new JFrame();
        String label1 = "Registro do usuário associado a este empréstimo: ";
        String label2 = "Registro do empréstimo: ";
        String[] labels = new String[] {label1, label2};
    
        UserResult uItem = new UserResult(this.model, user, false);
        uItem.setFrame(frame);

        boolean fresh = false;
        EmprestimoItem eItem = new EmprestimoItem(this.model, emprestimo, fresh);
        eItem.setFrame(frame);

        JComponent[] views = new JComponent[] {uItem.paint(), eItem.paint()};
    
        return this.popup(frame, labels, views);
    }
    private ActionListener popup(JFrame frame, String[] labels, JComponent[] views) {
        return e -> {
            JComponent results = Box.createVerticalBox();
            results.add(Box.createVerticalGlue());
            for (int i = 0; i < labels.length; i++) {
                Label label = new Label(labels[i]);
                JComponent view = views[i];
                if (label == null || view == null) continue;
                results.add(Margin.glueRight(label));
                results.add(Margin.rigidVertical(SPACEBETWEENLABELANDREGISTER));
                results.add(view);
                results.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            }
            results.add(Box.createVerticalGlue());
            results.setMinimumSize(minPopupDimension);
        
            frame.add(Margin.vertical(Margin.horizontal(results, windowPadding), windowPadding));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        };
    }



    private <T> List<Relatorio<T>> filteredCopy(List<Relatorio<T>> list) {
        List<Relatorio<T>> l = new ArrayList<>();
        for (Relatorio<T> r : list) {
            LocalDate date = r.getDate().toLocalDate();
            if (date.compareTo(this.from) >= 0 && date.compareTo(this.to) <= 0) {
                l.add(r);
            }
        }
        return l;
    }

    @Override
    public JComponent paint() {
        JComponent component = Box.createVerticalBox();
        boolean isEmprestimo = this.type == Reports.Type.EMPRESTIMO;
        boolean isBookRegister = this.type == Reports.Type.BOOK_REGISTER;
        boolean isDevolucao = this.type == Reports.Type.DEVOLUCAO;
        if (isEmprestimo || isDevolucao) {
            List<Relatorio<Emprestimo>> list = this.filteredCopy(isEmprestimo ? this.reports.getEmprestimos() : this.reports.getDevolucoes());
           
            if (list.size() == 0) {
                String msg = isEmprestimo ? "Não há empréstimos registrados" : "Não há devoluções registradas";
                Label emptyLabel = new Label(msg);
                component.add(emptyLabel);
                return component;
            }
            for (Relatorio<Emprestimo> r : list) {
                Emprestimo emprestimo = r.getReport();
                String username = isEmprestimo ? "Empréstimo realizado por: " : "Devolução feita por: ";
                String bookTitle = isEmprestimo ? "Livro emprestado: " : "Livro devolvido: ";
                username += r.getReport().getUser().getData().getName();
                bookTitle += r.getReport().getBook().getTitle();
                component.add(this.reportItem(username, bookTitle, r.getDate(), this.popupEmprestimo(emprestimo)));
                component.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            }
        } else if (isBookRegister) {
            List<Relatorio<Book>> list = this.filteredCopy(this.reports.getBooksRegister());
            if (list.size() == 0) {
                String msg = "Não há cadastro de livro registrado";
                Label emptyLabel = new Label(msg);
                component.add(emptyLabel);
                return component;
            }
            for (Relatorio<Book> r : list) {
                Book book = r.getReport();
                User user = (User)(r.getData())[0];
                String username = "Registro do admin que cadastrou o livro: " + user.getData().getName();
                String bookTitle = "Registro do livro cadastrado: " + book.getTitle();
                component.add(this.reportItem(username, bookTitle, r.getDate(), this.popupBookRegister(book, user)));
                component.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            }
        } else if (this.type == Reports.Type.USER_REGISTER || this.type == Reports.Type.USER_EDIT) {
            boolean isEdit = this.type == Reports.Type.USER_EDIT;
            List<Relatorio<User>> list = this.filteredCopy(isEdit ? this.reports.getUsersEdit() : this.reports.getUsersRegister());
            if (list.size() == 0) {
                String msg = isEdit ? "Não há registro de edição de cadastro de usuário" : "Não há registro de cadastro de usuário";
                Label emptyLabel = new Label(msg);
                component.add(emptyLabel);
                return component;
            }
            for (Relatorio<User> r : list) {
                User userAfter = r.getReport(); // novo usuário cadastrado (ou com cadastro editado)
                User registrador = (User)r.getData()[0]; // admin que cadastrou ou editou o cadastro do usuário
                User userBefore = null;
                if (isEdit) {
                    userBefore = (User)r.getData()[1];
                }
                String newUserName = isEdit ? "Registro do usuário que teve cadastro editado: " : "Registro do novo usuário cadastrado: ";
                String adminUserName = isEdit ? "Registro do admin que fez a edição de cadastro: " : "Registro do admin que fez o cadastro: ";
                newUserName += userAfter.getData().getName();
                adminUserName += registrador == null ? "Usuário pré-cadastrado" : registrador.getData().getName();
                ActionListener handler = isEdit ? this.popupUserEdit(registrador, userBefore, userAfter) : this.popupUserRegister(registrador, userAfter);
                component.add(this.reportItem(adminUserName, newUserName, r.getDate(), handler));
                component.add(Margin.rigidVertical(SPACEBETWEENITEMS));
            }
        }
        ScrollPane scrollPane = new ScrollPane(component);
        scrollPane.setPreferredSize(this.prefScrollDimension);
        return scrollPane;
    }

    private JComponent reportItem(String username, String bookTitle, LocalDateTime date, ActionListener openPopup) {
        JComponent content = Box.createVerticalBox();
        Label userLabel = new Label(username, TITLECOLOR);
        Label bookLabel = new Label(bookTitle, DESCRIPTIONCOLOR);
        Label dateLabel = new Label("Data: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        content.add(Margin.glueRight(Margin.horizontalRight(userLabel, RIGHTITEMPADDING)));
        content.add(Margin.glueRight(Margin.horizontalRight(bookLabel, RIGHTITEMPADDING)));
        content.add(Margin.glueRight(Margin.horizontalRight(dateLabel, RIGHTITEMPADDING)));
        content.setOpaque(true);
        content.setBackground(BGCOLOR);
        content.setBorder(ITEMBORDER);
        JComponent wrapper = PackLayout.createHorizontalBox();
        JComponent infoButton = new InfoButton(openPopup);
        infoButton.setMinimumSize(infoButton.getPreferredSize());
        wrapper.add(content);
        wrapper.add(Margin.glueVertical(infoButton));
        wrapper.setBackground(InfoButton.DEFAULTBGCOLOR);
        wrapper.setOpaque(true);
        return wrapper;
    }
}
