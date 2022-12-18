package by.belstu.deliverty.mapper;

import by.belstu.deliverty.dto.RatingDTO;
import org.springframework.jdbc.core.RowMapper;

public class RatingMapper implements RowMapper<RatingDTO> {
    @Override
    public RatingDTO mapRow(java.sql.ResultSet resultSet, int i) throws java.sql.SQLException {
        RatingDTO rating = new RatingDTO();
        rating.setId(resultSet.getLong("id"));
        rating.setRating(resultSet.getLong("rating"));
        rating.setMessage(resultSet.getString("message"));
        return rating;
    }
}
