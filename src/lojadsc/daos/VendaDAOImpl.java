package lojadsc.daos;

import java.util.List;

import javax.ejb.Stateless;

import lojadsc.entidades.Comprador;
import lojadsc.entidades.Venda;

@Stateless
public class VendaDAOImpl extends AppDAO implements VendaDAORemote {

	@Override
	public List<Venda> getVendas() {
		// TODO
		return null;
	}

	/**
	 * Persiste os dados da venda
	 *
	 * @see http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
	 */
	@Override
	public void save(Venda venda) {
		if (venda != null) {
			Comprador comprador = em.find(Comprador.class, venda.getComprador().getCpf());
			if (comprador != null) {
				// salva os dados da venda para o comprador já cadastrado
				em.merge(venda);
			}else{
				// cria o comprador e os dados da venda
				em.persist(venda);
			}
		}
	}
}
