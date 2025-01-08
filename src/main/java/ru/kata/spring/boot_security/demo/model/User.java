package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String secondName;
    private Integer age;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getFirstName() + " " + getSecondName();
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

    public User(String firstName,
                String secondName,
                Integer age,
                String email,
                String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public User() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String secondName) {
        this.email = secondName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        if (roles == null) {
            return new ArrayList<Role>();
        } else {
            return roles;
        }
    }

    public List<String> getShortRoles() {
        return getRoles().stream()
                .map(role -> role.getName().startsWith("ROLE_") ?
                        role.getName().substring(5)
                        : role.getName())
                .collect(Collectors.toList());
    }

    public String getShortStringRoles() {
        return getRoles().stream()
                .map(role -> role.getName().startsWith("ROLE_") ?
                        role.getName().substring(5)
                        : role.getName())
                .collect(Collectors.joining(" "));
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
    public void addRole(String role) {
        if (role.contains("USER")) {
            roles.add(new Role(2L,"ROLE_USER"));
        } else if (role.contains("ADMIN")) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        }
    }

    public void setRole(String role) {
        roles.clear();
        if (role.contains("USER")) {
            roles.add(new Role(2L,"ROLE_USER"));
        } else if (role.contains("ADMIN")) {
            roles.add(new Role(1L, "ROLE_ADMIN"));
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName=" + firstName + '\'' +
                ", secondName=" + secondName + '\'' +
                ", age=" + age + '\'' +
                ", email=" + email + '\'' +
                ", roles = " + roles.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getSecondName(), user.getSecondName()) &&
                Objects.equals(getAge(), user.getAge()) &&
                Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getSecondName(), getEmail(), getAge());
    }
}
