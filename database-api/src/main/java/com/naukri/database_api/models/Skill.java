package com.naukri.database_api.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_skill_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false, unique = true)
    String name;
}
