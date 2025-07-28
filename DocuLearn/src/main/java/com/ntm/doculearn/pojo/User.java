package com.ntm.doculearn.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "user")
@NamedQueries({

})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(min = 1, max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @Size(min = 1, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender", nullable = false)
    private String gender;


    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "role", nullable = false)
    private String role;

    @Lob
    @Column(name = "bio")
    private String bio;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @ColumnDefault("0")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "avatar")
    private String avatar;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Admin admin;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Teacher teacher;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Student student;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, String name, String gender, String password, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.password = password;
        this.role = role;
    }
    @Override
    public String toString() {
        return "com.post.pojo.User[ id=" + id + " ]";
    }

}
