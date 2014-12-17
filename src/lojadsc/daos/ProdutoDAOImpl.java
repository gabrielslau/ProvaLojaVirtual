package lojadsc.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lojadsc.entidades.Produto;

@Stateless
public class ProdutoDAOImpl extends AppDAO implements ProdutoDAORemote {

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> getProdutos() {
		Query query = em.createQuery("SELECT p FROM Produto p");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> getProdutosEmEstoque() {
		Query query = em.createQuery("SELECT p FROM Produto p WHERE p.quantidade > 0");
		return query.getResultList();
	}

	@Override
	public void adicionaQuantidade(int idProduto, int quantidade) {
		Produto p = em.find(Produto.class, idProduto);
		if (p != null) {
			int quant = p.getQuantidade();
			p.setQuantidade(quant + quantidade);
			em.merge(p);
		}
	}

	@Override
	public void decrementaQuantidade(int idProduto) {
		Produto p = em.find(Produto.class, idProduto);
		if (p != null) {
			int quant = p.getQuantidade();
			p.setQuantidade(--quant);
			em.merge(p);
		}
	}

	@Override
	public Produto getProduto(int idProduto) {
		return em.find(Produto.class, idProduto);
	}

	@Override
	public void incrementaQuantidade(int idProduto) {
		Produto p = em.find(Produto.class, idProduto);
		if (p != null) {
			int quant = p.getQuantidade();
			p.setQuantidade(++quant);
			em.merge(p);
		}
	}

	@Override
	public void addProduto(Produto produto) {
		if (produto != null) {
			em.persist(produto);
		}
	}
	@Override
	public void removeProduto(Produto produto) {
		if (produto != null) {
			Produto toBeRemoved = em.merge(produto);
			em.remove(toBeRemoved);
		}
	}
	@Override
	public void editarProduto(Produto produto) {
		Produto p = em.find(Produto.class, produto.getId());
		if (p != null) {
			p.setDescricao(produto.getDescricao());
			p.setQuantidade(produto.getQuantidade());
			p.setValor(produto.getValor());
			em.merge(p);
		}
	}

	/**
	 * Verifica se um dado produto está disponível no estoque
	 */
	@Override
	public Produto getProdutoEmEstoque(int id) {
		Produto produto = em.find(Produto.class, id);
		if(produto == null || produto.getQuantidade() == 0){
			return null;
		}
		return produto;
	}
}
