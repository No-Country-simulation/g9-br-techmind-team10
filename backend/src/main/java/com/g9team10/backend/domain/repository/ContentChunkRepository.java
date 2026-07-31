package com.g9team10.backend.domain.repository;

import com.g9team10.backend.domain.model.ContentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentChunkRepository extends JpaRepository<ContentChunk, Long> {
}