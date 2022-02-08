package br.ufc.tpii.model;

public class Crypto {

    private static String SECRET = "BIGSECRET";
    

    /* Aplica criptografia ao argumento */
    public static String crypt(String x) {
        // usaremos SECRET aqui dentro como chave da criptografia
        // na verdade a senha está exposta, mas não tem base de dados
        // quando/se uma base de dados for implementada devemos mudar isso
        return x + Crypto.SECRET.hashCode();
    }
}
