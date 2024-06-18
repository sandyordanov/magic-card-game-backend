package fontys.magiccardgame.domain;

import lombok.*;

import java.util.Deque;
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
    private List<Card> hand;
    private Deck deck;
    private Deque<Card> deckStack;
    private Long userId;
    private Integer hp;
}
