package main;

import dao.IProdutoDAO;
import dao.ProdutoDAO;
import domain.Produto;
import exceptions.DAOException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class ProdutoDAOTest {
    private IProdutoDAO iProdutoDAO;

    public ProdutoDAOTest() {
        iProdutoDAO = new ProdutoDAO();
    }

    @Test
    public void cadastrarProduto() throws DAOException {
        Produto produto = criarProduto("A1");

        Produto produtoCadastrado = iProdutoDAO.cadastrar(produto);
        assertNotNull(produtoCadastrado);
        assertNotNull(produtoCadastrado.getId());

        Produto produtoPesquisado = iProdutoDAO.pesquisar(produto.getId());
        assertNotNull(produtoPesquisado);
        assertEquals(produto.getCodigo(), produtoPesquisado.getCodigo());

        iProdutoDAO.excluir(produtoCadastrado);
        Produto produtoExcluido = iProdutoDAO.pesquisar(produtoCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void pesquisarProduto() throws DAOException {
        Produto produto = criarProduto("A2");

        Produto produtoCadastrado = iProdutoDAO.cadastrar(produto);
        assertNotNull(produtoCadastrado);
        assertNotNull(produtoCadastrado.getId());

        Produto produtoPesquisado = iProdutoDAO.pesquisar(produto.getId());
        assertNotNull(produtoPesquisado);
        assertEquals(produto.getCodigo(), produtoPesquisado.getCodigo());

        iProdutoDAO.excluir(produtoCadastrado);
        Produto produtoExcluido = iProdutoDAO.pesquisar(produtoCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void excluirProduto() throws DAOException {
        Produto produto = criarProduto("A3");

        Produto produtoCadastrado = iProdutoDAO.cadastrar(produto);
        assertNotNull(produtoCadastrado);
        assertNotNull(produtoCadastrado.getId());

        Produto produtoPesquisado = iProdutoDAO.pesquisar(produto.getId());
        assertNotNull(produtoPesquisado);
        assertEquals(produto.getCodigo(), produtoPesquisado.getCodigo());

        iProdutoDAO.excluir(produtoCadastrado);
        Produto produtoExcluido = iProdutoDAO.pesquisar(produtoCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void alterarProduto() throws DAOException {
        Produto produto = criarProduto("A4");

        Produto produtoCadastrado = iProdutoDAO.cadastrar(produto);
        assertNotNull(produtoCadastrado);
        assertNotNull(produtoCadastrado.getId());

        Produto produtoPesquisado = iProdutoDAO.pesquisar(produto.getId());
        assertNotNull(produtoPesquisado);
        assertEquals(produto.getCodigo(), produtoPesquisado.getCodigo());

        produtoCadastrado.setNome("Produto Quatro");
        iProdutoDAO.alterar(produtoCadastrado);
        Produto produtoAlterado = iProdutoDAO.pesquisar(produtoCadastrado.getId());
        assertNotNull(produtoAlterado);
        assertEquals(produtoCadastrado.getNome(), produtoAlterado.getNome());

        iProdutoDAO.excluir(produtoCadastrado);
        Produto produtoExcluido = iProdutoDAO.pesquisar(produtoCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void pesquisarTodos() throws DAOException {
        Produto produto1 = criarProduto("A5");

        Produto produto2 = criarProduto("A6");

        Produto produtoCadastrado1 = iProdutoDAO.cadastrar(produto1);
        assertNotNull(produtoCadastrado1);
        assertNotNull(produtoCadastrado1.getId());

        Produto produtoCadastrado2 = iProdutoDAO.cadastrar(produto2);
        assertNotNull(produtoCadastrado2);
        assertNotNull(produtoCadastrado2.getId());

        List<Produto> list = iProdutoDAO.pesquisarTodos();
        assertNotNull(list);
        assertEquals(2, list.size());

        for (Produto prod : list) {
            iProdutoDAO.excluir(prod);
            Produto produtoExcluido = iProdutoDAO.pesquisar(prod.getId());
            assertNull(produtoExcluido);
        }
    }

    private Produto criarProduto(String codigo) {
        Produto produto = new Produto();

        produto.setCodigo(codigo);
        produto.setDescricao("Produto");
        produto.setNome("Produto descricao");
        produto.setValor(BigDecimal.TEN);

        return produto;
    }
}
