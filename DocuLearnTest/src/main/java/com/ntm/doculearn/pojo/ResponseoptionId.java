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
public class ResponseoptionId implements Serializable {
    private static final long serialVersionUID = -3345330897095114278L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "option_id", nullable = false)
    private Integer optionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResponseoptionId entity = (ResponseoptionId) o;
        return Objects.equals(this.optionId, entity.optionId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionId, userId);
    }

}