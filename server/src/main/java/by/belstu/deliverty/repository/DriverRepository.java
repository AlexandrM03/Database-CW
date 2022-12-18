package by.belstu.deliverty.repository;

import by.belstu.deliverty.dto.AllDriverOrdersDTO;
import by.belstu.deliverty.dto.DriverDTO;
import by.belstu.deliverty.dto.DriverOrderDTO;
import by.belstu.deliverty.dto.OrderItemsDTO;
import by.belstu.deliverty.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class DriverRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DriverRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DriverDTO loginDriver(String username, String password) throws SQLException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("login_driver")
                .withCatalogName("driver_package")
                .returningResultSet("driver", new DriverLoginMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("password", password);
        List driver = jdbcCall.executeFunction(List.class, params);
        if (driver.size() == 0) {
            throw new SQLException("Driver not found");
        } else {
            return (DriverDTO) driver.get(0);
        }
    }

    public void fulfillDriverData(String username, String firstName, String lastName, String telephone) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fulfill_driver_data")
                .withCatalogName("driver_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("first_name_in", firstName)
                .addValue("last_name_in", lastName)
                .addValue("telephone_in", telephone);
        jdbcCall.execute(params);
    }

    public List<DriverOrderDTO> getAcceptedOrders() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_accepted_orders")
                .withCatalogName("driver_package")
                .returningResultSet("orders", new DriverOrderMapper());
        return jdbcCall.executeFunction(List.class);
    }

    public List<OrderItemsDTO> getOrderItems(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_order_items")
                .withCatalogName("driver_package")
                .returningResultSet("items", new OrderItemsMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        return jdbcCall.executeFunction(List.class, params);
    }

    public DriverDTO getDriverData(String username) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_driver_data")
                .withCatalogName("driver_package")
                .returningResultSet("driver", new DriverMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", username);
        return (DriverDTO) jdbcCall.executeFunction(List.class, params).get(0);
    }

    public void startOrder(Long orderId, String username) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("start_order")
                .withCatalogName("driver_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId)
                .addValue("driver_username_in", username);
        jdbcCall.execute(params);
    }

    public void finishOrder(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("finish_order")
                .withCatalogName("driver_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        jdbcCall.execute(params);
    }

    public List<AllDriverOrdersDTO> getDriverOrders(String username) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_driver_orders")
                .withCatalogName("driver_package")
                .returningResultSet("orders", new AllDriverOrdersMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", username);
        return jdbcCall.executeFunction(List.class, params);
    }

    public void updateDriverPassword(String username, String password) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("update_driver_password")
                .withCatalogName("driver_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", username)
                .addValue("new_password", password);
        jdbcCall.execute(params);
    }
}
