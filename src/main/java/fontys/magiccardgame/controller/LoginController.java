package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.LoginService;
import fontys.magiccardgame.business.dto.LoginRequest;
import fontys.magiccardgame.business.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }
}
