package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.UserService;
import fontys.magiccardgame.business.dto.CreateUserRequest;
import fontys.magiccardgame.business.dto.GetAllUsersResponse;
import fontys.magiccardgame.business.dto.UpdateUserRequest;
import fontys.magiccardgame.domain.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    @GetMapping("{id}")
    @RolesAllowed({"PLAYER", "ADMIN"})
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        final Optional<User> user = userService.getUser(id);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
       return ResponseEntity.ok().build();
    }

    @PostMapping("/admin")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> createAdmin(@RequestBody CreateUserRequest request) {
        userService.createAdmin(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN", "PLAYER"})
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request) {
        userService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN", "PLAYER"})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
