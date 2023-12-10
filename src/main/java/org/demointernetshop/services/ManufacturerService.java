package org.demointernetshop.services;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.ManufacturerDto;
import org.demointernetshop.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public List<ManufacturerDto> getAllManufacturer() {
        return manufacturerRepository.findAll().stream()
                .map(ManufacturerDto::from)
                .toList();
    }
}
