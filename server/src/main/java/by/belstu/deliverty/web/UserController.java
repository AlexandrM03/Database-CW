package by.belstu.deliverty.web;

import by.belstu.deliverty.payload.*;
import by.belstu.deliverty.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/fulfill")
    public ResponseEntity<?> fulfillUserData(@RequestBody FulfillRequest fulfillRequest,
                                             @RequestHeader("Authorization") String username) {
        try {
            userService.fulfillUserData(username, fulfillRequest);
            return ResponseEntity.ok(new MessageResponse("User data fulfilled successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String username) {
        try {
            return ResponseEntity.ok(userService.getUserData(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToOrder(@RequestBody OrderItemRequest orderItemRequest) {
        try {
            userService.addItemToOrder(orderItemRequest);
            return ResponseEntity.ok(new MessageResponse("Item added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest,
                                         @RequestHeader("Authorization") String username) {
        try {
            userService.createOrder(username, createOrderRequest);
            return ResponseEntity.ok(new MessageResponse("Order created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-orders")
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String username) {
        try {
            return ResponseEntity.ok(userService.getUserOrders(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-countries")
    public ResponseEntity<?> getCountries() {
        try {
            return ResponseEntity.ok(userService.getCountries());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-cities/{country}")
    public ResponseEntity<?> getCities(@PathVariable String country) {
        try {
            return ResponseEntity.ok(userService.getCities(country));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-order-items/{orderId}")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(userService.getOrderItems(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/rate-driver")
    public ResponseEntity<?> rateDriver(@RequestBody RateRequest rateRequest) {
        try {
            userService.rateDriver(rateRequest);
            return ResponseEntity.ok(new MessageResponse("Driver rated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-notifications")
    public ResponseEntity<?> getUserNotifications(@RequestHeader("Authorization") String username) {
        try {
            return ResponseEntity.ok(userService.getUserNotifications(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove-notification/{notificationId}")
    public ResponseEntity<?> removeNotification(@PathVariable Long notificationId) {
        try {
            userService.removeNotification(notificationId);
            return ResponseEntity.ok(new MessageResponse("Notification removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-order-status/{orderId}")
    public ResponseEntity<?> getOrderStatus(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(new MessageResponse(userService.getOrderStatus(orderId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
