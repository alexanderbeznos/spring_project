package servlets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import servlets.models.FootballPosition;
import servlets.storage.PlayerValidationException;
import servlets.validation.ValidateService;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0.
 * @since 07.11.2019.
 * @author Alexander Beznos (ast1bn@mail.ru)
 */
@Controller
public class CountryPositionController {

    private final Map<String, List<String>> clubs = new ConcurrentHashMap<>();
    private ValidateService validateService;

    @Autowired
    public CountryPositionController(ValidateService validateService) {
        this.validateService = validateService;
        clubs.put("Russia", Arrays.asList("Spartak", "Zenit", "Lokomotiv"));
        clubs.put("Spain", Arrays.asList("Barcelona", "Real Madrid", "Atletico Madrid"));
        clubs.put("England", Arrays.asList("Manchester City", "Liverpool", "Arsenal"));
    }

    /**
     * Метод отправляет в ответе список стран.
     */
    @GetMapping("/land")
    @ResponseBody
    public List<String> getCountries() {
        return new ArrayList<>(this.clubs.keySet());
    }

    /**
     * Метод получает страну. Находит список клубов этой страны. Оправляет в ответе список клуб.
     */
    @PostMapping("/clubs")
    @ResponseBody
    public List<String> getClub(@RequestBody String country) {
        return clubs.get(country);
    }

    /**
     * Метод получает список из БД и отправляет его в ответе.
     */
    @GetMapping("/position")
    @ResponseBody
    public Collection<FootballPosition> getPositions() throws PlayerValidationException {
        return validateService.findAllPositions();
    }
}
