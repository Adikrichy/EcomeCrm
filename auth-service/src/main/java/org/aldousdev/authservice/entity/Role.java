package org.aldousdev.authservice.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "name")
})
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",unique = true,nullable = false,length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    /**
     * Связь многие-ко-многим с пользователями
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Конструктор для создания роли с именем
     */
    public Role(String name){
        this.name = name;
    }
    /**
     * Конструктор для создания роли с именем и описанием
     */
    public Role(String name,String description){
        this.name = name;
        this.description = description;
    }
    public static Role createUserRole(){
        return Role.builder()
                .name("ROLE_USER")
                .description("Standard role users")
                .isDefault(true)
                .build();
    }

    public static Role createAdminRole(){
        return Role.builder()
                .name("Role_Admin")
                .description("Role administration")
                .isDefault(false)
                .build();
    }

    public static Role createModeratorRole(){
        return Role.builder()
                .name("Role_Moderator")
                .description("Role Moderator")
                .isDefault(false)
                .build();
    }
    /**
     * Проверяет, является ли роль ролью по умолчанию
     */
    public boolean isDefaultRole(){
        return Boolean.TRUE.equals(isDefault);
    }
    public boolean isAdminRole(){
        return "ROLE_Admin".equals(name);
    }
    public boolean isUserRole(){
        return "ROLE_USER".equals(name);
    }
    public String getSimpleName() {
        if (name != null && name.startsWith("ROLE_")){
            return name.substring(5);
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        return id != null && id.equals(role.getId());
    }

    @Override
    public int hashCode() {
        return  getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
