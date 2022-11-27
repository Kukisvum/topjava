package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserWithRoleExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> map = new HashMap<>();
        User user;
        while (rs.next()) {
            Long id = rs.getLong("id");
            user = map.get(id);
            if (user == null) {
                user = new User();
                user.setId(Math.toIntExact(id));
                user.setEmail(rs.getString("email"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRoles(new HashSet<>());
                map.put(id, user);
            }
            Set<Role> roles = new HashSet<>();
            if (rs.getString("role").equals("ADMIN")) {
                roles.add(Role.ADMIN);
            }
            if (rs.getString("role").equals("USER")) {
                roles.add(Role.USER);
            }
            user.setRoles(roles);

        }
        return new ArrayList<>(map.values());

    }
}
