package lojadsc.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AppDAO {
	@PersistenceContext(unitName="lojadsc")
	EntityManager em;
}
