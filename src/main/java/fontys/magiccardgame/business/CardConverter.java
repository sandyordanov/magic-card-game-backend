package fontys.magiccardgame.business;

import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.entity.CardEntity;

final class CardConverter {

    private CardConverter(){}

    public static Card convert(CardEntity cardEntity) {
        return Card.builder()
                .id(cardEntity.getId())
                .name(cardEntity.getName())
                .ap(cardEntity.getAttackPoints())
                .hp(cardEntity.getHealthPoints())
                .build();
    }
}
