package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.OrderDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserOrdersMapper implements RowMapper<OrderDTO> {
    @Override
    public OrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderDTO order = new OrderDTO();
        order.setId(rs.getLong("order_id"));
        order.setStartPoint(rs.getString("start_point"));
        order.setEndPoint(rs.getString("end_point"));
        order.setPrice(rs.getDouble("price"));
        order.setDriverLastName(rs.getString("driver_last_name"));
        order.setDriverFirstName(rs.getString("driver_first_name"));
        order.setDriverRating(rs.getDouble("driver_rating"));
        order.setStatus(rs.getString("order_status"));
        order.setStartDate(rs.getDate("start_date"));
        order.setEndDate(rs.getDate("end_date"));
        return order;
    }
}
