package com.jobmatchup.jobmatchup.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USER_TBL")
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FIRST_NAME", nullable = false,length = 30)
    private String firstName;
    @Column(name = "SURNAME", nullable = true, length = 40)
    private String surname;
    @Column(name = "EMAIL", unique = true, length = 100)
    private String email;
    @Column(name = "PASSWORD", nullable = false, length = 200)
    private String password;
    @Column(name = "ADDRESS", nullable = false, length = 200)
    private String address;

    public User() {
    }

    public User(String firstName, String surname, String email, String password, String address) {
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    

}
