package by.belstu.deliverty.repository;

import by.belstu.deliverty.dto.NotificationsDTO;
import by.belstu.deliverty.dto.OrderDTO;
import by.belstu.deliverty.dto.OrderItemsDTO;
import by.belstu.deliverty.dto.UserDTO;
import by.belstu.deliverty.mapper.NotificationsMapper;
import by.belstu.deliverty.mapper.OrderItemsMapper;
import by.belstu.deliverty.mapper.UserDataMapper;
import by.belstu.deliverty.mapper.UserOrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void fulfillUserData(String username, String firstName, String lastName, String telephone) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fulfill_user_data")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("first_name_in", firstName)
                .addValue("last_name_in", lastName)
                .addValue("telephone_in", telephone);
        jdbcCall.execute(params);
    }

    public UserDTO getUserData(String username) throws SQLException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_user_data")
                .withCatalogName("user_package")
                .returningResultSet("user", new UserDataMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username);
        List user = jdbcCall.executeFunction(List.class, params);
        if (user.size() == 0) {
            throw new SQLException("User not found");
        } else {
            return (UserDTO) user.get(0);
        }
    }

    public void addItemToOrder(Long orderIn, String itemName, Long itemWeight, Long itemVolume) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("add_item_to_order")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderIn)
                .addValue("item_name_in", itemName)
                .addValue("item_weight_in", itemWeight)
                .addValue("item_volume_in", itemVolume);
        jdbcCall.execute(params);
    }

    public void createOrder(String username, String startPoint, String endPoint) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("create_order")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("start_point_in", startPoint)
                .addValue("end_point_in", endPoint);
        jdbcCall.execute(params);
    }

    public List<OrderDTO> getUserOrders(String username) throws SQLException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_user_orders")
                .withCatalogName("user_package")
                .returningResultSet("orders", new UserOrdersMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username);
        List orders = jdbcCall.executeFunction(List.class, params);
        if (orders.size() == 0) {
            throw new SQLException("User not found");
        } else {
            return orders;
        }
    }

    public List<String> getCountries() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_countries")
                .withCatalogName("user_package")
                .returningResultSet("countries", (resultSet, i) -> resultSet.getString("name"));
        return jdbcCall.executeFunction(List.class);
    }

    public List<String> getCities(String country) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_cities")
                .withCatalogName("user_package")
                .returningResultSet("cities", (resultSet, i) -> resultSet.getString("name"));
        SqlParameterSource params = new MapSqlParameterSource().addValue("country_name_in", country);
        return jdbcCall.executeFunction(List.class, params);
    }

    public List<OrderItemsDTO> getOrderItems(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_order_items")
                .withCatalogName("user_package")
                .returningResultSet("items", new OrderItemsMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        return jdbcCall.executeFunction(List.class, params);
    }

    public void rateDriver(Long orderId, Long rating, String message) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("rate_driver")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId)
                .addValue("rating_in", rating)
                .addValue("message_in", message);
        jdbcCall.execute(params);
    }

    public List<NotificationsDTO> getNotifications(String username) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_user_notifications")
                .withCatalogName("user_package")
                .returningResultSet("notifications", new NotificationsMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username);
        return jdbcCall.executeFunction(List.class, params);
    }

    public void removeNotification(Long notificationId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("remove_notification")
                .withCatalogName("user_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("notification_id_in", notificationId);
        jdbcCall.execute(params);
    }

    public String getOrderStatus(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_order_status")
                .withCatalogName("user_package")
                .returningResultSet("status", (resultSet, i) -> resultSet.getString("status"));
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        return (String) jdbcCall.executeFunction(List.class, params).get(0);
    }
}