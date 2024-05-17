package fontys.magiccardgame.business.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCardToDeckRequest {
    private Long playerId;
    private Long cardId;
}
