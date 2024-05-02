package fontys.magiccardgame.controller;
import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController

@RequestMapping("/cards")
@AllArgsConstructor
public class CardController {
    private final CardService cardManager;

    @GetMapping
    public ResponseEntity<GetAllCardsResponse> getAllCards() {
        return ResponseEntity.ok(cardManager.getAllCards());
    }

    @GetMapping("{id}")
    public ResponseEntity<Card> getCard(@PathVariable(value = "id") final int id) {
        var result = cardManager.getById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public CardEntity createCard(@RequestBody CardEntity card) {
      var result = cardManager.save(card);
        return result;
    }

    @PutMapping("{id}")
    public ResponseEntity<Card> updateCard(@PathVariable("id") int id, @RequestBody Card updatedCard) {
        updatedCard.setId(id);
        Card result = cardManager.updateCard(updatedCard);
        if (result!=null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("{cardId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int cardId) {
        cardManager.deleteById(cardId);
        return ResponseEntity.noContent().build();
    }
}

