package backend.academy.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @ManyToMany()
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<Stat> stats = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<KeyWords> keyWords = new HashSet<>();
}