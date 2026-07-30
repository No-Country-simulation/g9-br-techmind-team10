package com.g9team10.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private String category;
    @Column(name = "knowledge_level", nullable = false, length = 20)
    private String level = "intermediario";
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

    public void addTag(Tag tag) {
        getTags().add(tag);
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public void review() {
        this.revised = Boolean.TRUE;
    }
}