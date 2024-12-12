package com.ktb.joing.domain.item.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EtcRequest {

    @Size(max = 50)
    private String name;
    @Size(max = 200)
    private String value;

    @Builder
    public EtcRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
