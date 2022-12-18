package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.DriverDTO;
import org.springframework.jdbc.core.RowMapper;

public class DriverMapper implements RowMapper<DriverDTO> {
    @Override
    public DriverDTO mapRow(java.sql.ResultSet resultSet, int i) throws java.sql.SQLException {
        DriverDTO driver = new DriverDTO();
        driver.setUsername(resultSet.getString("username"));
        driver.setFirstName(resultSet.getString("first_name"));
        driver.setLastName(resultSet.getString("last_name"));
        driver.setTelephone(resultSet.getString("telephone"));
        driver.setRating(resultSet.getLong("rating"));
        return driver;
    }
}
