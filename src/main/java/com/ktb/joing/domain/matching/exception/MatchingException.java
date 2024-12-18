package com.ktb.joing.domain.matching.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class MatchingException extends BusinessException {
    private final MatchingErrorCode matchingErrorCode;

    public MatchingException(MatchingErrorCode matchingErrorCode) {
        super(matchingErrorCode);
        this.matchingErrorCode = matchingErrorCode;
    }
}
