package com.itransition.itransitiontaskfour.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity(name = "users")
public class User implements UserDetails ,Comparable<User>{

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    private LocalDateTime lastLoginTime;
    private LocalDateTime registrationTime;
    private boolean isBlocked;
    private String password;

    public User(String name, String email, LocalDateTime lastLoginTime, LocalDateTime registrationTime, boolean isBlocked, String password, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.lastLoginTime = lastLoginTime;
        this.registrationTime = registrationTime;
        this.isBlocked = isBlocked;
        this.password = password;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updated_at;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
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
        if (isBlocked==true){
            return false;
        }
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

    @Override
    public int compareTo(User o) {
        return (int) (getId()-o.getId());
    }
}
