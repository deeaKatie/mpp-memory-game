package repository;

import exception.RepositoryException;
import model.Configuration;
import model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigurationDBRepository implements IConfigurationDBRepository {

    private static final Logger logger= LogManager.getLogger();
    private Session session;

    @Autowired
    public ConfigurationDBRepository() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = factory.openSession();
    }
    @Override
    public Configuration add(Configuration entity) {
        Transaction transaction = session.beginTransaction();
        Long id = (Long) session.save(entity);
        entity.setId(id);
        transaction.commit();
        return entity;
    }

    @Override
    public void delete(Configuration entity) {

    }

    @Override
    public void update(Configuration entity, Long aLong) {

    }

    @Override
    public Configuration findById(Long aLong) throws RepositoryException {
        return null;
    }

    @Override
    public Iterable<Configuration> getAll() {
        Query query = session.createQuery("from Configuration ");
        List<Configuration> entities = query.list();
        return entities;
    }
}
