package servlets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import servlets.models.Player;
import servlets.storage.PlayerValidationException;
import servlets.validation.ValidateService;
import java.util.Collection;

/**
 * @version 1.0.
 * @since 07.11.2019.
 * @author Alexander Beznos (ast1bn@mail.ru)
 */
@Controller
public class PlayersController {
    private ValidateService validateService;

    @Autowired
    public PlayersController(ValidateService validateService) {
        this.validateService = validateService;
    }

    /**
     * Метод перенаправляет на страницу со всеми игроками.
     */
    @GetMapping("/list")
    public String getPlayers(Model model) {
        try {
            Collection<Player> players = validateService.findAll();
            model.addAttribute("players", players);
            model.addAttribute("answer", "ok");
        } catch (PlayerValidationException e) {
            model.addAttribute("answer", "Error");
        }
        return "list";
    }

    /**
     * Метод передаёт игрока в качестве атрибута и перенаправляет на страницу создания игрока.
     */
    @GetMapping("/playerForm")
    public String addPlayer(Model model) {
        model.addAttribute("player", new Player());
        return "createOrUpdate";
    }

    /**
     * Метод добавляет игрока в БД и перенаправляет на страницу с результатом.
     */
    @PostMapping("/playerForm")
    public String addPlayerPost(Model model, @ModelAttribute("newPlayer") Player player) {
        try {
            validateService.addOrUpdate(player);
            model.addAttribute("answer", "Operation was successful");
        } catch (PlayerValidationException e) {
            model.addAttribute("answer", e.getMessage());
        }
        return "createPost";
    }


    /**
     * Метод передаёт игрока полученного из БД по id.
     */
    @GetMapping("/updatePlayerForm")
    public String updatePlayer(@RequestParam("id") int id, Model model) {
        try {
            Player player = validateService.findById(id);
            model.addAttribute("player", player);
        } catch (PlayerValidationException e) {
            e.printStackTrace();
        }
        return "createOrUpdate";
    }

    /**
     * Метод перенаправляет на страницу подтверждения удаления игрока.
     */
    @GetMapping("/deletePlayer")
    public String deletePlayer() {
        return "delete";
    }

    /**
     * Метод удаляет игрока из БД.
     */
    @PostMapping("/deletePlayer")
    public String deletePlayerPost(Model model, @RequestParam("id") int id) {
        try {
            validateService.delete(id);
            model.addAttribute("answer", "Operation was successful");
        } catch (PlayerValidationException e) {
            model.addAttribute("answer", e.getMessage());
        }
        return "deletePost";
    }
}
