package backend.academy.userservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "key_words")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyWords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_words_id")
    private Long id;

    @Column(name = "words")
    private String words;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "category_id")
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id",
            referencedColumnName = "id")
    private User user;
}
