package fontys.magiccardgame.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultCardsService {

    @Value("${default.card.ids}")
    private String defaultCardIds;

    public List<Long> getDefaultCardIds() {
        return Arrays.stream(defaultCardIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}

