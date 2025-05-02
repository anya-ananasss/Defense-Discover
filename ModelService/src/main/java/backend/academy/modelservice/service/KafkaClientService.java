package backend.academy.modelservice.service;

import backend.academy.modelservice.dto.StatEvent;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface KafkaClientService {

    void sendNotificationStackOverflow(StatEvent statEvent) throws JsonProcessingException;
}