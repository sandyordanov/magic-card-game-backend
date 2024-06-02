package fontys.magiccardgame.domain;

import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Player {
    private Long id;
    private String name;
    private List<Card> ownedCards;
    private Deck deck;
    private Long userId;
}
