package fontys.magiccardgame.business.dto;

import fontys.magiccardgame.domain.Card;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetAllCardsResponse {
    private List<Card> cards;
}
