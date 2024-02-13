package dao;

import dao.generics.IGenericDAO;
import domain.Venda;

public interface IVendaDAO extends IGenericDAO<Venda, Long> {

    public void finalizarVenda(Venda venda);

    public void cancelarVenda(Venda venda);

    public Venda consultarComCollection(Long id);
}
