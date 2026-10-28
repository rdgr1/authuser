package com.ead.authuser.client;

import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.UUID;


@Component
public class CourseClient {
    Logger logger = LogManager.getLogger(CourseClient.class);

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

//    @Retry(name = "retryInstance", fallbackMethod = "retryfallback")
    @CircuitBreaker(name = "circuitbrakerInstance", fallbackMethod = "circuitBreakerFallback")
    public Page<CourseRecordDto> getAllCoursesByUser(UUID userId, Pageable pageable){
         var uri = UriComponentsBuilder.fromHttpUrl(baseUrlCourse + "/courses")
                .queryParam("userId",userId)
                .queryParam("page" + pageable.getPageNumber())
                .queryParam("size" + pageable.getPageSize())
                .queryParam("sort" + pageable.getSort().toString().replaceAll(":\\s*", ","))
                .build().toUri();
        try {
            return restClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(
                            new ParameterizedTypeReference<ResponsePageDto<CourseRecordDto>>() {});
        } catch (RestClientException e) {
            logger.error("Error Request RestClient with cause: {} ", e.getMessage());
            throw new RuntimeException("Error Request RestClient", e);
        }
    }

    public ResponseEntity<Boolean> existsByCourseIdAndUser(UUID courseId, UserModel userModel){
        var uri = baseUrlCourse + "/courses/" + courseId + "/users/" + userModel.getUserId();
        try {
            return restClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 404, (request, response) -> {
                                logger.error("Course Not Found CourseId: {}", courseId);
                                throw new NotFoundException("Course Not Found!");
                            }
                    )
                    .toEntity(Boolean.class);
        } catch (RestClientException e){
            logger.error("Error RestClient with cause: {}", e);
            throw new RuntimeException("Error Request GET RestClient", e );
        }
    }

    private Page<CourseRecordDto> retryfallback(UUID userId, Pageable pageable, Throwable t) {
        logger.error("Inside retry retryFallback, cause - {}", t.toString());
        var searchResult = new ArrayList<CourseRecordDto>();
        return new PageImpl<>(searchResult);
    }

    private Page<CourseRecordDto> circuitBreakerFallback(UUID user, Pageable pageable, Throwable t ){
        logger.error("Inside retry circuitBreakerFallback, cause - {}", t.toString());
        var searchResult = new ArrayList<CourseRecordDto>();
        return new PageImpl<>(searchResult);
    }
}
