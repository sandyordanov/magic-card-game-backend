package fontys.magiccardgame.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayCardRequest {
    private long gameId;
    private long userId;
    private Card card;
}
