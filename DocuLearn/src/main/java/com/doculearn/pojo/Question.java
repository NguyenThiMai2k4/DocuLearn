package com.doculearn.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("'SINGLE_CHOICE'")
    @Lob
    @Column(name = "response_type", nullable = false)
    private String responseType;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_at")
    private Instant updateAt;

    @OneToMany(mappedBy = "question")
    private Set<QuestionOption> questionOptions = new LinkedHashSet<>();

    public Question() {}

    public Question(Integer id) {
        this.id = id;
    }

    public Question(Integer id, String content, String responseType) {
        this.id = id;
        this.content = content;
        this.responseType = responseType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question question)) return false;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "com.doculearn.pojo.Question[ id=" + id + " ]";
    }
}