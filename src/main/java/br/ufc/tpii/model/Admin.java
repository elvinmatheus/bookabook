package br.ufc.tpii.model;

public class Admin extends User {

    public Admin(UserData data, String encryptedPassword) {
        super(data, encryptedPassword);
    }

    @Override
    public int getPrivilege() {
        return User.ADMINPRIVILEGE;
    }
    
    
}
