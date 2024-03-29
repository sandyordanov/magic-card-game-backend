package fontys.magiccardgame;

import fontys.magiccardgame.business.impl.CardService;
import fontys.magiccardgame.models.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig
@SpringBootTest
public class CardServiceTests {
    @Autowired
    CardService cardService;

    @Test
    void updateCard_ShouldReturnFalse_WhenInvalidCardId() {
        Card invalidCard = new Card(10, "name", 2, 3);

        var result = cardService.updateCard(invalidCard);
        assertFalse(result);
    }

    @Test
    void updateCard_ShouldReturnTrue_WhenCardExists() {
        Card invalidCard = new Card(1, "name", 2, 3);

        var result = cardService.updateCard(invalidCard);
        assertTrue(result);
    }
}
