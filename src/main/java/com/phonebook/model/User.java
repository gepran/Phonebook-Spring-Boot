package com.phonebook.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "LOGIN" }) })
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String login;

    @Column
    private String firstName;

    @Column
    private String secondName;

    @Column
    private String password;

    @Column
    private String phoneNumber;

    @Column
    private Date birthdate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Column
    private String resetToken;

    @Column(unique = true)
    private String email1;
}