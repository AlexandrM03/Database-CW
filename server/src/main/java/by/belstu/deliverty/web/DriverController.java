package by.belstu.deliverty.web;

import by.belstu.deliverty.payload.FulfillRequest;
import by.belstu.deliverty.payload.MessageResponse;
import by.belstu.deliverty.payload.SigninRequest;
import by.belstu.deliverty.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @PostMapping("/login")
    public ResponseEntity<?> loginDriver(@RequestBody SigninRequest signinRequest) {
        try {
            return ResponseEntity.ok(driverService.loginDriver(signinRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/fulfill")
    public ResponseEntity<?> fulfillDriverData(@RequestBody FulfillRequest fulfillRequest,
                                               @RequestHeader("Authorization") String username) {
        try {
            driverService.fulfillDriverData(username, fulfillRequest);
            return ResponseEntity.ok(new MessageResponse("Driver data fulfilled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accepted-orders")
    public ResponseEntity<?> getAcceptedOrders() {
        try {
            return ResponseEntity.ok(driverService.getAcceptedOrders());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order-items/{orderId}")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(driverService.getOrderItems(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/driver-data")
    public ResponseEntity<?> getDriverData(@RequestHeader("Authorization") String username) {
        try {
            return ResponseEntity.ok(driverService.getDriverData(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/start-order/{orderId}")
    public ResponseEntity<?> startOrder(@PathVariable Long orderId,
                                        @RequestHeader("Authorization") String username) {
        try {
            driverService.startOrder(orderId, username);
            return ResponseEntity.ok(new MessageResponse("Order started successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/finish-order/{orderId}")
    public ResponseEntity<?> finishOrder(@PathVariable Long orderId) {
        try {
            driverService.finishOrder(orderId);
            return ResponseEntity.ok(new MessageResponse("Order finished successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/driver-orders")
    public ResponseEntity<?> getDriverOrders(@RequestHeader("Authorization") String username) {
        try {
            return ResponseEntity.ok(driverService.getDriverOrders(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/update-password/{newPassword}")
    public ResponseEntity<?> updatePassword(@PathVariable String newPassword,
                                            @RequestHeader("Authorization") String username) {
        try {
            driverService.updateDriverPassword(username, newPassword);
            return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
