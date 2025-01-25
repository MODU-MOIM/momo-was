package com.example.momowas.archive.repository;

import com.example.momowas.archive.domain.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
