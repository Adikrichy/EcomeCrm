package org.aldousdev.authservice.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //For google Auth And Unique
    private String email;

    private String password;

    private String fullName;

    private String telegramId;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean emailVerified;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    // mappedBy = "User" указывает на поле в Token, которое ссылается на User
    // cascade = ALL если удаляется юзер, удаляются и его токены
    // orphanRemoval = true  если убрать токен из списка, он удалится из БД
    private List<Token> tokens;
}
