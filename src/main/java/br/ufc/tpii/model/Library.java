package br.ufc.tpii.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.controller.commands.RefreshCmd;
import br.ufc.tpii.framework.App;

public class Library {

    private Map<String, Set<Book>> books;
    private Map<String, Book> booksByISBN;
    private App app;

    public Library(App app) {
        this.app = app;
        this.books = new HashMap<String, Set<Book>>();
        this.booksByISBN = new HashMap<String, Book>();
    }
    /* Pesquisa de livros por Título e por Nome de Autor */
    public Set<Book> findByTitle(String title) {
        return this.books.getOrDefault(title, new HashSet<Book>());
    }

    public Book findByISBN(String isbn) {
        return this.booksByISBN.get(isbn);
    }

    /* 
     * true retornado sse existe livro na biblioteca
     * com o @param isbn especificado
     */
    public boolean hasBook(String isbn) {
        return booksByISBN.containsKey(isbn);
    }

    /* 
     * true retornado sse existe livro na biblioteca
     * com os metadados do @param book especificado
     */
    public boolean hasBook(Book book) {
        if (book == null) return false;
        String isbn = book.getIsbn();
        return hasBook(isbn) && booksByISBN.get(isbn).isForAllIntentsAndPurposes(book);
    }


    /** Adiciona um livro à biblioteca e retorna false se o set não foi alterado. */
    public boolean addBook(Book book) {
        String title = book.getTitle();
        this.booksByISBN.put(book.getIsbn(), book);
        Set<Book> set = this.books.get(title);
        if (set == null) {
            set = new HashSet<>();
            this.books.put(title, set);
        }
        boolean b = set.add(book);
        // notificamos os views que estão observando a mudança de estado "LibraryAddBook"
        this.app.control().invoke(new RefreshCmd(RefreshID.LibraryAddBook));
        return b;
    }

    public void removeBook(Book book) {
        String isbn = book.getIsbn();
        this.books.get(book.getTitle()).remove(book); // tenta remover se livro está presente
        this.booksByISBN.remove(isbn);
        this.app.control().invoke(new RefreshCmd(RefreshID.LibraryRemoveBook));
    }

    /** Remove @param oldBook, insere @param newBook e retorna true
     * sse @param oldBook pertencesse a biblioteca e não existisse livro na
     * biblioteca, além de @param oldBook, com o isbn de @param newBook */
    public boolean updateBook(Book oldBook, Book newBook) {
        if (oldBook == null || newBook == null) return false;

        String oldIsbn = oldBook.getIsbn();
        String newIsbn = newBook.getIsbn();
        
        boolean b = hasBook(oldBook) && (oldBook.equals(newBook) || !hasBook(newIsbn));
        if (newBook.getHowManyTotal() < oldBook.getHowManyTotal() - oldBook.getHowManyAvailable()) {
            throw new RuntimeException("Não é possível reduzir a quantidade de exemplares se a nova quantidade não conseguir representar os livros pendentes (reservas ou empréstimos)");
        }
        
        if (b) {
            String title = oldBook.getTitle();
            Set<Book> booksWithSameTitle = this.books.get(title);
            for (Book book: booksWithSameTitle) {
                if (book.equals(oldBook)) {
                    book.update(newBook);
                    break;
                }
            }
            if (!oldBook.getTitle().equals(title)) {
                booksWithSameTitle.remove(oldBook);
                this.addBook(oldBook);
                if (booksWithSameTitle.size() == 0) {
                    this.books.remove(title);
                }
            }
            // isbn não pode ter mudado, então vamos conseguir encontrar o livro a seguir
            this.findByISBN(oldIsbn).update(newBook);
        }
        // notificamos os views que estão observando a mudança de estado "LibraryUpdateBook"
        this.app.control().invoke(new RefreshCmd(RefreshID.LibraryUpdateBook));
        return b;
    }
    /** Retorna uma coleção de livros que satisfazem os filtros.
     * Um filtro nulo é satisfeito por qualquer livro.
     * 
     * O filtro titleFilter só é satisfeito caso o nome do livro contenha
     * titleFilter (ou seja, titleFilter é substring do nome do usuário).
     * 
     * O filtro authorFilter é definido da mesma forma.
     */
    public List<Book> getFilteredBooks(String titleFilter, String authorFilter) {
        List<Book> b = new ArrayList<>();
        for (Set<Book> bookset : this.books.values()) {
            for (Book book : bookset) {
                List<String> authorNames = book.getAuthors();
                boolean satisfiesTitleFilter = titleFilter == null || book.getTitle().contains(titleFilter);
                boolean satisfiesAuthorFilter = false;
                for (String authorName: authorNames) {
                    if (authorFilter == null || authorName.contains(authorFilter)) {
                        satisfiesAuthorFilter = true;
                        break;
                    }
                }
                if (satisfiesTitleFilter && satisfiesAuthorFilter) {
                    b.addAll(bookset);
                }
            }
        }
        return b;
    }
}
