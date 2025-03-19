package com.example.momowas.crew.dto;


import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import com.example.momowas.crewregion.domain.CrewRegion;
import com.example.momowas.region.domain.Region;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import javax.xml.stream.Location;
import java.util.List;

public class CrewSpecification {

    public static Specification<Crew> equalCrewIdIn(List<Long> ids) {
        return (root, query, criteriaBuilder) -> root.get("id").in(ids);
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

    public static Specification<Crew> hasRegion(String depth1) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<CrewRegion> crewRegion = subquery.from(CrewRegion.class);

            Join<CrewRegion, Region> regionJoin = crewRegion.join("region");

            subquery.select(crewRegion.get("crew").get("id"))
                    .where(criteriaBuilder.equal(regionJoin.get("regionDepth1"), depth1));

            return root.get("id").in(subquery);
        };
    }


}