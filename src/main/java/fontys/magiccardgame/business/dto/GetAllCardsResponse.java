package fontys.magiccardgame.business.dto;

import fontys.magiccardgame.domain.Card;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllCardsResponse {
    private List<Card> cards;
}
