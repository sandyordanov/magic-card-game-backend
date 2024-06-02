package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.domain.Card;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<GetAllCardsResponse> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
    @GetMapping("{id}")
    public ResponseEntity<Card> getCard(@PathVariable(value = "id") final Long id) {
        var result = cardService.getById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/search")
    public ResponseEntity<GetAllCardsResponse> searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minHealthPoints,
            @RequestParam(required = false) Integer maxHealthPoints,
            @RequestParam(required = false) Integer minAttackPoints,
            @RequestParam(required = false) Integer maxAttackPoints) {
        return ResponseEntity.ok(cardService.searchCards(name, minHealthPoints, maxHealthPoints, minAttackPoints, maxAttackPoints));
    }

    @PostMapping()
    @RolesAllowed({"ADMIN"})
    public Card createCard(@RequestBody Card card) {
        return cardService.save(card);
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Card> updateCard(@PathVariable("id") Long id, @RequestBody Card updatedCard) {
        updatedCard.setId(id);
        Card result = cardService.updateCard(updatedCard);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{cardId}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteById(cardId);
        return ResponseEntity.noContent().build();
    }
}

