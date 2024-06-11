package fontys.magiccardgame.business.converters;

import fontys.magiccardgame.domain.Deck;
import fontys.magiccardgame.persistence.entity.DeckEntity;

public final class DeckConverter {
    private DeckConverter(){}
    public static Deck convert(DeckEntity entity) {
        return Deck.builder()
                .id(entity.getId())
                .playerId(entity.getPlayer().getId())
                .cards(entity.getCards().stream().map(CardConverter::convert).toList())
                .build();
    }
}
