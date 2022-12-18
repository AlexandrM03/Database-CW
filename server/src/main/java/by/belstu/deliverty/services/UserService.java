package by.belstu.deliverty.services;

import by.belstu.deliverty.dto.NotificationsDTO;
import by.belstu.deliverty.dto.OrderDTO;
import by.belstu.deliverty.dto.OrderItemsDTO;
import by.belstu.deliverty.dto.UserDTO;
import by.belstu.deliverty.payload.CreateOrderRequest;
import by.belstu.deliverty.payload.FulfillRequest;
import by.belstu.deliverty.payload.OrderItemRequest;
import by.belstu.deliverty.payload.RateRequest;
import by.belstu.deliverty.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void fulfillUserData(String username, FulfillRequest fulfillRequest) {
        userRepository.fulfillUserData(username,
                fulfillRequest.getFirstName(),
                fulfillRequest.getLastName(),
                fulfillRequest.getTelephone());
    }

    public UserDTO getUserData(String username) throws SQLException {
        return userRepository.getUserData(username);
    }

    public void addItemToOrder(OrderItemRequest orderItemRequest) {
        userRepository.addItemToOrder(orderItemRequest.getOrderId(),
                orderItemRequest.getName(),
                orderItemRequest.getWeight(),
                orderItemRequest.getVolume());
    }

    public void createOrder(String username, CreateOrderRequest createOrderRequest) {
        userRepository.createOrder(username,
                createOrderRequest.getStartPoint(),
                createOrderRequest.getEndPoint());
    }

    public List<OrderDTO> getUserOrders(String username) throws SQLException {
        return userRepository.getUserOrders(username);
    }

    public List<String> getCountries() {
        return userRepository.getCountries();
    }

    public List<String> getCities(String country) {
        return userRepository.getCities(country);
    }

    public List<OrderItemsDTO> getOrderItems(Long orderId) {
        return userRepository.getOrderItems(orderId);
    }

    public void rateDriver(RateRequest rateRequest) {
        userRepository.rateDriver(rateRequest.getOrderId(), rateRequest.getRate(), rateRequest.getMessage());
    }

    public List<NotificationsDTO> getUserNotifications(String username) {
        return userRepository.getNotifications(username);
    }

    public void removeNotification(Long notificationId) {
        userRepository.removeNotification(notificationId);
    }

    public String getOrderStatus(Long orderId) {
        return userRepository.getOrderStatus(orderId);
    }
}

