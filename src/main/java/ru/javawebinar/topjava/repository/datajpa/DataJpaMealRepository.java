package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORTED_BY_DATE_TIME = Sort.by(Sort.Direction.DESC, "date_time");
    private final CrudMealRepository crudRepository;

    private final CrudUserRepository userRepository;
    @Autowired
    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getReferenceById(userId));
        if (meal.isNew()) {
            crudRepository.save(meal);
        } else if (crudRepository.findMealById(meal.getId(), userId) == null) {
            return null;
        }
//        meal.setId(crudRepository.getNextVal());
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {

        return crudRepository.deleteByIdAndUser(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {

        return crudRepository.findMealById(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
