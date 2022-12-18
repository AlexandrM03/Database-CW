package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.DriverDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverLoginMapper implements RowMapper<DriverDTO> {
    @Override
    public DriverDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        DriverDTO driver = new DriverDTO();
        driver.setUsername(rs.getString("username"));
        return driver;
    }
}
