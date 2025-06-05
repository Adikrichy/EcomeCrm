package org.aldousdev.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //не создаёт отдельную таблицу, но наследники получат поля
public abstract class BaseEntity {
    @CreationTimestamp //	автоматически ставит дату при insert
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp //автоматически обновляется при update
    private LocalDateTime updatedAt;
    //Это для ускорения разработки Единый способ отслеживать createdAt updatedAt и Что бы избежать дублирования кода
    // Не забудь что бы классы наследовались от этого класса
}
