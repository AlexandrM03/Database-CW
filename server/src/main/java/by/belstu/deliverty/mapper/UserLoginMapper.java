package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.UserDTO;
import org.springframework.jdbc.core.RowMapper;

public class UserLoginMapper implements RowMapper<UserDTO> {
    @Override
    public UserDTO mapRow(java.sql.ResultSet resultSet, int i) throws java.sql.SQLException {
        UserDTO user = new UserDTO();
        user.setUsername(resultSet.getString("username"));
        user.setRoleName(resultSet.getString("role_name"));
        return user;
    }
}
