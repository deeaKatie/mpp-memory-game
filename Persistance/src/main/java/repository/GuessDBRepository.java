package repository;

import exception.RepositoryException;
import model.Guess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class GuessDBRepository implements IGuessDBRepository {

    private static final Logger logger= LogManager.getLogger();
    private Session session;

    public GuessDBRepository() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = factory.openSession();
    }
    @Override
    public Guess add(Guess entity) {
        Transaction transaction = session.beginTransaction();
        Long id = (Long) session.save(entity);
        entity.setId(id);
        transaction.commit();
        return entity;
    }

    @Override
    public void delete(Guess entity) {

    }

    @Override
    public void update(Guess entity, Long aLong) {
        logger.traceEntry();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        logger.traceExit();
    }

    @Override
    public Guess findById(Long aLong) throws RepositoryException {
        return null;
    }

    @Override
    public Iterable<Guess> getAll() {
        return null;
    }
}
