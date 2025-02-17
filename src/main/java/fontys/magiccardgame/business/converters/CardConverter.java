package fontys.magiccardgame.business.converters;

import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.entity.CardEntity;

public final class CardConverter {

    private CardConverter(){}
    public static Card convert(CardEntity cardEntity) {
        return Card.builder()
                .id(cardEntity.getId())
                .name(cardEntity.getName())
                .attackPoints(cardEntity.getAttackPoints())
                .healthPoints(cardEntity.getHealthPoints())
                .build();
    }
}
