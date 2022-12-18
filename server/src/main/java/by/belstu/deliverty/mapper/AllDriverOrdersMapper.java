package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.AllDriverOrdersDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AllDriverOrdersMapper implements RowMapper<AllDriverOrdersDTO> {
    @Override
    public AllDriverOrdersDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        AllDriverOrdersDTO allDriverOrdersDTO = new AllDriverOrdersDTO();
        allDriverOrdersDTO.setId(rs.getLong("id"));
        allDriverOrdersDTO.setStartPoint(rs.getString("start_point"));
        allDriverOrdersDTO.setEndPoint(rs.getString("end_point"));
        allDriverOrdersDTO.setPrice(rs.getDouble("price"));
        allDriverOrdersDTO.setStartDate(rs.getDate("start_date"));
        allDriverOrdersDTO.setEndDate(rs.getDate("end_date"));
        allDriverOrdersDTO.setStatus(rs.getString("order_status"));
        return allDriverOrdersDTO;
    }
}
