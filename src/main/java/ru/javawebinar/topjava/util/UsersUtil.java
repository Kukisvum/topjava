package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class UsersUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<User> users = Arrays.asList(
            new User(1, "Vasya", "vasya@mail.ru", "1234", DEFAULT_CALORIES_PER_DAY, true, new ArrayList<Role>(Arrays.asList(Role.USER))),
            new User(2, "Petya", "petya@mail.ru", "1234", DEFAULT_CALORIES_PER_DAY, true, new ArrayList<Role>(Arrays.asList(Role.USER)))

    );
}
