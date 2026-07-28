package com.g9team10.backend.domain.service;

import com.g9team10.backend.api.dto.response.ContentCountDTO;
import com.g9team10.backend.domain.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentCountService {
    private final ContentRepository repository;

    public List<ContentCountDTO> findAll() {
        return repository.countByCategory();
    }
}
