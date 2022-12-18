package by.belstu.deliverty.services;

import by.belstu.deliverty.dto.*;
import by.belstu.deliverty.payload.SignupRequest;
import by.belstu.deliverty.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void updateOrderPrice(Long orderId) {
        adminRepository.updateOrderPrice(orderId);
    }

    public void acceptOrder(Long orderId) {
        adminRepository.acceptOrder(orderId);
    }

    public void hireDriver(SignupRequest signupRequest) {
        adminRepository.hireDriver(signupRequest.getUsername(), signupRequest.getPassword());
    }

    public List<DriverDTO> getAllDrivers() {
        return adminRepository.getAllDrivers();
    }

    public List<UserDTO> getAllUsers() {
        return adminRepository.getAllUsers();
    }

    public List<AdminOrderDTO> getAllOrders() {
        return adminRepository.getAllOrders();
    }

    public List<AdminOrderDTO> getAllOrdersByDriver(String driverUsername) {
        return adminRepository.getAllOrdersByDriver(driverUsername);
    }

    public List<AdminOrderDTO> getAllOrdersByUser(String username) {
        return adminRepository.getAllOrdersByUser(username);
    }

    public List<OrderItemsDTO> getAllOrderItems(Long orderId) {
        return adminRepository.getAllOrderItems(orderId);
    }

    public void rejectOrder(Long orderId) {
        adminRepository.rejectOrder(orderId);
    }

    public List<NotificationsDTO> getAdminNotifications() {
        return adminRepository.getAdminNotifications();
    }

    public void removeNotification(Long notificationId) {
        adminRepository.removeNotification(notificationId);
    }

    public void confirmDriverRating(String driverUsername) {
        adminRepository.confirmDriverRating(driverUsername);
    }

    public void removeDriverRating(Long ratingId) {
        adminRepository.removeDriverRating(ratingId);
    }

    public List<RatingDTO> getDriverRating(String driverUsername) {
        return adminRepository.getDriverRating(driverUsername);
    }

    public List<UserDTO> getAllUsersPagination(Long page) {
        return adminRepository.getAllUsersPagination(page);
    }
}
