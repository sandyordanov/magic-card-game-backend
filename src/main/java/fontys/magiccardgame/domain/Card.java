package fontys.magiccardgame.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Card {
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 0, message = "Health points must be greater than or equal to 0")
    @Max(value = 20, message = "Health points must be less than or equal to 20")
    private int healthPoints;

    @Min(value = 0, message = "Attack points must be greater than or equal to 0")
    @Max(value = 20, message = "Attack points must be less than or equal to 20")
    private int attackPoints;

}
