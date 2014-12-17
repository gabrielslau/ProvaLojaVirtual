package lojadsc.mbeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lojadsc.daos.VendaDAORemote;
import lojadsc.entidades.Venda;

@ManagedBean(name = "vendaMB")
@SessionScoped
public class VendaMB extends AppMB {
	// private Map<Integer, ItemDeVenda> itens;

	@EJB
	private VendaDAORemote vendaDAO;

	private List<Venda> itensDeVenda;
	private Venda venda;

	public VendaMB() {
		super();
		itensDeVenda = new ArrayList<Venda>();
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Venda> getItensDeVenda() {
		this.itensDeVenda = vendaDAO.getVendas();
		return itensDeVenda;
	}

	public void setItensDeVenda(List<Venda> itensDeVenda) {
		this.itensDeVenda = itensDeVenda;
	}

	public boolean isPossuiItens() {
		getItensDeVenda();
		return !itensDeVenda.isEmpty();
	}

	public String view(Venda venda) {
		this.venda = venda;
		return "venda_detail.xhtml";
	}
}
