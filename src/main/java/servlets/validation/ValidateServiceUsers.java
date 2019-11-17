package servlets.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servlets.models.User;
import servlets.storage.DBUsers;
import servlets.storage.UserValidationException;

/**
 * @version 1.0.
 * @since 29.10.2019.
 * @author Alexander Beznos (ast1bn@mail.ru)
 */

@Service
public class ValidateServiceUsers {
    /**
     * Ссылка на объект DBStore.
     */
    private DBUsers dbUsers;

    @Autowired
    private ValidateServiceUsers(DBUsers dbUsers) {
        this.dbUsers = dbUsers;
    }


    /**
     * Метод для добавления пользователя в БД.
     * @param user пользователя для добавления
     */
    public void addOrUpdate(User user) throws UserValidationException {
        this.dbUsers.addOrUpdate(user);
    }

    /**
     * Метод возвращает пользователя по номеру.
     * @param login номер пользователя игрока.
     */
    public User findByLogin(String login) throws UserValidationException {
        return this.dbUsers.findByLogin(login);
    }
}
