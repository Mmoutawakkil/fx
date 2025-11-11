package com.progressoft.fx.service.implementation;

import com.progressoft.fx.constant.SaveResult;
import com.progressoft.fx.dto.request.DealRequest;
import com.progressoft.fx.model.Deal;
import com.progressoft.fx.repository.DealRepository;
import com.progressoft.fx.service.DealService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SaveResult saveDeal(DealRequest req) {
        OffsetDateTime ts;
        try {
            ts = OffsetDateTime.parse(req.getDealTimestamp());
        } catch (DateTimeParseException ex) {
            log.warn("Invalid timestamp for row {} : {}", req.getDealUniqueId(), req.getDealTimestamp());
            return SaveResult.ERROR;
        }
        Deal deal = Deal.builder()
                .dealUniqueId(req.getDealUniqueId())
                .fromCurrency(req.getFromCurrency())
                .toCurrency(req.getToCurrency())
                .dealTimestamp(ts)
                .amount(req.getAmount()).build();
        try {
            dealRepository.save(deal);
            log.info("Imported row {}", req.getDealUniqueId());
            return SaveResult.IMPORTED;
        } catch (DataIntegrityViolationException dive) {
            log.info("Duplicate row detected {}", req.getDealUniqueId());
            return SaveResult.DUPLICATE;
        } catch (Exception ex) {
            log.error("Error saving row {}: {}", req.getDealUniqueId(), ex.getMessage());
            return SaveResult.ERROR;
        }
    }
}
