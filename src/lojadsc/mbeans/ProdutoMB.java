package lojadsc.mbeans;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import lojadsc.daos.ProdutoDAORemote;
import lojadsc.entidades.Produto;

@ManagedBean(name="produtoMB")
@ApplicationScoped
public class ProdutoMB {
	private List<Produto> produtos;
	
	@EJB
	private ProdutoDAORemote dao;

	public ProdutoMB() {
		super();
	}

	public List<Produto> getProdutos() {
		this.produtos = dao.getProdutosEmEstoque();
		return produtos;
	}
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
}
