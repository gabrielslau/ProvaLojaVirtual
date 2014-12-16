package lojadsc.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Venda implements Serializable {
	private static final long serialVersionUID = -4177723501523823682L;
	private int id;
	private Date data;
	private Comprador comprador;
	private FormaDePagamento formaDePagamento;
	private List<ItemDeVenda> itensDeVenda;

	public Venda() {
		super();
		this.data = Calendar.getInstance().getTime();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@ManyToOne(cascade=CascadeType.PERSIST)
	public Comprador getComprador() {
		return comprador;
	}

	public void setComprador(Comprador comprador) {
		this.comprador = comprador;
	}

	@OneToMany(cascade=CascadeType.ALL)
	public List<ItemDeVenda> getItensDeVenda() {
		return itensDeVenda;
	}

	public void setItensDeVenda(List<ItemDeVenda> itensDeVenda) {
		this.itensDeVenda = itensDeVenda;
	}

	@Enumerated(EnumType.ORDINAL)
	public FormaDePagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	@Transient
	public double getTotal() {
		double total = 0;
		for(ItemDeVenda idv : itensDeVenda) {
			total += idv.getSubtotal();
		}
		return total;
	}
}
