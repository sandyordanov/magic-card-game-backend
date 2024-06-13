package fontys.magiccardgame.persistance.tests;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class DeckRepositoryTests {
    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository;
    private CardEntity card;
    private DeckEntity deck;
    private PlayerEntity player;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        card = CardEntity.builder()
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        cardRepository.save(card);
        UserEntity user = UserEntity.builder().username("asd").password("asdads").role(RoleEnum.ADMIN).build();
        userRepository.save(user);
        player = PlayerEntity.builder()
                .name("Player1")
                .ownedCards(List.of(card))
                .user(user)
                .build();

        playerRepository.save(player);

        deck = DeckEntity.builder()
                .cards(List.of(card))
                .player(player)
                .build();

        deckRepository.save(deck);
    }

    @Test
    void testFindAverageHealthAndAttackPointsByDeckId() {
        List<Object[]> result = deckRepository.findAverageHealthAndAttackPointsByDeckId(deck.getId());
        assertThat(result).hasSize(1);
        Object[] averages = result.get(0);
        assertThat(averages[0]).isEqualTo(25.0); // Average health points
        assertThat(averages[1]).isEqualTo(15.0); // Average attack points
    }

    @Test
    void testFindById() {
        DeckEntity foundDeck = deckRepository.findById(deck.getId()).orElse(null);
        assertThat(foundDeck).isNotNull();
        assertThat(foundDeck.getCards()).contains(card);
    }

    @Test
    void testDeleteDeck() {
        deckRepository.deleteById(deck.getId());
        DeckEntity foundDeck = deckRepository.findById(deck.getId()).orElse(null);
        assertThat(foundDeck).isNull();
    }
}
