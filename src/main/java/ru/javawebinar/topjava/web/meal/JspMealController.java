package ru.javawebinar.topjava.web.meal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;


@Controller
public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService mealService;



    @GetMapping("/meals")
    public String getAllMeals(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meals", MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @PostMapping("/meals")
    public String saveMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {

            int userId = SecurityUtil.authUserId();
            assureIdConsistent(meal, Integer.parseInt(request.getParameter("id")));
            mealService.update(meal, userId );
        } else {
            int userId = SecurityUtil.authUserId();
            checkNew(meal);
            mealService.create(meal, userId);
        }

        return "redirect:/meals";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(@RequestParam int id) {
        int userId = SecurityUtil.authUserId();
        mealService.delete(id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    public String updateMeal(@RequestParam int id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = mealService.get(id, userId);
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals/create")
    public String createMeal(Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("update {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }


}
