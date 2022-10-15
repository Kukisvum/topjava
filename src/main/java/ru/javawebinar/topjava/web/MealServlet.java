package ru.javawebinar.topjava.web;



import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";

    private static final List<Meal> meals = new ArrayList();

    static {
        List<Meal> mealsObject = Arrays.asList(
                new Meal(1,LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2,LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3,LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4,LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5,LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6,LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7,LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
            meals.addAll(mealsObject);
        }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");

        List<MealTo> mealsTo = filteredByStreams(meals, 2000);


        if (action!=null && action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            for (Meal meal: meals) {
                if (meal.getId() == id) {
                    meals.remove(meal);
                    break;
                }
            }
            for (MealTo mealTo: mealsTo) {
                if (mealTo.getId() == id) {
                    mealsTo.remove(mealTo);
                    break;
                }
            }
            forward = LIST_MEALS;
            request.setAttribute("mealsTo", mealsTo);

        } else if (action!=null && action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = new Meal();
            for (Meal mealRow : meals) {
                if (mealRow.getId() == id) {
//                    meal.setId(mealRow.getId());
                    meal = mealRow;
                }
            }
            request.setAttribute("meal", meal);
        } else if (action!=null && action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
        } else {
            forward = LIST_MEALS;
            request.setAttribute("mealsTo", mealsTo);
        }
          request.getRequestDispatcher(forward).forward(request, response);
//        response.sendRedirect(forward);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        meal.setDescription(request.getParameter("description"));
        try {
            String ldt = request.getParameter("dateTime");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(ldt, formatter);
            meal.setDateTime(localDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            meal.setId(meals.size()+1);
            meals.add(meal);
        } else {
            for (Meal oneMeal: meals) {
                if (oneMeal.getId() == Integer.parseInt(id)) {
                    oneMeal.setCalories(Integer.parseInt(request.getParameter("calories")));
                    oneMeal.setDescription(request.getParameter("description"));
                    try {
                        String ldt = request.getParameter("dateTime");
                        System.out.println(ldt);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                        LocalDateTime localDateTime = LocalDateTime.parse(ldt, formatter);
                        oneMeal.setDateTime(localDateTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            for (Meal oneMeal: meals) {
                if (oneMeal.getId() == Integer.parseInt(id)) {
                    meal = oneMeal;
                }
        }
        }

        List<MealTo> mealsTo = filteredByStreams(meals, 2000);
        request.setAttribute("mealsTo", mealsTo);
        request.getRequestDispatcher(LIST_MEALS).forward(request, response);
//        response.sendRedirect("meals.jsp");
    }


    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }


    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}