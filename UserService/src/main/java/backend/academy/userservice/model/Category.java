package backend.academy.userservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY)
    private Set<Stat> stats = new HashSet<>();

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY)
    private Set<KeyWords> keyWords = new HashSet<>();

}
