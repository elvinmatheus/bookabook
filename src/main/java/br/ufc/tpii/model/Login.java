package br.ufc.tpii.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.DisplayPopupCmd;
import br.ufc.tpii.controller.commands.LoginCmd;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;

public class Login {

    private App app; // conexão com Model
    public Login(App app) {
        this.app = app;
    }
    
    /* Estado de login */
    private User user; // estado inicial
    private boolean isLoggedIn = false;
    private boolean isAdmin = false;
    /* Getters */
    public User getUser() { return this.user; }
    public boolean isLoggedIn() { return this.isLoggedIn; }
    public boolean isAdmin() { return this.isAdmin; }
    /* Setters */
    public void setUser(User user) {
        boolean userChanged = user != this.user;
        this.user = user;
        if (userChanged) this.app.control().invoke(new RefreshCmd(RefreshID.LoginUserChanged));
    }
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    /* Base de dados */
    private Map<Integer, User> users = new HashMap<Integer, User>();
    /** Retorna todos os usuários. */
    public Collection<User> getUsers() {
        return this.users.values();
    }
    public User getUser(int matricula) { return this.users.get(matricula); }
    private int matriculaCounter = 0;
    public int getMatricula() { return this.matriculaCounter; }
    public void incrementMatricula() {
        this.matriculaCounter += 1;
        this.app.control().invoke(new RefreshCmd(RefreshID.LoginIncrementMatricula));
    }
    /** Retorna uma coleção de usuários que satisfazem os filtros.
     * Um filtro nulo é satisfeito por qualquer usuário.
     * 
     * O filtro nameFilter só é satisfeita caso o nome do usuário contenha
     * nameFilter (ou seja, nameFilter é substring do nome do usuário).
     * 
     * O filtro matriculaFilter é definido da mesma forma.
     */
    public Collection<User> getFilteredUsers(String nameFilter, String matriculaFilter) {
        List<User> c = new ArrayList<>(this.users.values());
        c.removeIf(user -> {
            UserData data = user.getData();
            // temos que reduzir o escopo de matriculaFilter para poder atribuir null no catch
            String matrFilter = matriculaFilter;
            int matriculaFilterInt = -1;
            try {
                matriculaFilterInt = Integer.parseInt(matrFilter);
            } catch (NumberFormatException e) { matrFilter = null; }
            return !((nameFilter == null || data.getName().contains(nameFilter)) &&
                (matrFilter == null || data.getMatricula() == matriculaFilterInt));
                // adicionar matrícula depois!
        });
        return c;
    }
    /* Adiciona usuário */
    public void addUser(User user) {
        int matricula = this.getMatricula();
        user.getData().setMatricula(matricula);
        this.incrementMatricula();
        if (users.containsKey(matricula)) {
            throw new RuntimeException("Usuário já está cadastrado");
        }
        users.put(matricula, user);
        this.app.control().invoke(new RefreshCmd(RefreshID.LoginAddUser, user));
    }

    /* Funções para salvar e carregar a base de dados */
    public void loadUserDB(String filename) {
        return;
    }
    public void saveUserDB(String filename) {
        return;
    }


    /** Retorna um usuário, se a senha está correta */
    private User validate(int matricula, String password) {
        User u = this.users.getOrDefault(matricula, null);
        if (u == null) return null;
        if (!u.comparePassword(password)) return null;
        return u;
    }

    /* Tenta realizar login de usuario, retornando erro caso usuário esteja logado
     * ou caso matrícula ou senha estejam inseridas erradas */
    public void tryLogin(int matricula, String password) {
        if (this.isLoggedIn) {
            Command displayErr = new DisplayPopupCmd("Usuário já está logado");
            this.app.control().invoke(displayErr);
            return;
        }
        User u = this.validate(matricula, password);
        if (u != null) {
            Command loginCmd = new LoginCmd(u);
            this.app.control().invoke(loginCmd);
            return;
        }
        this.app.control().invoke(new DisplayPopupCmd("Matrícula e/ou senha incorretos"));
    }

}
