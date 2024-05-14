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
    private Long id;

    @NotNull(message = "Name is mandatory.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Attack points are mandatory.")
    @Column(name = "attack_Points")
    private int attackPoints;

    @NotNull(message = "Health points are mandatory.")
    @Column(name = "health_Points")
    private int healthPoints;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "ownedCards")
    private List<PlayerEntity> owners;
}
