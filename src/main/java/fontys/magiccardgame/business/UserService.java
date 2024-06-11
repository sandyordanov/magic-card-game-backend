package fontys.magiccardgame.business;

import fontys.magiccardgame.business.converters.UserConverter;
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

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepo;
    private CardRepository cardRepository;
    private PlayerRepository playerRepository;
    private AccessToken requestAccessToken;
    static final String UNAUTHORISED_EXCEPTION_MESSAGE = "USER_ID_NOT_FROM_LOGGED_IN_USER";
    DefaultCards defaultCards;

    public Optional<User> getUser(Long id) {
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) && !requestAccessToken.getUserId().equals(id)) {
            throw new UnauthorizedDataAccessException(UNAUTHORISED_EXCEPTION_MESSAGE);
        }
        return userRepo.findById(id).map(UserConverter::convert);
    }

    public GetAllUsersResponse getAllUsers() {
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name())) {
            throw new UnauthorizedDataAccessException("ADMIN_PERMISSION_MANDATORY");
        }
        return GetAllUsersResponse.builder()
                .users(userRepo.findAll().stream().map(UserConverter::convert).toList()).build();
    }

    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepo.findByUsername(request.getUsername()) != null) {
            throw new UsernameAlreadyExistsException();
        }
        UserEntity newUserEntity = generateUserEntity(request);
        saveNewPlayer(newUserEntity);
        return UserConverter.convert(newUserEntity);
    }

    public void createAdmin(CreateUserRequest request) {
        //only admins can create admin accounts
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name())) {
            throw new UnauthorizedDataAccessException("ADMIN_PERMISSION_MANDATORY");
        }
        if (userRepo.findByUsername(request.getUsername()) != null) {
            throw new UsernameAlreadyExistsException();
        }
        generateAdminEntity(request);
    }

    public void updateUser(UpdateUserRequest request) {
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) && !requestAccessToken.getUserId().equals(request.getId())) {
            throw new UnauthorizedDataAccessException(UNAUTHORISED_EXCEPTION_MESSAGE);
        }
        UserEntity user = userRepo.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found for ID: " + request.getId()));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);

    }

    public void deleteUser(Long id) {
        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) && !requestAccessToken.getUserId().equals(id)) {
                throw new UnauthorizedDataAccessException(UNAUTHORISED_EXCEPTION_MESSAGE);
        }
        userRepo.deleteById(id);
    }


    // private helper methods
    private void saveNewPlayer(UserEntity user) {

        List<Long> defaultCardIds = defaultCards.getDefaultCardIds();
        List<CardEntity> defaultCardsCollection = cardRepository.findAllById(defaultCardIds);

        PlayerEntity newPlayer = PlayerEntity.builder()
                .name("player" + user.getId())
                .user(user)
                .ownedCards(defaultCardsCollection)
                .deck(new DeckEntity())
                .build();
        playerRepository.save(newPlayer);
    }

    private UserEntity generateUserEntity(CreateUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(RoleEnum.PLAYER)
                .build();
        return userRepo.save(newUser);
    }

    private UserEntity generateAdminEntity(CreateUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(RoleEnum.ADMIN)
                .build();
        return userRepo.save(newUser);
    }
}
