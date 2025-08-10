package com.doculearn.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "questionoptions")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "created_at", updatable = false, insertable = false)
    private Instant createdAt;

    @Column(name = "update_at", insertable = false)
    private Instant updateAt;

    public QuestionOption() {}

    public QuestionOption(Integer id) {
        this.id = id;
    }

    //so sanh 2 doi tuong thong qua primaryKey
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuestionOption that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "com.doculearn.pojo.QuestionOption[ id=" + id + " ]";
    }

}