package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.UserDTO;
import org.springframework.jdbc.core.RowMapper;

public class UserDataMapper implements RowMapper<UserDTO> {
    @Override
    public UserDTO mapRow(java.sql.ResultSet resultSet, int i) throws java.sql.SQLException {
        UserDTO user = new UserDTO();
        user.setUsername(resultSet.getString("username"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setTelephone(resultSet.getString("telephone"));
        return user;
    }
}