package fontys.magiccardgame.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "deck")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeckEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "deck")
    private PlayerEntity player;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<CardEntity> cards;


}
