package com.training.kafka.consumer.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightInfoRepository extends JpaRepository<FlightInfo, Long> {
}