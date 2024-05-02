package fontys.magiccardgame.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private int id;
    private String name;
    private int hp;
    private int ap;

}
