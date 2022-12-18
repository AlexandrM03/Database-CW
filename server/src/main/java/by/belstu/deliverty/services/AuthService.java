package by.belstu.deliverty.services;

import by.belstu.deliverty.dto.UserDTO;
import by.belstu.deliverty.payload.SigninRequest;
import by.belstu.deliverty.payload.SignupRequest;
import by.belstu.deliverty.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void registerUser(SignupRequest userIn) {
        authRepository.registerUser(userIn.getUsername(), userIn.getPassword());
    }

    public UserDTO loginUser(SigninRequest userIn) throws Exception {
        return authRepository.loginUser(userIn.getUsername(), userIn.getPassword());
    }
}
