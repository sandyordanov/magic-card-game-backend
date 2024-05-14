package fontys.magiccardgame.business;

import fontys.magiccardgame.business.dto.CreateUserRequest;
import fontys.magiccardgame.business.dto.GetAllUsersResponse;
import fontys.magiccardgame.business.dto.UpdateUserRequest;
import fontys.magiccardgame.business.exception.UsernameAlreadyExistsException;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTestService {

//    private final PasswordEncoder passwordEncoder;
//
//    private UserRepository userRepo;
//    private CardRepository cardRepository;
//    private PlayerRepository playerRepository;
//
//    DefaultCardsService defaultCardsService;

  //  @Transactional
    public void createUser(CreateUserRequest request) {
//        if (userRepo.findByUsername(request.getUsername()) != null) {
//            throw new UsernameAlreadyExistsException();
//        }
//        UserEntity newUserEntity = generateUserEntity(request);
//        saveNewPlayer(newUserEntity);
//        return UserConverter.convert(newUserEntity);

    }


    // private helper methods
//    private void saveNewPlayer(UserEntity user) {
//
//        List<Long> defaultCardIds = defaultCardsService.getDefaultCardIds();
//        List<CardEntity> defaultCards = cardRepository.findAllById(defaultCardIds);
//
//        PlayerEntity newPlayer = PlayerEntity.builder()
//                .name("player" + user.getId())
//                .user(user)
//                .ownedCards(defaultCards)
//                .deck(new DeckEntity())
//                .build();
//        playerRepository.save(newPlayer);
//    }
//
//    private UserEntity generateUserEntity(CreateUserRequest request) {
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//        UserEntity newUser = UserEntity.builder()
//                .username(request.getUsername())
//                .password(encodedPassword)
//                .role(RoleEnum.PLAYER)
//                .build();
//        return userRepo.save(newUser);
//    }
}
