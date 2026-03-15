package org.skypro.recommendationservice.controller;

import org.skypro.recommendationservice.repository.RecommendationsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.skypro.recommendationservice.dto.ServiceInfoResponse;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ManagementController {

    private final RecommendationsRepository recommendationsRepository;
    private final BuildProperties buildProperties;

    public ManagementController(RecommendationsRepository recommendationsRepository,
                                BuildProperties buildProperties) {
        this.recommendationsRepository = recommendationsRepository;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/management/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        recommendationsRepository.clearCaches();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/management/info")
    public ServiceInfoResponse getInfo() {
        return new ServiceInfoResponse(
                buildProperties.getName(),
                buildProperties.getVersion()
        );
    }
}