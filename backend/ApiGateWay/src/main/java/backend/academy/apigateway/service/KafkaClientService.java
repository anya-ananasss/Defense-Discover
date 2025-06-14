package backend.academy.apigateway.service;

import backend.academy.apigateway.dto.PasswordRepair;
import backend.academy.apigateway.dto.UserConfirmation;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface KafkaClientService {

    void sendNotificationStackOverflow(UserConfirmation userConfirmation) throws JsonProcessingException;

    void sendPasswordRepair(PasswordRepair passwordRepair) throws JsonProcessingException;
}