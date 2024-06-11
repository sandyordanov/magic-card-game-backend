package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.GetDeckResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/decks")
@AllArgsConstructor
public class DeckController {
    private final DeckService deckService;

    @RolesAllowed({"PLAYER"})
    @GetMapping({"{deckId}"})
    public GetDeckResponse getDeck(@PathVariable Long deckId) {
        return deckService.getDeck(deckId);
    }
    @RolesAllowed({"PLAYER"})
    @GetMapping("/{deckId}/average-stats")
    public Map<String, Double> getAverageHealthAndAttackPointsByDeckId(@PathVariable Long deckId) {
        return deckService.getAverageHealthAndAttackPointsByDeck(deckId);
    }

    @RolesAllowed({"PLAYER"})
    @PutMapping({"/{deckId}/card/{cardId}"})
    public void addCardToDeck(@PathVariable Long deckId,@PathVariable Long cardId) {
        deckService.addCard(deckId,cardId);
    }

    @RolesAllowed({"PLAYER"})
    @DeleteMapping({"/{deckId}/card/{cardId}"})
    public ResponseEntity<Void> removeFromDeck(@PathVariable Long deckId,@PathVariable Long cardId) {
        boolean cardRemoved = deckService.removeCardFromDeck(deckId, cardId);
        if (cardRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
