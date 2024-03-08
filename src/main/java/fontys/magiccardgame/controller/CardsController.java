package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.CardManager;
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
    private final CardManager cardManager;

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
    public ResponseEntity<Void> createCard(@RequestBody Card card) {
        cardManager.save(card);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

