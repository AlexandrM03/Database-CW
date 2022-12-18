package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.OrderItemsDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemsMapper implements RowMapper<OrderItemsDTO> {
    @Override
    public OrderItemsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
        orderItemsDTO.setName(rs.getString("item_name"));
        orderItemsDTO.setWeight(rs.getDouble("item_weight"));
        orderItemsDTO.setVolume(rs.getDouble("item_volume"));
        return orderItemsDTO;
    }
}
