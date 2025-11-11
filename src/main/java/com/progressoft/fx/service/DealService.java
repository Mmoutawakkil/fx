package com.progressoft.fx.service;

import com.progressoft.fx.constant.SaveResult;
import com.progressoft.fx.dto.request.DealRequest;

public interface DealService {
    SaveResult saveDeal(DealRequest req);
}
