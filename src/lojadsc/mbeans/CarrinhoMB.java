package lojadsc.mbeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lojadsc.daos.ProdutoDAORemote;
import lojadsc.daos.VendaDAORemote;
import lojadsc.entidades.Comprador;
import lojadsc.entidades.FormaDePagamento;
import lojadsc.entidades.ItemDeVenda;
import lojadsc.entidades.Produto;
import lojadsc.entidades.Venda;

@ManagedBean(name = "carrinhoMB")
@SessionScoped
public class CarrinhoMB extends AppMB {
	private Map<Integer, ItemDeVenda> itens;
	private Comprador comprador;
	private FormaDePagamento formaDePagamento;
	private List<FormaDePagamento> formasDePagamento;
	private int idProduto;

	@EJB
	private ProdutoDAORemote produtoDAO;

	@EJB
	private VendaDAORemote vendaDAO;

	public CarrinhoMB() {
		super();
		itens = new HashMap<Integer, ItemDeVenda>();
		comprador = new Comprador();
	}

	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public List<FormaDePagamento> getFormasDePagamento() {
		return Arrays.asList(FormaDePagamento.values());
	}

	public void setFormasDePagamento(List<FormaDePagamento> formasDePagamento) {
		this.formasDePagamento = formasDePagamento;
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

	public boolean isSemItens() {
		return itens.isEmpty();
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
					// Redireciona para a página inicial
					if (itens.isEmpty())
						this.redirect("index");
				} else {
					itens.get(p.getId()).setQuantidade(--quant);
				}
			}
		}
		return "carrinho.xhtml";
	}

	public double getTotal() {
		double total = 0;
		for (ItemDeVenda item : itens.values()) {
			total += item.getSubtotal();
		}
		return total;
	}

	/**
	 * Finalizar a compra dos itens no carrinho. A finalização da compra
	 * consiste em solicitar as informações do comprador e a forma de pagamento
	 * e salvar TODAS (a venda em si, os seus itens e os dados com comprador) as
	 * informações da "Venda" no banco de dados
	 *
	 */
	public String finalizaCompra() {
		if (itens.isEmpty()) {
			// TODO: saber como redirecionar automaticamente
			this.redirect("index.xhtml");
		}
		return "finaliza_compra.xhtml";
	}

	public String processafinalizaCompra() {
		Venda venda = new Venda();
		List<ItemDeVenda> itensDeVenda = new ArrayList<ItemDeVenda>(
				itens.values());

		venda.setComprador(comprador);
		venda.setFormaDePagamento(formaDePagamento);
		venda.setItensDeVenda(itensDeVenda);

		// TODO: adicionar try-catch e setar mensagem de erro se não conseguir
		// salvar
		vendaDAO.save(venda);

		// TODO: Verificar se os produtos do carrinho estão com contador ZERO
		// se estiverem, apaga o produto do banco

		// Limpa o carrinho
		itens = new HashMap<Integer, ItemDeVenda>();

		return "index.xhtml";
	}

	@PreDestroy
	public void cancelaCarrinho() {
		if (!itens.isEmpty()) {
			for (ItemDeVenda item : itens.values()) {
				produtoDAO.adicionaQuantidade(item.getProduto().getId(),
						item.getQuantidade());
			}
		}
		itens = new HashMap<Integer, ItemDeVenda>();
		this.redirect("index");
	}
}
