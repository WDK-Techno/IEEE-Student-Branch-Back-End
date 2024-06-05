package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
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

    @ManyToOne //many users can in one academic year
    @JoinColumn(name = "academic_id")
    private AcademicYear academicYear;

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleOu> userRoleOus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleProject> userRoleProjects;


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
