package fontys.magiccardgame.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayCardRequest {
    private long gameId;
    private long playerId;
    private Card card;
}
