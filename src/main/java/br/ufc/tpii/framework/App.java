package br.ufc.tpii.framework;

import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RegisterBookCmd;
import br.ufc.tpii.controller.commands.RegisterUserCmd;
import br.ufc.tpii.controller.commands.ReserveBookCmd;
import br.ufc.tpii.helpers.Logger;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.Crypto;
import br.ufc.tpii.model.Library;
import br.ufc.tpii.model.Login;
import br.ufc.tpii.model.Reports;
import br.ufc.tpii.model.User;
import br.ufc.tpii.model.UserData;
import br.ufc.tpii.view.pages.Home;

/** Model */
public class App {
    /* Design Pattern: Singleton */
    public final static App instance = new App();
    private App() {}
    public static App get() {
        return App.instance;
    }
    /* Permite acessar o estado da aplicação */
    /* Estado de login */
    private final Login login = new Login(this);
    public Login getLogin() { return this.login; }
    /* Estado da VIEW */
    private GUI gui;
    public void navigate(Page page) { this.gui.navigate(page); }
    public Page getCurrentPage() { return this.gui.getCurrentPage(); }
    public JFrame getFrame() { return this.gui.getFrame(); }
    /* Estado da biblioteca */
    private final Library library = new Library(this);
    public Library getLibrary() { return this.library; }

    /* Conexão do Model (App) com o Controller */
    private final Controller controller = new Controller(new Logger(true));
    public Controller control() { return this.controller; }

    private boolean ignorePopup = false;
    public boolean shouldIgnorePopup() { return this.ignorePopup; }
    public void shouldIgnorePopup(boolean should) { this.ignorePopup = should; }


    /** Informações de exibição controladas pelo modelo.
     * 
     * Ao atualizar o estado do modelo, as views que estão observando esse estado
     * serão notificadas. Uma string de identificação (como RefreshID.UserContext) é passada
     * como argumento para que as views possam saber que tipo de alteração de estado ocorreu.
     * 
     * Os atributos userContext, bookContext, userListContext e bookListContext
     * são de propósito geral, e sua semântica depende de cada página.
     * 
     * Se uma página precisa de mais atributos (por exemplo, a página exibe mais de 1 lista de usuários),
     * essas informações adicionais podem ser passadas como argumento, pois o segundo argumento é Object ...args
     * 
     */
    private User userContext; // usuário do qual se está exibindo informação atualmente
    public User getUserContext() { return this.userContext; }
    public void setUserContext(User user, Object... args) {
        this.userContext = user;
        this.getCurrentPage().refresh(RefreshID.UserContext, args);
    }
    
    private Book bookContext; // livro do qual se está exibindo informação atualmente
    public Book getBookContext() { return this.bookContext; }
    public void setBookContext(Book book, Object... args) {
        this.bookContext = book;
        this.getCurrentPage().refresh(RefreshID.BookContext, args);
    }

    private List<Book> bookListContext; // lista de livros que está sendo exibida na tela
    public List<Book> getBookListContext() { return this.bookListContext; }
    public void setBookListContext(List<Book> books, Object... args) {
        this.bookListContext = books;
        this.getCurrentPage().refresh(RefreshID.BookListContext, args);
    }

    private List<User> userListContext; // lista de usuários que está sendo exibida na tela
    public List<User> getUserListContext() { return this.userListContext; }
    public void setUserListContext(List<User> users, Object... args) {
        this.userListContext = users;
        this.getCurrentPage().refresh(RefreshID.UserListContext, args);
    }

    /* Relatórios */
    private Reports reports = new Reports();
    public Reports getReports() { return this.reports; }

