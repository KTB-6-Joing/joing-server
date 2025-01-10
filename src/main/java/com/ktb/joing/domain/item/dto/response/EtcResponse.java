package com.ktb.joing.domain.item.dto.response;

import com.ktb.joing.domain.item.entity.Etc;

public record EtcResponse(
        String name,
        String value
){
    public static EtcResponse from(Etc etc) {
        return new EtcResponse(
                etc.getName(),
                etc.getValue()
        );
    }
}
