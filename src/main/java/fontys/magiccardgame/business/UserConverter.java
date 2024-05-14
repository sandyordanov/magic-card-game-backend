package fontys.magiccardgame.business;

import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.entity.UserEntity;

final class UserConverter {

    public static User convert(UserEntity entity){
        return User.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .build();
    }
}
