package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.impl.CardService;
import fontys.magiccardgame.models.Card;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/cards")
@AllArgsConstructor
public class CardsController {
    private final CardService cardManager;

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardManager.getAllCards());
    }

    @GetMapping("{id}")
    public ResponseEntity<Card> getCard(@PathVariable(value = "id") final long id) {
        var result = cardManager.getById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public Card createCard(@RequestBody Card card) {
      var result = cardManager.save(card);
        return result;
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCard(@PathVariable("id") int id, @RequestBody Card updatedCard) {
        updatedCard.setId(id);
        boolean result = cardManager.updateCard(updatedCard);
        if (result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long cardId) {
        cardManager.deleteById(cardId);
        return ResponseEntity.noContent().build();
    }
}

