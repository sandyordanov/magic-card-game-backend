package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.domain.Card;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/decks")
@AllArgsConstructor
public class DeckController {
    private final DeckService deckService;
    @PutMapping
    public void addCardToDeck(@RequestBody AddCardToDeckRequest request) {
       deckService.addCard(request);
    }
}
