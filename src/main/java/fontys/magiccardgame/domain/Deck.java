package fontys.magiccardgame.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Deck {
    private Long id;
    private Long playerId;
    private List<Card> cards;
}
