package fontys.magiccardgame.business;

import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.business.exception.DeckSizeLimitException;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeckService {

    private DeckRepository deckRepo;

    private CardRepository cardRepo;

    private PlayerRepository playerRepository;

    static final int DECK_SIZE = 3;
    public int getDeckSize() {
        return DECK_SIZE;
    }

    public void addCard(Long userId, Long cardId) {
        PlayerEntity player = playerRepository.findByUser_Id(userId);
        //  () -> new IllegalArgumentException("Cannot find player with the specified id."));
        CardEntity card = cardRepo.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));

        if (!checkDeckSize(player)) {
            throw new DeckSizeLimitException(DECK_SIZE);
        }
        if (player.getOwnedCards().stream().noneMatch(ownedCard -> ownedCard.getId().equals(card.getId()))) {
            throw new IllegalArgumentException("You do not have the given card in player's card collection");
        }

        player.getDeck().getCards().add(card);
        playerRepository.save(player);
    }
    public Boolean removeCardFromDeck(Long userId, Long cardId) {
        PlayerEntity player = playerRepository.findByUser_Id(userId);
        Boolean result = player.getDeck().getCards().removeIf(cardEntity -> cardEntity.getId() == cardId);
        playerRepository.save(player);
        return result;
    }

    private boolean checkDeckSize(PlayerEntity player) {
        int cardsInDeck = player.getDeck().getCards().size();
        return cardsInDeck <= DECK_SIZE;
    }

    public GetAllCardsResponse getDeck(Long userId) {
        return GetAllCardsResponse.builder()
                .cards(playerRepository.findByUser_Id(userId)
                        .getDeck().getCards()
                        .stream()
                        .map(CardConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public GetAllCardsResponse getOwnedCards(Long userId) {
        return GetAllCardsResponse.builder()
                .cards(playerRepository.findByUser_Id(userId)
                        .getOwnedCards()
                        .stream()
                        .map(CardConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}

