package dao;

import dao.generics.GenericDAO;
import domain.Produto;

public class ProdutoDAO extends GenericDAO<Produto, Long> implements IProdutoDAO {
    public ProdutoDAO() {
        super(Produto.class);
    }
}
