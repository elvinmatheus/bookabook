package br.ufc.tpii.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;

public class UserData {
    /* Struct de dados. Não define comportamento, então todos os campos são públicos */
    private String name, address, contact, email, document;
    private LocalDate birthdate;
    private int matricula = -1; // sinaliza que o programa ainda não atribuiu matrícula a esse objeto
    private List<Book> reservedBooks = new ArrayList<>(); // livros reservados
    private List<Emprestimo> emprestimos = new ArrayList<>(); // livros emprestados
    User user; // a quem esses dados se referem

    // Método construtor
    public UserData(String name, String address, String contact, String email, String document, LocalDate birthdate) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.document = document;
        this.birthdate = birthdate;
    }

    public void owner(User user) { 
        this.user = user; 
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public LocalDate getBirthdate() { return birthdate; }
    public String getContact() { return contact; }
    public String getDocument() { return document; }
    public String getEmail() { return email; }
    public int getMatricula() { return matricula; }
    public void setMatricula(int matricula) { this.matricula = matricula; }
    
    /* Reserva livro para o usuário */
    public void reserve(Book book) {
        this.reservedBooks.add(book);
        App.get().control().invoke(new RefreshCmd(RefreshID.UserReserveBook, book)); // notifica views observadoras
    }

    /* Remove reserva de livro realizada para o usuário */
    public void removeReserved(Book book) {
        this.reservedBooks.remove(book);
        App.get().control().invoke(new RefreshCmd(RefreshID.UserUnreserveBook, book)); // notifica views observadoras
    }
    
    public List<Book> getReservedBooks() { return Collections.unmodifiableList(this.reservedBooks); }

    /* Empresta livro para o usuário */
    public void emprestar(Book book) {
        Emprestimo rent = new Emprestimo(book, this.user);
        this.emprestimos.add(rent);
        App.get().control().invoke(new RefreshCmd(RefreshID.UserEmprestar, book)); // notifica views observadoras
    }

    /* Devolução de livro emprestado pelo usuário */
    public Emprestimo devolver(Book book) {
        App app = App.get();
        Emprestimo toRemove = null;
        for (Emprestimo e : this.emprestimos) {
            if (e.getBook().equals(book)) {
                toRemove = e;
            }
        }
        if (toRemove == null) {
            app.control().invoke(new DisplayPopupCmd("Tentativa falha de devolver livro. Usuário não possui tal livro emprestado", JOptionPane.ERROR_MESSAGE));
            return toRemove;
        }
        this.emprestimos.remove(toRemove);
        app.control().invoke(new RefreshCmd(RefreshID.UserDevolver, book)); // notifica views observadoras
        return toRemove;
    }

    public List<Emprestimo> getEmprestimos() { return Collections.unmodifiableList(this.emprestimos); }

    /* Verifica se usuário possui livros emprestados */
    public boolean hasBookRented(Book book) {
        for (Emprestimo e: this.getEmprestimos()) {
            if (e.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }

    /* Verifica se usuário possui livros reservados */
    public boolean hasBookReserved(Book book) {
        return this.getReservedBooks().contains(book);
    }

    public void update(UserData d) {
        this.name = d.name;
        this.address = d.address;
        this.birthdate = d.birthdate;
        this.contact = d.contact;
        this.email = d.email;
        this.document = d.document;
    }

    public UserData copy() {
        UserData d = new UserData(name, address, contact, email, document, LocalDate.from(birthdate));
        d.setMatricula(this.getMatricula());
        d.emprestimos = List.copyOf(this.getEmprestimos());
        d.reservedBooks = List.copyOf(this.getReservedBooks());
        return d;
    }

    
}