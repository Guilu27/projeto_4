package dao;

import dao.generics.GenericDAO;
import domain.Cliente;

public class ClienteDAO extends GenericDAO<Cliente, Long> implements IClienteDAO {
    public ClienteDAO() {
        super(Cliente.class);
    }
}
