package fontys.magiccardgame.business.converters;

import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.entity.UserEntity;

public class UserConverter {
private UserConverter(){}
    public static User convert(UserEntity entity){
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
