package com.progressoft.fx.controller;

import com.progressoft.fx.dto.request.DealRequest;
import com.progressoft.fx.model.Deal;
import com.progressoft.fx.repository.DealRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealRepository dealRepository;

    @PostMapping
    @Transactional(noRollbackFor = Exception.class)
    public ResponseEntity<?> saveDeal(@Valid @RequestBody DealRequest request) {
        log.info("Received FX rows request: {}", request);
        try {
            if (dealRepository.existsByDealUniqueId(request.getDealUniqueId())) {
                log.warn("Duplicate row with id {} ignored", request.getDealUniqueId());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Row with ID " + request.getDealUniqueId() + " already exists");
            }
            Deal deal = Deal.builder()
                    .dealUniqueId(request.getDealUniqueId())
                    .fromCurrency(request.getFromCurrency())
                    .toCurrency(request.getToCurrency())
                    .dealTimestamp(OffsetDateTime.parse(request.getDealTimestamp()))
                    .amount(request.getAmount())
                    .build();
            Deal saved = dealRepository.save(deal);
            log.info("Saved row {}", saved.getDealUniqueId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data format or constraint violation.");
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    /**
     * Bulk import endpoint – accepts multiple deals and saves them independently (no rollback).
     */
    @PostMapping("/bulk")
    @Transactional(noRollbackFor = Exception.class)
    public ResponseEntity<?> bulkImport(@Valid @RequestBody List<DealRequest> requests) {
        log.info("Bulk import of {} deals", requests.size());
        int imported = 0;
        int duplicates = 0;
        for (DealRequest req : requests) {
            try {
                if (dealRepository.existsByDealUniqueId(req.getDealUniqueId())) {
                    duplicates++;
                    continue;
                }
                Deal deal = Deal.builder()
                        .dealUniqueId(req.getDealUniqueId())
                        .fromCurrency(req.getFromCurrency())
                        .toCurrency(req.getToCurrency())
                        .dealTimestamp(OffsetDateTime.parse(req.getDealTimestamp()))
                        .amount(req.getAmount())
                        .build();
                dealRepository.save(deal);
                imported++;
            } catch (Exception e) {
                log.error("Failed to import row {}: {}", req.getDealUniqueId(), e.getMessage());
            }
        }
        String summary = String.format("Imported %d row. Skipped %d duplicates.", imported, duplicates);
        log.info(summary);
        return ResponseEntity.ok(summary);
    }
}
