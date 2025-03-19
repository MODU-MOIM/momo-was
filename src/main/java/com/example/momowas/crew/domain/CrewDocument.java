package com.example.momowas.crew.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Document(indexName = "crew")
public class CrewDocument {
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long crewId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Builder
    public CrewDocument(Long crewId, String name) {
        this.crewId = crewId;
        this.name = name;
    }
}

