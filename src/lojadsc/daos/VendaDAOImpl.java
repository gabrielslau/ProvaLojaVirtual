package lojadsc.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lojadsc.entidades.Comprador;
import lojadsc.entidades.Venda;

@Stateless
public class VendaDAOImpl extends AppDAO implements VendaDAORemote {

	@SuppressWarnings("unchecked")
	@Override
	public List<Venda> getVendas() {
		Query query = em.createQuery("SELECT v FROM Venda v");
		return query.getResultList();
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
