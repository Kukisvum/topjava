package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal get(int id, int authUserId) {
        return service.getById(id, authUserId);
    }


    public List<Meal> get() {
        return service.getAll(SecurityUtil.authUserId());
    }

    public List<Meal> get(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return service.getAll(SecurityUtil.authUserId(), dateFrom, dateTo, timeFrom, timeTo);
    }

    public void delete(int id, int authUserId) {
        service.delete(id, authUserId);
    }

    public Meal save(Meal meal, int authUserId) {
        return service.save(meal, authUserId);
    }



}