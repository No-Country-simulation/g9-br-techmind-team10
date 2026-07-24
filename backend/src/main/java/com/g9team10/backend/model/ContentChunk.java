package com.g9team10.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "content_chunk")
public class ContentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    public ContentChunk(Content content, int position, String text) {
        this.content = content;
        this.position = position;
        this.text = text;
    }
}
