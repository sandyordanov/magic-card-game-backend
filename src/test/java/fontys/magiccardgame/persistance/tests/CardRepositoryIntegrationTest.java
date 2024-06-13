package fontys.magiccardgame.persistance.tests;

import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class CardRepositoryIntegrationTest {

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

    private CardEntity card;

    @BeforeEach
     void setUp() {
        card = CardEntity.builder()
                .name("Fireball")
                .attackPoints(10)
                .healthPoints(5)
                .build();
    }

    @Test
     void testSaveCard() {
        CardEntity savedCard = cardRepository.save(card);
        assertThat(savedCard).isNotNull();
        assertEquals(savedCard,card);
    }

    @Test
     void testFindCardById() {
        CardEntity savedCard = cardRepository.save(card);
        Optional<CardEntity> foundCard = cardRepository.findById(savedCard.getId());
        assertThat(foundCard).isPresent();
        assertThat(foundCard.get().getName()).isEqualTo(card.getName());
        assertThat(foundCard.get().getAttackPoints()).isEqualTo(card.getAttackPoints());
        assertThat(foundCard.get().getHealthPoints()).isEqualTo(card.getHealthPoints());
    }

    @Test
     void testUpdateCard() {
        CardEntity savedCard = cardRepository.save(card);
        savedCard.setAttackPoints(15);
        savedCard.setName("newName");
        CardEntity updatedCard = cardRepository.save(savedCard);
        assertThat(updatedCard.getAttackPoints()).isEqualTo(15);
        assertThat(updatedCard.getName()).isEqualTo("newName");
    }

    @Test
     void testDeleteCard() {
        CardEntity savedCard = cardRepository.save(card);
        cardRepository.deleteById(savedCard.getId());
        Optional<CardEntity> foundCard = cardRepository.findById(savedCard.getId());
        assertThat(foundCard).isNotPresent();
    }
}