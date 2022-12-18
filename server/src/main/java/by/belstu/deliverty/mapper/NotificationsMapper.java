package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.NotificationsDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationsMapper implements RowMapper<NotificationsDTO> {
    @Override
    public NotificationsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        NotificationsDTO notificationsDTO = new NotificationsDTO();
        notificationsDTO.setId(rs.getLong("id"));
        notificationsDTO.setOperationDate(rs.getDate("operation_date"));
        notificationsDTO.setMessage(rs.getString("message"));
        return notificationsDTO;
    }
}
