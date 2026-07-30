package com.g9team10.backend.domain.model;

import com.g9team10.backend.domain.event.ContentCreatedEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content extends AbstractAggregateRoot<Content> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private String category;
    @Column(name = "knowledge_level", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Level level = Level.INTERMEDIATE;
    @Enumerated(EnumType.STRING)
    private EmbeddingStatus embeddingStatus = EmbeddingStatus.PENDING;
    private Double probability;
    private Boolean revised = Boolean.FALSE;
    @CreationTimestamp
    private OffsetDateTime dateProcessing;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "content_tag",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Content(String title, String text, String category, Double probability) {
        this.title = title;
        this.text = text;
        this.category = category;
        this.probability = probability;
    }

    public void classify(Level level) {
        this.level = level;
    }

    public void review() {
        this.revised = Boolean.TRUE;
    }

    public void addTags(Set<Tag> tags) {
        this.tags.addAll(tags);
    }

    public void replaceTags(Set<Tag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public void created() {
        registerEvent(new ContentCreatedEvent(id));
    }

    public void startEmbedding() {
        this.embeddingStatus = EmbeddingStatus.PROCESSING;
    }

    public void completeEmbedding() {
        this.embeddingStatus = EmbeddingStatus.COMPLETED;
    }

    public void failEmbedding() {
        this.embeddingStatus = EmbeddingStatus.FAILED;
    }
}