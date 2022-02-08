package br.ufc.tpii.controller.commands;

import java.util.List;

import br.ufc.tpii.controller.RefreshID;
import br.ufc.tpii.framework.App;
import br.ufc.tpii.framework.Command;
import br.ufc.tpii.model.Book;
import br.ufc.tpii.model.User;

public class RefreshCmd implements Command {

    /** Chama refresh nas views observadoras.
     * Mais precisamente, chama refresh na view principal (raiz),
     * que por sua vez (segundo o padrão Composite) chama refresh as subviews
     */
    
    RefreshID changeID; // identificador da alteração de estado do modelo
    Object[] args; // argumentos adicionais, semântica variável
    public RefreshCmd(RefreshID changeID, Object ...args) {
        this.changeID = changeID;
        this.args = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute() {
        App app = App.get();
        switch (this.changeID) {
            // alguns refresh são alterações de contexto
            // por exemplo podemos atualizar o usuário que está sendo exibido no momento
            // ou o livro que está sendo exibido no momento
            // o refresh típico é lidado no default desse switch
            case UserContext:
                app.setUserContext((User)args[0]);
                break;
            case BookContext:
                app.setBookContext((Book)args[0]);
                break;
            case UserListContext:
                app.setUserListContext((List<User>)args[0]);
                break;
            case BookListContext:
                app.setBookListContext((List<Book>)args[0]);
                break;
            case CLEAR:
                app.setUserContext(null);
                app.setBookContext(null);
                app.setUserListContext(null);
                app.setBookListContext(null);
                break;
            default:
                // notifica a View mais externa,
                // que por sua vez notificará seus filhos
                app.getCurrentPage().refresh(this.changeID, this.args);
        }
    }

    @Override
    public String log() {
        return "RefreshCmd: " + this.changeID;
    }
    
}
