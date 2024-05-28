package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.domain.Card;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/decks")
@AllArgsConstructor
public class DeckController {
    private final DeckService deckService;
    @RolesAllowed({"PLAYER"})
    @PutMapping({"/add/{userId}/card/{cardId}"})
    public void addCardToDeck(@PathVariable Long userId,@PathVariable Long cardId) {
        deckService.addCard(userId,cardId);
    }
    @DeleteMapping({"/remove/{userId}/card/{cardId}"})
    public ResponseEntity<Void> removeFromDeck(@PathVariable Long userId,@PathVariable Long cardId) {
        if (deckService.removeCardFromDeck(userId,cardId)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping({"{userId}"})
    public GetAllCardsResponse getDeck(@PathVariable Long userId) {
        return deckService.getDeck(userId);
    }

    @GetMapping({"/ownedCards/{id}"})
    public GetAllCardsResponse getOwnedCards(@PathVariable Long id) {
        return deckService.getOwnedCards(id);
    }


}
