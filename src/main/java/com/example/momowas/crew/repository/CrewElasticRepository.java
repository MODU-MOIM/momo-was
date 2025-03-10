package com.example.momowas.crew.repository;

import com.example.momowas.crew.domain.CrewDocument;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewElasticRepository extends ElasticsearchRepository<CrewDocument, String> {
    SearchHits<CrewDocument> findByName(String name);
}
