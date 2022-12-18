package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.AdminOrderDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminOrderMapper implements RowMapper<AdminOrderDTO> {
    @Override
    public AdminOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdminOrderDTO order = new AdminOrderDTO();
        order.setId(rs.getLong("id"));
        order.setStartPoint(rs.getString("start_point"));
        order.setEndPoint(rs.getString("end_point"));
        order.setPrice(rs.getDouble("price"));
        order.setStartDate(rs.getDate("start_date"));
        order.setEndDate(rs.getDate("end_date"));
        order.setStatus(rs.getString("order_status"));
        order.setUsername(rs.getString("username"));
        order.setDriverUsername(rs.getString("driver_username"));
        return order;
    }
}
