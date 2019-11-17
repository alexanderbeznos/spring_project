package servlets.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servlets.models.FootballPosition;
import servlets.models.Player;
import servlets.storage.DBStore;
import servlets.storage.PlayerValidationException;

import java.util.Collection;

/**
 * @version 1.0.
 * @since 25.10.2019.
 * @author Alexander Beznos (ast1bn@mail.ru)
 */
@Service
public class ValidateService implements Validate {

    /**
     * Ссылка на объект DBStore.
     */
    private DBStore dbStore;

    @Autowired
    public ValidateService(DBStore dbStore) {
        this.dbStore = dbStore;
    }

    /**
     * Метод для добавления и редактирования игрока в БД.
     * @param player игрок для добавления
     */
    @Override
    public void addOrUpdate(Player player) throws PlayerValidationException {
        this.dbStore.addOrUpdate(player);
    }

    /**
     * Метод для удаления игрока из БД.
     * @param id номер игрока для удаления.
     */
    @Override
    public void delete(int id) throws PlayerValidationException {
        this.dbStore.delete(id);

    }

    /**
     * Метод возвращает список всех игроков.
     */
    @Override
    public Collection<Player> findAll() throws PlayerValidationException {
        Collection<Player> result = this.dbStore.findAll();
        return result;
    }

    /**
     * Метод возвращает список всех позиций.
     */
    @Override
    public Collection<FootballPosition> findAllPositions() throws PlayerValidationException {
        Collection<FootballPosition> result = this.dbStore.findAllPositions();
        return result;
    }

    /**
     * Метод возвращает игрока по номеру.
     * @param id номер искомого игрока.
     */
    @Override
    public Player findById(int id) throws PlayerValidationException {
        return this.dbStore.findById(id);
    }
}
