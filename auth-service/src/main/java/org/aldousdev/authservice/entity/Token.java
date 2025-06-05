package org.aldousdev.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private boolean expired;

    private boolean revoked;

    @ManyToOne (fetch = FetchType.LAZY) // один токен на один юзер !! не грузим юзера, если не надо (оптимизация)
    @JoinColumn(name = "user_id", nullable = false)  //создаёт внешний ключ в таблице token
    private User user;
}
