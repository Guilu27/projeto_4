package main;

import dao.ClienteDAO;
import dao.IClienteDAO;
import domain.Cliente;
import exceptions.DAOException;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ClienteDAOTest {
    private IClienteDAO iClienteDAO;
    private Random rd;

    public ClienteDAOTest() {
        iClienteDAO = new ClienteDAO();
        rd = new Random();
    }

    @Test
    public void cadastrar() throws DAOException {
        Cliente cliente = criarCliente();

        Cliente clienteCadastrado = iClienteDAO.cadastrar(cliente);
        assertNotNull(clienteCadastrado);
        assertNotNull(clienteCadastrado.getId());

        Cliente clientePesquisado = iClienteDAO.pesquisar(cliente.getId());
        assertNotNull(clientePesquisado);
        assertEquals(cliente.getCpf(), clientePesquisado.getCpf());

        iClienteDAO.excluir(clienteCadastrado);
        Cliente produtoExcluido = iClienteDAO.pesquisar(clienteCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void pesquisar() throws DAOException {
        Cliente cliente = criarCliente();

        Cliente clienteCadastrado = iClienteDAO.cadastrar(cliente);
        assertNotNull(clienteCadastrado);
        assertNotNull(clienteCadastrado.getId());

        Cliente clientePesquisado = iClienteDAO.pesquisar(cliente.getId());
        assertNotNull(clientePesquisado);
        assertEquals(cliente.getCpf(), clientePesquisado.getCpf());

        iClienteDAO.excluir(clienteCadastrado);
        Cliente produtoExcluido = iClienteDAO.pesquisar(clienteCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void excluir() throws DAOException {
        Cliente cliente = criarCliente();

        Cliente clienteCadastrado = iClienteDAO.cadastrar(cliente);
        assertNotNull(clienteCadastrado);
        assertNotNull(clienteCadastrado.getId());

        Cliente clientePesquisado = iClienteDAO.pesquisar(cliente.getId());
        assertNotNull(clientePesquisado);
        assertEquals(cliente.getCpf(), clientePesquisado.getCpf());

        iClienteDAO.excluir(clienteCadastrado);
        Cliente produtoExcluido = iClienteDAO.pesquisar(clienteCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void alterar() throws DAOException {
        Cliente cliente = criarCliente();

        Cliente clienteCadastrado = iClienteDAO.cadastrar(cliente);
        assertNotNull(clienteCadastrado);
        assertNotNull(clienteCadastrado.getId());

        Cliente clientePesquisado = iClienteDAO.pesquisar(cliente.getId());
        assertNotNull(clientePesquisado);
        assertEquals(cliente.getCpf(), clientePesquisado.getCpf());

        clienteCadastrado.setNome("Outro Cliente");
        iClienteDAO.alterar(clienteCadastrado);
        Cliente clienteAlterado = iClienteDAO.pesquisar(clienteCadastrado.getId());
        assertNotNull(clienteAlterado);
        assertEquals(clienteCadastrado.getNome(), clienteAlterado.getNome());

        iClienteDAO.excluir(clienteCadastrado);
        Cliente produtoExcluido = iClienteDAO.pesquisar(clienteCadastrado.getId());
        assertNull(produtoExcluido);
    }

    @Test
    public void pesquisarTodos() throws DAOException {
        Cliente cliente1 = criarCliente();
        Cliente cliente2 = criarCliente();

        Cliente clienteCadastrado1 = iClienteDAO.cadastrar(cliente1);
        assertNotNull(clienteCadastrado1);
        assertNotNull(clienteCadastrado1.getId());

        Cliente clienteCadastrado2 = iClienteDAO.cadastrar(cliente2);
        assertNotNull(clienteCadastrado2);
        assertNotNull(clienteCadastrado2.getId());

        List<Cliente> list = iClienteDAO.pesquisarTodos();
        assertNotNull(list);
        assertEquals(2, list.size());

        for (Cliente cliente : list) {
            iClienteDAO.excluir(cliente);
            Cliente clienteExcluido = iClienteDAO.pesquisar(cliente.getId());
            assertNull(clienteExcluido);
        }
    }


    private Cliente criarCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Cliente 1");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        return cliente;
    }
}
