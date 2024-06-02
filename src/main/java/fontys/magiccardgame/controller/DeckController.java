package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.dto.GetDeckResponse;
import fontys.magiccardgame.domain.Card;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
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
        if (deckService.removeCardFromDeck(deckId,cardId)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
