package com.ktb.joing.common.util.webClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb.joing.common.exception.AiErrorCode;
import com.ktb.joing.common.exception.AiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveHttpService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public <T, R> Mono<R> post(String url, T body, Class<R> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.value() == 422, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    try {
                                        AIErrorResponse errorResponse = objectMapper.readValue(errorBody, AIErrorResponse.class);
                                        AiErrorCode errorCode = getAiErrorCode(errorResponse.getDetail().getCode());
                                        return Mono.error(new AiException(errorCode, errorBody));
                                    } catch (Exception e) {
                                        return Mono.error(new AiException(AiErrorCode.AI_VALIDATION_ERROR, errorBody));
                                    }
                                })
                )
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new AiException(AiErrorCode.AI_BAD_REQUEST, errorBody)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new AiException(AiErrorCode.AI_BAD_GATEWAY, errorBody)))
                )
                .bodyToMono(responseType);
    }

    private AiErrorCode getAiErrorCode(String code) {
        return switch (code) {
            case "INVALID_CHANNEL_ID_FORMAT" -> AiErrorCode.AI_INVALID_CHANNEL_ID_FORMAT;
            case "INVALID_CHANNEL_ID" -> AiErrorCode.AI_INVALID_CHANNEL_ID;
            case "NOT_ENOUGH_UPLOADS" -> AiErrorCode.AI_NOT_ENOUGH_UPLOADS;
            default -> AiErrorCode.AI_VALIDATION_ERROR;
        };
    }

}
