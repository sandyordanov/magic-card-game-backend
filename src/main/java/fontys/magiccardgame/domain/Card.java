package fontys.magiccardgame.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Card {

    private int id;
    private String name;
    private int hp;
    private int ap;

}
