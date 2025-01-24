package com.example.momowas.record.repository;

import com.example.momowas.record.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
