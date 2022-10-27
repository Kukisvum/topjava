package ru.javawebinar.topjava.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest  {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void Get() {
        Meal meal = mealService.get(mealId1, user_id);
        assertMatch(meal1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void GetNotFound() {
        Meal meal = mealService.get(mealId1, admin_id);
//        assertMatch(NotFoundException.class, meal);
    }

    @Test
    public void Delete() {
        mealService.delete(mealId2,user_id);
        assertThrows(NotFoundException.class,  () -> mealService.get(mealId2, user_id));
    }

    @Test
    public void GetBetweenInclusive() {
        Iterable<Meal> actual = mealService.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY,30),
                                                               LocalDate.of(2020,Month.JANUARY,30), 100000);
        assertMatch(actual, meal3, meal2, meal1 );
    }

    @Test
    public void GetAll() {
        Iterable<Meal> actual = mealService.getAll(user_id);
        assertMatch(actual, meal3, meal2, meal1);
    }

    @Test
    public void Update() {
        Meal updated = getUpdated();
        mealService.update(updated, user_id);

        assertMatch(mealService.get(mealId1, user_id), updated);

    }

    @Test
    public void Create() {
        Meal created = mealService.create(getNew(), user_id);
        Integer newInt = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newInt);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newInt, user_id), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),10000));
    }

}