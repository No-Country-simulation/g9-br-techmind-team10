package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.valueObject.CategoryCount;
import com.g9team10.backend.domain.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentCountService {
    private final ContentRepository repository;

    public List<CategoryCount> findAll() {
        return repository.countByCategory();
    }
}
