package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);
//
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
//        Collections.sort(meals, (m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
        meals.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
        HashMap<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();
        int sumPerDay = 0;
        for (int i = 0; i < meals.size(); i++) {
            if ((i + 1) != meals.size()
                    && meals.get(i).getDateTime().toLocalDate().equals(meals.get(i + 1).getDateTime().toLocalDate())
            ) {
                sumPerDay += meals.get(i).getCalories();
            } else if (i + 1 == meals.size()) {
                sumPerDay += meals.get(meals.size() - 1).getCalories();
                sumCaloriesPerDay.put(meals.get(i).getDateTime().toLocalDate(), sumPerDay);
            } else {
                sumCaloriesPerDay.put(meals.get(i).getDateTime().toLocalDate(), sumPerDay);
                sumPerDay = 0;
            }
        }
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (caloriesPerDay < sumCaloriesPerDay.get(userMeal.getDateTime().toLocalDate())) {
                userMealWithExcess.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
            } else {
                userMealWithExcess.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
            }
        }
        List<UserMealWithExcess> resultMealTo = new ArrayList<>();
        for (UserMealWithExcess userMeal : userMealWithExcess) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultMealTo.add(userMeal);
            }
        }
        return userMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        meals.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
        Map<LocalDate, Integer> caloriesPerDay1 = meals.stream()
                .collect(Collectors.groupingBy((item -> item.getDateTime().toLocalDate()),
                        Collectors.summingInt(UserMeal::getCalories)));
        ArrayList<UserMealWithExcess> mealTo = new ArrayList<>();
        meals.stream().forEach((m) -> {
            if (caloriesPerDay < caloriesPerDay1.get(m.getDateTime().toLocalDate())) {
                mealTo.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), false));
            } else {
                mealTo.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), true));
            }
        });
        return mealTo.stream()
                .filter((m) -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());

    }
}
