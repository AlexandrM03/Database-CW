package by.belstu.deliverty.repository;

import by.belstu.deliverty.dto.UserDTO;
import by.belstu.deliverty.mapper.UserLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerUser(String username, String password) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("register_user")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("password", password);
        jdbcCall.execute(params);
    }

    public UserDTO loginUser(String username, String password) throws SQLException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("login_user")
                .withCatalogName("user_package")
                .returningResultSet("user", new UserLoginMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("password", password);
        List user = jdbcCall.executeFunction(List.class, params);
        if (user.size() == 0) {
            throw new SQLException("User not found");
        } else {
            return (UserDTO) user.get(0);
        }
    }
}
