package com.g9team10.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "user_content_tag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_content_tag_user_content_name",
                        columnNames = {"user_id", "content_id", "normalized_name"}
                )
        }
)
public class UserContentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "normalized_name", nullable = false, length = 80)
    private String normalizedName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UserContentTag(User user, Content content, String name, String normalizedName) {
        this.user = user;
        this.content = content;
        this.name = name;
        this.normalizedName = normalizedName;
    }
}