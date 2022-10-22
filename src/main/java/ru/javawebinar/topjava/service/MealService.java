package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Service
public class MealService {

    @Autowired
    private MealRepository repository;

    public Meal getById(int id, int authUserId) {
        return repository.get(id, authUserId);
    }


    public List<Meal> getAll(int authUserId) {
        return  (List<Meal>) repository.getAll(authUserId);

    }

    public List<Meal> getAll(int authUserId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return (List<Meal>) repository.getAll(authUserId, dateFrom, dateTo, timeFrom, timeTo);
    }


    public void delete(int id, int authUserId) {
        repository.delete(id, authUserId);
    }


    public Meal save(Meal meal, int authUserId) {
        return repository.save(meal, authUserId);
    }


}