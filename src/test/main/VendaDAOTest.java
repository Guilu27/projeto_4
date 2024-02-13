package main;

import dao.*;
import domain.Cliente;
import domain.Produto;
import domain.Venda;
import exceptions.DAOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.*;

public class VendaDAOTest {
    private IVendaDAO iVendaDAO;

    private IVendaDAO iVendaExclusaoDao;

    private IClienteDAO iClienteDAO;

    private IProdutoDAO iProdutoDAO;

    private Random rd;

    private Cliente cliente;

    private Produto produto;

    public VendaDAOTest() {
        iVendaDAO = new VendaDAO();
        iVendaExclusaoDao = new VendaExclusaoDAO();
        iClienteDAO = new ClienteDAO();
        iProdutoDAO = new ProdutoDAO();
        rd = new Random();
    }

    @Before
    public void init() throws DAOException {
        cliente = cadastrarCliente();
        produto = cadastrarProduto("A1", BigDecimal.TEN);
    }

    private Cliente cadastrarCliente() throws DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Cliente 1");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        iClienteDAO.cadastrar(cliente);

        return cliente;
    }

    private Produto cadastrarProduto(String codigo, BigDecimal valor) throws DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        iProdutoDAO.cadastrar(produto);
        return produto;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }

    @After
    public void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        iClienteDAO.excluir(cliente);
    }

    private void excluirVendas() {
        Collection<Venda> list = iVendaExclusaoDao.pesquisarTodos();
        list.forEach(prod -> {
            iVendaExclusaoDao.excluir(prod);
        });
    }

    private void excluirProdutos() {
        Collection<Produto> list = iProdutoDAO.pesquisarTodos();
        list.forEach(produto -> {
            iProdutoDAO.excluir(produto);
        });
    }

    @Test
    public void pesquisar() throws DAOException {
        Venda venda = criarVenda("A1");
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        Venda vendaConsultada = iVendaDAO.pesquisar(venda.getId());
        assertNotNull(vendaConsultada);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws DAOException {
        Venda venda = criarVenda("A2");
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);

        assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));

        Venda vendaConsultada = iVendaDAO.pesquisar(venda.getId());
        assertTrue(vendaConsultada.getId() != null);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        retorno.setStatus(Venda.Status.CANCELADA);
        iVendaDAO.cancelarVenda(venda);

        Venda vendaConsultada = iVendaDAO.pesquisar(venda.getId());
        assertEquals(codigoVenda, vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(produto, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws DAOException {
        Venda venda = criarVenda("A6");
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);

        Venda venda1 = criarVenda("A6");
        Venda retorno1 = iVendaDAO.cadastrar(venda1);
        assertNull(retorno1);
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerProduto() throws DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void finalizarVenda() throws DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        iVendaDAO.finalizarVenda(venda);

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Venda retorno = iVendaDAO.cadastrar(venda);
        assertNotNull(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(Venda.Status.CONCLUIDA);
        iVendaDAO.finalizarVenda(venda);

        Venda vendaConsultada = iVendaDAO.consultarComCollection(venda.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }
}
