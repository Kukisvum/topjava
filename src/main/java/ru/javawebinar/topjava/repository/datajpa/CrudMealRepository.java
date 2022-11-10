package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {



    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC ")
    List<Meal> findAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.id=:id and m.user.id=:userId")
    Meal findMealById(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.dateTime >=:startDateTime and m.dateTime <:endDateTime " +
            " and m.user.id=:userId ORDER BY m.dateTime DESC ")
    List<Meal> findAllBetweenHalfOpen(@Param("startDateTime") LocalDateTime startDateTime
            ,@Param("endDateTime") LocalDateTime endDateTime
            ,@Param("userId") int userId);


    User getReferenceById(Class<User> user, int userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id and m.user.id=:userId")
    int deleteByIdAndUser(@Param("id") int id,@Param("userId") int userId);

    @Query(value = "SELECT global_seq.nextval FROM meals", nativeQuery = true)
    Integer getNextVal();
}
