package lojadsc.daos;

import java.util.List;

import javax.ejb.Remote;

import lojadsc.entidades.Produto;

@Remote
public interface ProdutoDAORemote {
	public List<Produto> getProdutos();
	public List<Produto> getProdutosEmEstoque();
	public void adicionaQuantidade(int idProduto, int quantidade);
	public void decrementaQuantidade(int idProduto);
	public void incrementaQuantidade(int idProduto);
	public Produto getProduto(int idProduto);
}
