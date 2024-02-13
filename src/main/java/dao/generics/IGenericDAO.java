package dao.generics;

import domain.Persistente;
import exceptions.DAOException;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T extends Persistente, E extends Serializable> {
    T cadastrar(T entity) throws DAOException;

    T pesquisar(E valor);

    void excluir(T produto);

    void alterar(T produtoCadastrado);

    List<T> pesquisarTodos();
}
