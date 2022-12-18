package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.DriverOrderDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverOrderMapper implements RowMapper<DriverOrderDTO> {

    @Override
    public DriverOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        DriverOrderDTO order = new DriverOrderDTO();
        order.setId(rs.getLong("id"));
        order.setStartPoint(rs.getString("start_point"));
        order.setEndPoint(rs.getString("end_point"));
        order.setPrice(rs.getDouble("price"));
        order.setStartDate(rs.getDate("start_date"));
        order.setStatus(rs.getString("order_status"));
        order.setUsername(rs.getString("username"));
        return order;
    }
}
