package servlets.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import servlets.models.FootballPosition;
import servlets.models.Player;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @version 1.0.
 * @since 25.10.2019.
 * @author Alexander Beznos (ast1bn@mail.ru)
 */
@Repository
public class DBStore implements Store {

    private SessionFactory factory;

    @Autowired
    public DBStore(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }

    private <T> T doFunction(final Function<Session, T> command) throws PlayerValidationException {
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
                throw new PlayerValidationException("Problem has occurred");
            }
        } finally {
            session.close();
        }
        return null;
    }

    private void doVoid(final Consumer<Session> command) throws PlayerValidationException {
        final Session session = factory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            command.accept(session);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
                throw new PlayerValidationException("Problem has occurred");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Метод создаёт и редактирует игрока в БД.
     * Игроку присваивается уникальный номер.
     */
    @Override
    public void addOrUpdate(Player player) throws PlayerValidationException {
        doVoid(tmp -> tmp.saveOrUpdate(player));
    }

    /**
     * Метод удаляет игрока из БД.
     */
    @Override
    public void delete(int id) throws PlayerValidationException {
        Player player = new Player(id);
        doVoid(tmp -> tmp.delete(player));
    }

    /**
     * Метод получает всех игроков из БД.
     */
    @Override
    public Collection<Player> findAll() throws PlayerValidationException {
        return doFunction(tmp -> tmp.createQuery("from Player").list());
    }

    /**
     * Метод ищет игрока по id в БД.
     */
    @Override
    public Player findById(int id) throws PlayerValidationException {
        String query = String.format("from Player where id = %d", id);
        return (Player) doFunction(tmp -> tmp.createQuery(query).getSingleResult());
    }

    /**
     * Метод получает позиции игроков из БД.
     */
    @Override
    public Collection<FootballPosition> findAllPositions() throws PlayerValidationException {
        return doFunction(tmp -> tmp.createQuery("from FootballPosition").list());
    }
}