    /* Ponto de partida da Interface Gráfica */
    public static void main(String args[]) {
        App app = App.get(); // Criação do Model
        GUI gui = new GUI(app); // conexão do Model (App) com View
        app.gui = gui;
        app.gui.navigate(new Home()); // página inicial

        // Enquanto povoamos com coisas falsas, desativo os popups
        app.shouldIgnorePopup(true);

        // Cadastramos algumas coisas falsas para fins de teste
        // USU�?RIO FALSO
        UserData data = new UserData("Nome mockup", "Endereço mockup", "(88) 99999-9999", "example@gmail.com", "Documento mockup", LocalDate.of(1999, 12, 31));
        String passwordMockup = "aaaaaa";
        String encrypted = Crypto.crypt(passwordMockup);
        User fakeNormalUser = new User(data, encrypted);
        app.control().invoke(new RegisterUserCmd(data, passwordMockup, false));
        // ADMIN FALSO
        UserData d = new UserData("Admin mockup", "Endereço mockup", "(88) 99992-9999", "admin@gmail.com", "Documento mockup", LocalDate.of(2000, 12, 31));
        String passwordMockup1 = "bbbbbb";
        User fakeAdmin = new User(d, Crypto.crypt(passwordMockup1));
        app.control().invoke(new RegisterUserCmd(d, passwordMockup1, true));
        // LIVRO 1
        List<String> authors1 = new ArrayList<>();
        authors1.add("ASCENCIO, Ana Fernanda Gomes");
        authors1.add("CAMPOS, Edilene Aparecida Veneruchi de");
        String title1 = "Fundamentos da programação de computadores: algoritmos, Pascal, C/C++ e java";
        String local1 = "São Paulo: Pearson Education do Brasil";
        Book book1 = new Book(title1, "", "3. ed.", "9788564574168", local1, authors1, 2012, 70);
        app.control().invoke(new RegisterBookCmd(book1, fakeAdmin));
        app.control().invoke(new ReserveBookCmd(book1, fakeNormalUser));
        // LIVRO 2
        List<String> authors2 = new ArrayList<>();
        authors2.add("CELES, Waldemar");
        authors2.add("CERQUEIRA, Renato");
        authors2.add("RANGEL, José Lucas");
        String title2 = "Introdução a estruturas de dados: com técnicas de programação em C";
        String local2 = "Rio de Janeiro, RJ: Elsevier: Campus";
        Book book2 = new Book(title2, "", "14. ed.", "8535212280", local2, authors2, 2004, 22);
        app.control().invoke(new RegisterBookCmd(book2, fakeAdmin));
        app.control().invoke(new ReserveBookCmd(book2, fakeNormalUser));
        // LIVRO 3
        List<String> authors3 = new ArrayList<>();
        authors3.add("MANZANO, José Augusto N. G.");
        authors3.add("OLIVEIRA, Jayr Figueiredo de");
        authors3.add("RANGEL, José Lucas");
        String title3 = "Algoritmos: lógica para desenvolvimento de programação de computadores";
        String local3 = "São Paulo, SP: Érica";
        Book book3 = new Book(title3, "", "27. ed. rev.", "9788536502212", local3, authors3, 2014, 50);
        app.control().invoke(new RegisterBookCmd(book3, fakeAdmin));
        app.control().invoke(new ReserveBookCmd(book3, fakeNormalUser));
        // LIVRO 4
        List<String> authors4 = new ArrayList<>();
        authors4.add("DEITEL, H. M.");
        authors4.add("DEITEL, P. J.");
        String title4 = "Java: Como programar";
        String local4 = "Pearson Prentice Hall";
        Book book4 = new Book(title4, "", "8a ed.", "9788546502212", local4, authors4, 2010, 100);
        app.control().invoke(new RegisterBookCmd(book4, fakeAdmin));
        // LIVRO 5
        List<String> authors5 = new ArrayList<>();
        authors5.add("LETHBRIDGE, Timothy C.");
        authors5.add("LAGANIERE, Robert");
        String title5 = "Object-Oriented Software Engineering: Practical Software Development Using UML and Java";
        String local5 = "McGraw-Hill Publishing Company";
        Book book5 = new Book(title5, "", "2 ed.", "978-0077109080", local5, authors5, 2004, 10);
        app.control().invoke(new RegisterBookCmd(book5, fakeAdmin));
        // LIVRO 6
        List<String> authors6 = new ArrayList<>();
        authors6.add("MEYER, Bertrand");
        String title6 = "Object-oriented software construction";
        String local6 = "Upper Saddle River, N. J.: Prentice Hall PTR";
        Book book6 = new Book(title6, "", "2nd ed.", "0136291554", local6, authors6, 1997, 30);
        app.control().invoke(new RegisterBookCmd(book6, fakeAdmin));

        // Já povoamos! Reativo os popups
        app.shouldIgnorePopup(false);

        app.getFrame().setSize(new Dimension(1200, 700));
        app.getFrame().revalidate();
    }

    
}
