package com.training.kafka.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.kafka.consumer.data.FlightInfoRepository;
import com.training.kafka.consumer.data.FlightInfo;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FlightInfoConsumer {
    private static final Logger logger = LoggerFactory.getLogger(FlightInfoConsumer.class);
    private final FlightInfoRepository flightInfoRepository;
    private final ObjectMapper objectMapper;
    private final ParallelStreamProcessor<String, String> parallelConsumer;

    @Value("${kafka.topic.flight-info}")
    private String topic;

    public FlightInfoConsumer(FlightInfoRepository flightInfoRepository, ObjectMapper objectMapper, ParallelStreamProcessor<String, String> parallelConsumer) {
        this.flightInfoRepository = flightInfoRepository;
        this.objectMapper = objectMapper;
        this.parallelConsumer = parallelConsumer;
    }

    @PostConstruct
    public void start() {
        parallelConsumer.subscribe(List.of(topic));
        parallelConsumer.poll(record -> {
            String flightInfoJson = record.value();
            logger.info("Consumed flight info: {}", flightInfoJson);
            try {
                FlightInfo flightInfo = objectMapper.readValue(flightInfoJson, FlightInfo.class);
                flightInfoRepository.save(flightInfo);
            } catch (JsonProcessingException e) {
                logger.error("Error deserializing flight info", e);
            }
        });
    }
}