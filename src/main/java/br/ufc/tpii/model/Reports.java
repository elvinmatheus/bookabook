package br.ufc.tpii.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Reports {

    public enum Type {
        EMPRESTIMO("Empréstimo"),
        DEVOLUCAO("Devolução"),
        BOOK_REGISTER("Cadastro de livro"),
        USER_REGISTER("Cadastro de usuário"),
        USER_EDIT("Edição de cadastro de usuário");
    
        private String representation;
        private Type(String representation) {
            this.representation = representation;
        }
        @Override
        public String toString() {
            return this.representation;
        }
    
    }
    

    public Reports() {

    }

    private List<Relatorio<Emprestimo>> emprestimos = new ArrayList<>();
    private List<Relatorio<Emprestimo>> devolucoes = new ArrayList<>();
    private List<Relatorio<Book>> booksRegister = new ArrayList<>(); // livros cadastrados
    private List<Relatorio<User>> usersRegister = new ArrayList<>(); // usuários cadastrados
    private List<Relatorio<User>> usersEdit = new ArrayList<>(); // usuários cadastrados

    public void add(Object obj, Reports.Type type, Object... data) {
        if (type == Type.EMPRESTIMO) {
            this.addEmprestimo((Emprestimo)obj, type, data);
        } else if (type == Type.DEVOLUCAO) {
            this.addDevolucao((Emprestimo)obj, type, data);
        } else if (type == Type.BOOK_REGISTER) {
            this.addBookRegister((Book)obj, type, data);
        } else if (type == Type.USER_REGISTER) {
            this.addUserRegister((User)obj, type, data);
        } else if (type == Type.USER_EDIT) {
            this.addUserEdit((User)obj, type, data);
        }
    }

    private void addEmprestimo(Emprestimo e, Reports.Type type, Object... data) {
        this.emprestimos.add(new Relatorio<Emprestimo>(e, type, data));
    }
    private void addDevolucao(Emprestimo e, Reports.Type type, Object... data) {
        this.devolucoes.add(new Relatorio<Emprestimo>(e, type, data));
    }
    private void addBookRegister(Book e, Reports.Type type, Object... data) {
        this.booksRegister.add(new Relatorio<Book>(e, type, data));
    }
    private void addUserRegister(User e, Reports.Type type, Object... data) {
        this.usersRegister.add(new Relatorio<User>(e, type, data));
    }
    private void addUserEdit(User e, Reports.Type type, Object... data) {
        this.usersEdit.add(new Relatorio<User>(e, type, data));
    }

    public List<Relatorio<Emprestimo>> getEmprestimos() {
        return Collections.unmodifiableList(this.emprestimos);
    }
    public List<Relatorio<Emprestimo>> getDevolucoes() {
        return Collections.unmodifiableList(this.devolucoes);
    }
    public List<Relatorio<Book>> getBooksRegister() {
        return Collections.unmodifiableList(this.booksRegister);
    }
    public List<Relatorio<User>> getUsersRegister() {
        return Collections.unmodifiableList(this.usersRegister);
    }
    public List<Relatorio<User>> getUsersEdit() {
        return Collections.unmodifiableList(this.usersEdit);
    }
    
}
