package com.progressoft.fx.service;

import com.progressoft.fx.constant.SaveResult;
import com.progressoft.fx.dto.request.DealRequest;
import com.progressoft.fx.model.Deal;
import com.progressoft.fx.repository.DealRepository;
import com.progressoft.fx.service.implementation.DealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DealServiceTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private DealServiceImpl dealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDeal_shouldReturnImported_whenValidDeal() {
        DealRequest req = DealRequest.builder()
                .dealUniqueId("123")
                .fromCurrency("USD")
                .toCurrency("MAD")
                .dealTimestamp("2025-11-11T20:00:00+01:00")
                .amount(BigDecimal.valueOf(100))
                .build();
        when(dealRepository.save(any(Deal.class))).thenReturn(new Deal());
        SaveResult result = dealService.saveDeal(req);
        assertThat(result).isEqualTo(SaveResult.IMPORTED);
        verify(dealRepository).save(any(Deal.class));
    }

    @Test
    void saveDeal_shouldReturnDuplicate_whenDataIntegrityViolationOccurs() {
        DealRequest req = DealRequest.builder()
                .dealUniqueId("123")
                .fromCurrency("USD")
                .toCurrency("MAD")
                .dealTimestamp("2025-11-11T20:00:00+01:00")
                .amount(BigDecimal.valueOf(100))
                .build();
        when(dealRepository.save(any(Deal.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate key"));
        SaveResult result = dealService.saveDeal(req);
        assertThat(result).isEqualTo(SaveResult.DUPLICATE);
        verify(dealRepository).save(any(Deal.class));
    }

    @Test
    void saveDeal_shouldReturnError_whenTimestampIsInvalid() {
        DealRequest req = DealRequest.builder()
                .dealUniqueId("124")
                .fromCurrency("USD")
                .toCurrency("MAD")
                .dealTimestamp("INVALID_TIMESTAMP")
                .amount(BigDecimal.valueOf(100))
                .build();
        SaveResult result = dealService.saveDeal(req);
        assertThat(result).isEqualTo(SaveResult.ERROR);
        verify(dealRepository, never()).save(any());
    }

    @Test
    void saveDeal_shouldReturnError_whenUnexpectedExceptionOccurs() {
        DealRequest req = DealRequest.builder()
                .dealUniqueId("125")
                .fromCurrency("USD")
                .toCurrency("MAD")
                .dealTimestamp("2025-11-11T20:00:00+01:00")
                .amount(BigDecimal.valueOf(100))
                .build();
        when(dealRepository.save(any(Deal.class)))
                .thenThrow(new RuntimeException("DB connection lost"));
        SaveResult result = dealService.saveDeal(req);
        assertThat(result).isEqualTo(SaveResult.ERROR);
        verify(dealRepository).save(any(Deal.class));
    }
}
