package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(value= MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public Meal getById(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {

        super.delete(id);
    }

    @PostMapping()
    public ResponseEntity<Meal> created(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id,@RequestBody Meal meal) {
        super.update(meal, id);
    }

    @GetMapping("/getBetween")
    public List<MealTo>  getBetween(@RequestParam String start,
                                    @RequestParam String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return super.getBetween(startDate.toLocalDate(), startDate.toLocalTime(), endDate.toLocalDate(), endDate.toLocalTime());
    }

}