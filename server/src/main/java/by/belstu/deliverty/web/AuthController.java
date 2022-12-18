package by.belstu.deliverty.web;

import by.belstu.deliverty.dto.UserDTO;
import by.belstu.deliverty.payload.MessageResponse;
import by.belstu.deliverty.payload.SigninRequest;
import by.belstu.deliverty.payload.SignupRequest;
import by.belstu.deliverty.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SigninRequest signinRequest,
                                              HttpServletResponse response,
                                              HttpServletRequest request) {
        try {
            UserDTO user = authService.loginUser(signinRequest);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        cookie.setValue(user.getUsername());
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }
            }
            Cookie cookie = new Cookie("username", user.getUsername());
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            authService.registerUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
