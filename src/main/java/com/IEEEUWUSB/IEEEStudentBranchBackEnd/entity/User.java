package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userID;



    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String ieee_email;

    @JsonIgnore
    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String ieee_membership_number;

    private String nameWithInitial;

    @Column(nullable = false)
    private String contactNo;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "varchar(255) default 'default_profile_pic.png'")
    private String profilePic;

    private String status;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(columnDefinition = "TEXT")
    private String fbURL;

    @Column(columnDefinition = "TEXT")
    private String linkedInURL;

    @Column(columnDefinition = "TEXT")
    private String insterURL;

    @Column(columnDefinition = "TEXT")
    private String location;

    @ManyToOne
    private AcademicYear  academicYear ;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
