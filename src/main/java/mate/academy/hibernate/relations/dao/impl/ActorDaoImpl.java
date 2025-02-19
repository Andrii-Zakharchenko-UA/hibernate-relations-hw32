package mate.academy.hibernate.relations.dao.impl;

import jakarta.persistence.Entity;
import java.util.Optional;
import mate.academy.hibernate.relations.dao.ActorDao;
import mate.academy.hibernate.relations.exeption.DataProcessingException;
import mate.academy.hibernate.relations.model.Actor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Entity
public class ActorDaoImpl extends AbstractDao implements ActorDao {
    public ActorDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Actor add(Actor actor) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(actor);
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add " + actor, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return actor;
    }

    @Override
    public Optional<Actor> get(Long id) {
        try (Session session = factory.openSession()) {
            Actor actor = session.get(Actor.class, id);
            return Optional.ofNullable(actor);
        } catch (DataProcessingException e) {
            throw new DataProcessingException("Can't get actor by id " + id, e);
        }
    }
}
