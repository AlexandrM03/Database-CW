package by.belstu.deliverty.services;

import by.belstu.deliverty.dto.AllDriverOrdersDTO;
import by.belstu.deliverty.dto.DriverDTO;
import by.belstu.deliverty.dto.DriverOrderDTO;
import by.belstu.deliverty.dto.OrderItemsDTO;
import by.belstu.deliverty.payload.FulfillRequest;
import by.belstu.deliverty.payload.SigninRequest;
import by.belstu.deliverty.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverDTO loginDriver(SigninRequest signinRequest) throws SQLException {
        return driverRepository.loginDriver(signinRequest.getUsername(), signinRequest.getPassword());
    }

    public void fulfillDriverData(String username, FulfillRequest fulfillRequest) {
        driverRepository.fulfillDriverData(username,
                fulfillRequest.getFirstName(),
                fulfillRequest.getLastName(),
                fulfillRequest.getTelephone());
    }

    public List<DriverOrderDTO> getAcceptedOrders() {
        return driverRepository.getAcceptedOrders();
    }

    public List<OrderItemsDTO> getOrderItems(Long orderId) {
        return driverRepository.getOrderItems(orderId);
    }

    public DriverDTO getDriverData(String username) {
        return driverRepository.getDriverData(username);
    }

    public void startOrder(Long orderId, String username) {
        driverRepository.startOrder(orderId, username);
    }

    public void finishOrder(Long orderId) {
        driverRepository.finishOrder(orderId);
    }

    public List<AllDriverOrdersDTO> getDriverOrders(String username) {
        return driverRepository.getDriverOrders(username);
    }

    public void updateDriverPassword(String username, String password) {
        driverRepository.updateDriverPassword(username, password);
    }
}
