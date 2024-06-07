//package fontys.magiccardgame.persistance.tests;
//
//import fontys.magiccardgame.persistence.CardRepository;
//import fontys.magiccardgame.persistence.entity.CardEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//
//@DataJpaTest
// class CardRepositoryIntegrationTest {
//
//    @Autowired
//    private CardRepository cardRepository;
//
//    private CardEntity card;
//
//    @BeforeEach
//     void setUp() {
//        card = CardEntity.builder()
//                .name("Fireball")
//                .attackPoints(10)
//                .healthPoints(5)
//                .build();
//    }
//
//    @Test
//     void testSaveCard() {
//        CardEntity savedCard = cardRepository.save(card);
//        assertThat(savedCard).isNotNull();
//        assertThat(savedCard.getId()).isNotNull();
//    }
//
//    @Test
//     void testFindCardById() {
//        CardEntity savedCard = cardRepository.save(card);
//        Optional<CardEntity> foundCard = cardRepository.findById(savedCard.getId());
//        assertThat(foundCard).isPresent();
//        assertThat(foundCard.get().getName()).isEqualTo(card.getName());
//    }
//
//    @Test
//     void testUpdateCard() {
//        CardEntity savedCard = cardRepository.save(card);
//        savedCard.setAttackPoints(15);
//        CardEntity updatedCard = cardRepository.save(savedCard);
//        assertThat(updatedCard.getAttackPoints()).isEqualTo(15);
//    }
//
//    @Test
//     void testDeleteCard() {
//        CardEntity savedCard = cardRepository.save(card);
//        cardRepository.deleteById(savedCard.getId());
//        Optional<CardEntity> foundCard = cardRepository.findById(savedCard.getId());
//        assertThat(foundCard).isNotPresent();
//    }
//}