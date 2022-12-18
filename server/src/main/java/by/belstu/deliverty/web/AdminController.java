package by.belstu.deliverty.web;

import by.belstu.deliverty.payload.MessageResponse;
import by.belstu.deliverty.payload.SignupRequest;
import by.belstu.deliverty.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/update-order-price/{orderId}")
    public ResponseEntity<?> updateOrderPrice(@PathVariable Long orderId) {
        try {
            adminService.updateOrderPrice(orderId);
            return ResponseEntity.ok(new MessageResponse("Order price updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/accept-order/{orderId}")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId) {
        try {
            adminService.acceptOrder(orderId);
            return ResponseEntity.ok(new MessageResponse("Order accepted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/hire-driver")
    public ResponseEntity<?> hireDriver(@RequestBody SignupRequest signupRequest) {
        try {
            adminService.hireDriver(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Driver hired successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-drivers")
    public ResponseEntity<?> getAllDrivers() {
        try {
            return ResponseEntity.ok(adminService.getAllDrivers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(adminService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(adminService.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-orders-by-driver/{driverUsername}")
    public ResponseEntity<?> getAllOrdersByDriver(@PathVariable String driverUsername) {
        try {
            return ResponseEntity.ok(adminService.getAllOrdersByDriver(driverUsername));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-orders-by-user/{username}")
    public ResponseEntity<?> getAllOrdersByUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(adminService.getAllOrdersByUser(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-order-items/{orderId}")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(adminService.getAllOrderItems(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/reject-order/{orderId}")
    public ResponseEntity<?> rejectOrder(@PathVariable Long orderId) {
        try {
            adminService.rejectOrder(orderId);
            return ResponseEntity.ok(new MessageResponse("Order rejected successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-notifications")
    public ResponseEntity<?> getAllNotifications() {
        try {
            return ResponseEntity.ok(adminService.getAdminNotifications());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove-notification/{notificationId}")
    public ResponseEntity<?> removeNotification(@PathVariable Long notificationId) {
        try {
            adminService.removeNotification(notificationId);
            return ResponseEntity.ok(new MessageResponse("Notification removed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/confirm-driver-rating/{driverUsername}")
    public ResponseEntity<?> confirmDriverRating(@PathVariable String driverUsername) {
        try {
            adminService.confirmDriverRating(driverUsername);
            return ResponseEntity.ok(new MessageResponse("Driver rating confirmed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove-driver-rating/{ratingId}")
    public ResponseEntity<?> removeDriverRating(@PathVariable Long ratingId) {
        try {
            adminService.removeDriverRating(ratingId);
            return ResponseEntity.ok(new MessageResponse("Driver rating removed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-driver-rating/{driverUsername}")
    public ResponseEntity<?> getDriverRating(@PathVariable String driverUsername) {
        try {
            return ResponseEntity.ok(adminService.getDriverRating(driverUsername));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-users-pagination/{page}")
    public ResponseEntity<?> getAllUsersPagination(@PathVariable Long page) {
        try {
            return ResponseEntity.ok(adminService.getAllUsersPagination(page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
