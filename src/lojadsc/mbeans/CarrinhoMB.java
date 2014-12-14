package lojadsc.mbeans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lojadsc.daos.ProdutoDAORemote;
import lojadsc.entidades.Comprador;
import lojadsc.entidades.ItemDeVenda;
import lojadsc.entidades.Produto;

@ManagedBean(name="carrinhoMB")
@SessionScoped
public class CarrinhoMB {
	private Map<Integer, ItemDeVenda> itens;
	private Comprador comprador;
	private int idProduto;

	@EJB
	private ProdutoDAORemote produtoDAO;

	public CarrinhoMB() {
		super();
		itens = new HashMap<Integer, ItemDeVenda>();
	}

	public Collection<ItemDeVenda> getItens() {
		return itens.values();
	}

	public Comprador getComprador() {
		return comprador;
	}

	public void setComprador(Comprador comprador) {
		this.comprador = comprador;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public boolean isPossuiItens() {
		return !itens.isEmpty();
	}

	public String adicionaItem() {
		Produto p = produtoDAO.getProduto(idProduto);
		if (p != null) {
			produtoDAO.decrementaQuantidade(idProduto);
			if (itens.containsKey(p.getId())) {
				int quant = itens.get(p.getId()).getQuantidade();
				itens.get(p.getId()).setQuantidade(++quant);
			} else {
				ItemDeVenda novo = new ItemDeVenda();
				novo.setProduto(p);
				novo.setQuantidade(1);
				itens.put(p.getId(), novo);
			}
		}
		return "index.xhtml";
	}

	public String incrementaItem() {
		Produto p = produtoDAO.getProduto(idProduto);
		if (p != null) {
			produtoDAO.decrementaQuantidade(idProduto);
			if (itens.containsKey(p.getId())) {
				int quant = itens.get(p.getId()).getQuantidade();
				itens.get(p.getId()).setQuantidade(++quant);
			}
		}
		return "carrinho.xhtml";
	}

	public String decrementaItem() {
		Produto p = produtoDAO.getProduto(idProduto);
		if (p != null) {
			produtoDAO.incrementaQuantidade(idProduto);
			if (itens.containsKey(p.getId())) {
				int quant = itens.get(p.getId()).getQuantidade();
				if (quant == 1) {
					itens.remove(p.getId());
				} else {
					itens.get(p.getId()).setQuantidade(--quant);
				}
			}
		}
		return "carrinho.xhtml";
	}

	public double getTotal() {
		double total = 0;
		for (ItemDeVenda item: itens.values()) {
			total += item.getSubtotal();
		}
		return total;
	}

	public String finalizaCompra() {
		return "";
	}

	@PreDestroy
	public void cancelaCarrinho() {
		if (!itens.isEmpty()) {
			for (ItemDeVenda item: itens.values()) {
				produtoDAO.adicionaQuantidade(item.getProduto().getId(), item.getQuantidade());
			}
		}
		itens = new HashMap<Integer, ItemDeVenda>();
	}
}
