package fontys.magiccardgame.persistence.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "card")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name is manditory.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Attack points are manditory.")
    @Column(name = "attackPoints")
    private int attackPoints;

    @NotNull(message = "Health points are manditory.")
    @Column(name = "healthPoints")
    private int healthPoints;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "ownedCards")
    private List<Player> owners;
}
