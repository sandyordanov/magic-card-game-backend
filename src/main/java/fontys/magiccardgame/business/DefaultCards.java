package fontys.magiccardgame.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DefaultCards {

    @Value("${default.card.ids}")
    private String defaultCardIds;

    public List<Long> getDefaultCardIds() {
        return Arrays.stream(defaultCardIds.split(","))
                .map(Long::parseLong)
                .toList();
    }
}

