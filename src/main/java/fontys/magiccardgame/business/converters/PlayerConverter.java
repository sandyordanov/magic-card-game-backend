package fontys.magiccardgame.business.converters;

import fontys.magiccardgame.domain.Player;
import fontys.magiccardgame.persistence.entity.PlayerEntity;

public final class PlayerConverter {
    private PlayerConverter(){}
    public static Player convert(PlayerEntity entity){
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .ownedCards(entity.getOwnedCards().stream().map(CardConverter::convert).toList())
                .deck(DeckConverter.convert(entity.getDeck()))
                .userId(entity.getUser().getId())
                .build();
    }
}
