package com.ntm.doculearn.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StudentenrollmentId implements Serializable {
    private static final long serialVersionUID = 3603399830545734447L;
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentenrollmentId entity = (StudentenrollmentId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.subjectId, entity.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, subjectId);
    }

}