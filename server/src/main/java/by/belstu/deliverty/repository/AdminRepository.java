package by.belstu.deliverty.repository;

import by.belstu.deliverty.dto.*;
import by.belstu.deliverty.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateOrderPrice(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("update_order_price")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        jdbcCall.execute(params);
    }

    public void acceptOrder(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("accept_order")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        jdbcCall.execute(params);
    }

    public void hireDriver(String username, String password) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("hire_driver")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username)
                .addValue("driver_password", password);
        jdbcCall.execute(params);
    }

    public List<UserDTO> getAllUsers() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_users")
                .withCatalogName("admin_package")
                .returningResultSet("users", new UserDataMapper());
        return jdbcCall.executeFunction(List.class);
    }

    public List<DriverDTO> getAllDrivers() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_drivers")
                .withCatalogName("admin_package")
                .returningResultSet("drivers", new DriverMapper());
        return jdbcCall.executeFunction(List.class);
    }

    public List<AdminOrderDTO> getAllOrders() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_orders")
                .withCatalogName("admin_package")
                .returningResultSet("orders", new AdminOrderMapper());
        return jdbcCall.executeFunction(List.class);
    }

    public List<AdminOrderDTO> getAllOrdersByDriver(String driverUsername) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_orders_by_driver")
                .withCatalogName("admin_package")
                .returningResultSet("orders", new AdminOrderMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", driverUsername);
        return jdbcCall.executeFunction(List.class, params);
    }

    public List<AdminOrderDTO> getAllOrdersByUser(String username) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_orders_by_user")
                .withCatalogName("admin_package")
                .returningResultSet("orders", new AdminOrderMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("username_in", username);
        return jdbcCall.executeFunction(List.class, params);
    }

    public List<OrderItemsDTO> getAllOrderItems(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_order_items")
                .withCatalogName("admin_package")
                .returningResultSet("order_items", new OrderItemsMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        return jdbcCall.executeFunction(List.class, params);
    }

    public void rejectOrder(Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("reject_order")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("order_id_in", orderId);
        jdbcCall.execute(params);
    }

    public List<NotificationsDTO> getAdminNotifications() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_admin_notifications")
                .withCatalogName("admin_package")
                .returningResultSet("notifications", new NotificationsMapper());
        return jdbcCall.executeFunction(List.class);
    }

    public void removeNotification(Long notificationId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("remove_admin_notification")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("notification_id_in", notificationId);
        jdbcCall.execute(params);
    }

    public void confirmDriverRating(String driverUsername) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("confirm_driver_rating")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", driverUsername);
        jdbcCall.execute(params);
    }

    public void removeDriverRating(Long ratingId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("remove_driver_rating")
                .withCatalogName("admin_package");
        SqlParameterSource params = new MapSqlParameterSource().addValue("rating_id_in", ratingId);
        jdbcCall.execute(params);
    }

    public List<RatingDTO> getDriverRating(String driverUsername) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_driver_rating")
                .withCatalogName("admin_package")
                .returningResultSet("ratings", new RatingMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("driver_username_in", driverUsername);
        return jdbcCall.executeFunction(List.class, params);
    }

    public List<UserDTO> getAllUsersPagination(Long page) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("get_all_users_pagination")
                .withCatalogName("admin_package")
                .returningResultSet("users", new UserDataMapper());
        SqlParameterSource params = new MapSqlParameterSource().addValue("page_number_in", page)
                .addValue("page_size_in", 15);
        return jdbcCall.executeFunction(List.class, params);
    }
}
