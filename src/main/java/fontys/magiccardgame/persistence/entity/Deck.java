package fontys.magiccardgame.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "deck")
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Deck {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Player player;

    @ManyToMany
    private List<Card> cards;


}
