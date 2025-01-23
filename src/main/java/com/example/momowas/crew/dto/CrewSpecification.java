package com.example.momowas.crew.dto;


import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class CrewSpecification {

    public static Specification<Crew> equalCrewName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Crew> equalCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Crew> equalAgeGroup(Integer ageGroup) {
        return (root, query, criteriaBuilder) -> {
            int lowerBound = (ageGroup / 10) * 10;
            int upperBound = lowerBound + 9;

            Expression<Integer> minAge = root.get("minAge");
            Expression<Integer> maxAge = root.get("maxAge");

            //null이면 항상 만족
            return criteriaBuilder.or(
                    criteriaBuilder.isNull(minAge),
                    criteriaBuilder.isNull(maxAge),

                    criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(minAge, upperBound),
                            criteriaBuilder.greaterThanOrEqualTo(maxAge, lowerBound)
                    )
            );
        };
    }

}