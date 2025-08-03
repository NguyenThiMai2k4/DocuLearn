package com.doculearn.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "summary")
public class Summary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sections")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> sections;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_at")
    private Instant updateAt;

    @ColumnDefault("'PUBLISH'")
    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "summary")
    private Set<Question> questions = new LinkedHashSet<>();

    public Summary() {
    }

    public Summary(Integer id) {
        this.id = id;
    }

    public Summary(Integer id, String title, Map<String, Object> sections, String status) {
        this.id = id;
        this.title = title;
        this.sections = sections;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Summary summary)) return false;
        return Objects.equals(id, summary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "com.doculearn.pojo.Summary[ id=" + id + " ]";
    }
}