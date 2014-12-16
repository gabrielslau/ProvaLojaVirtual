package lojadsc.daos;

import java.util.List;

import javax.ejb.Stateless;

import lojadsc.entidades.Comprador;
import lojadsc.entidades.Venda;

@Stateless
public class VendaDAOImpl extends AppDAO implements VendaDAORemote {

	@Override
	public List<Venda> getVendas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Venda venda) {
		if (venda != null) {
			Comprador comprador = em.find(Comprador.class, venda.getComprador().getCpf());
			if (comprador != null) {
				/*
				 * desacopla o comprador para que evite de salvá-lo se o mesmo
				 * já existir no banco
				 */
				em.detach(comprador);
			}
			// salvou
			em.persist(venda);
		}
	}
}
