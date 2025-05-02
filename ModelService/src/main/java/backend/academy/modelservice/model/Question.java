package backend.academy.modelservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String topic;
    @JsonProperty("correct_answer")
    private String correctAnswer;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private LocalDateTime createdAt;
}
