package lojadsc.daos;

import java.util.List;

import javax.ejb.Remote;

import lojadsc.entidades.Venda;

@Remote
public interface VendaDAORemote {
	public List<Venda> getVendas();
	public void save(Venda venda);
}
