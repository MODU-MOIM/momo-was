package com.example.momowas.crew.dto;


import com.example.momowas.crew.domain.Category;
import com.example.momowas.crew.domain.Crew;
import org.springframework.data.jpa.domain.Specification;

public class CrewSpecification {

    public static Specification<Crew> equalCrewName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Crew> equalCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

}