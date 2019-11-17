package servlets.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import servlets.models.User;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class DBUsers {
    private SessionFactory factory;

    @Autowired
    private DBUsers(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }

    private void doVoid(final Consumer<Session> command) throws UserValidationException {
        final Session session = factory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            command.accept(session);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
                throw new UserValidationException("Problem has occurred");
            }
        } finally {
            session.close();
        }
    }

    private <T> T doFunction(final Function<Session, T> command) throws UserValidationException {
        final Session session = factory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            T rsl = command.apply(session);
            tr.commit();
            return rsl;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
                e.printStackTrace();
                throw new UserValidationException("Problem has occurred");
            }
        } finally {
            session.close();
        }
        return null;
    }

    /**
     * Метод создаёт и выполняет запрос на добавление нового пользователя в БД.
     * Пользователю присваивается уникальный номер.
     */
    public void addOrUpdate(User user) throws UserValidationException {
        doVoid(tmp -> tmp.saveOrUpdate(user));
    }

    /**
     * Метод создает и выполняет запрос по поиску игрока по id в БД.
     */
    public User findByLogin(String login) throws UserValidationException {
        String query = String.format("from User u where u.login = '%s'", login);
        return (User) doFunction(tmp -> tmp.createQuery(query).getSingleResult());
    }
}
