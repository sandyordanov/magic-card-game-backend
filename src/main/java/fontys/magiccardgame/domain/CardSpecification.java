package fontys.magiccardgame.domain;

import fontys.magiccardgame.persistence.entity.CardEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CardSpecification {

    public static Specification<CardEntity> getCardsByFilters(String name, Integer minHealthPoints, Integer maxHealthPoints, Integer minAttackPoints, Integer maxAttackPoints) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (minHealthPoints != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("healthPoints"), minHealthPoints));
            }
            if (maxHealthPoints != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("healthPoints"), maxHealthPoints));
            }
            if (minAttackPoints != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("attackPoints"), minAttackPoints));
            }
            if (maxAttackPoints != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("attackPoints"), maxAttackPoints));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
