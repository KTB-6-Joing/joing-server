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
                                        AiErrorCode errorCode = switch (errorResponse.getDetail()) {
                                            case "유효하지 않은 형식의 채널아이디입니다." -> AiErrorCode.AI_INVALID_CHANNEL_ID_FORMAT;
                                            case "유효하지 않은 채널아이디입니다." -> AiErrorCode.AI_INVALID_CHANNEL_ID;
                                            case "영상의 개수가 충분하지 않아 더이상의 평가가 불가능합니다." -> AiErrorCode.AI_INSUFFICIENT_VIDEOS;
                                            default -> AiErrorCode.AI_VALIDATION_ERROR;
                                        };
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

}
