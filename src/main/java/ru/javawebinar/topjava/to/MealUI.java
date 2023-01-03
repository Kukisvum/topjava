package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.web.converter.DateTimeFormatters;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealUI extends BaseTo implements Serializable {


    private  LocalDateTime dateTime;

    private  String description;

    private  int calories;


    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealUI(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public MealUI(String dateTime, String description, int calories, boolean excess) {
        super(null);

        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }




    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

}
