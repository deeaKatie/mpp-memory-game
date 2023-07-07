package repository;

import exception.RepositoryException;
import model.User;
import model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class WordDBRepository implements IWordDBRepository {

    private static final Logger logger= LogManager.getLogger();
    private Session session;

    public WordDBRepository() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = factory.openSession();
    }

    @Override
    public Word add(Word entity) {
        Transaction transaction = session.beginTransaction();
        Long id = (Long) session.save(entity);
        entity.setId(id);
        transaction.commit();
        return entity;
    }

    @Override
    public void delete(Word entity) {

    }

    @Override
    public void update(Word entity, Long aLong) {

    }

    @Override
    public Word findById(Long aLong) throws RepositoryException {
        return null;
    }

    @Override
    public Iterable<Word> getAll() {
        Query query = session.createQuery("from Word");
        List<Word> entities = query.list();
        return entities;
    }
}
