package br.ufc.tpii.model;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;

public class User {
    /** Representa um usuário */

    private String encryptedPassword; // não armazenamos a senha, apenas sua versão criptografada
    private UserData data;

    public UserData getData() { return this.data; }

    /* Indica se a senha encriptografada passada como argumento é igual 
     * à senha encriptografada desse usuário */
    public boolean comparePassword(String password) {
        return Crypto.crypt(password).equals(this.encryptedPassword);
    }

    /* Método construtor */
    public User(UserData data, String encryptedPassword) {
        data.owner(this);
        this.data = data;
        this.encryptedPassword = encryptedPassword;
    }

    /* Verifica se o usuário possui devoluções com atraso */
    public String status() {
        for (Emprestimo e : this.data.getEmprestimos()) {
            if (e.isExpired()) {
                return "Empréstimo expirado";
            }
        }
        return "Regular";
    }

    public void update(UserData d) {
        if (d == null) throw new NullPointerException("Dados do usuário não pode ser null");
        this.data.update(d);
        App.get().control().invoke(new RefreshCmd(RefreshID.UpdateUserData, this.data));
    }

    /** Retorna o nível de privilégio desse usuário.
     * 
     * 1 - Usuário comum
     * 2 - Administrador
     */
    public final static int GUESTPRIVILEGE = 0;
    public final static int USERPRIVILEGE = 1;
    public final static int ADMINPRIVILEGE = 2;
    public int getPrivilege() {
        return User.USERPRIVILEGE;
    }

    public User copy() {
        UserData d = data.copy();
        User u = new User(d, encryptedPassword);
        return u;
    }

}