package com.g9team10.backend.service;

import com.g9team10.backend.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}