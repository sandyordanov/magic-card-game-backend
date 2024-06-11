package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.PlayerService;
import fontys.magiccardgame.domain.Player;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/players"})
@AllArgsConstructor
public class PlayerController {
    private PlayerService playerService;
    @GetMapping("{userId}")
    @RolesAllowed({"ADMIN", "PLAYER"})
    public ResponseEntity<Player> getPlayer(@PathVariable Long userId) {
        var result = playerService.getPlayer(userId);
        return ResponseEntity.ok(result);
    }
    @GetMapping()
    @RolesAllowed({"ADMIN", "PLAYER"})
    public ResponseEntity<List<Player>> getPlayer() {
        var result = playerService.getAllPlayers();
        return ResponseEntity.ok(result);
    }
}
