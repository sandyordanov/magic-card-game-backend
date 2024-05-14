package fontys.magiccardgame.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private Long id;
    private String name;
    private int healthPoints;
    private int attackPoints;

}
