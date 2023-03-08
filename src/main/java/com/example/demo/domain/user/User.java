package com.example.demo.domain.user;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.imagepost.ImagePost;
import com.example.demo.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Log4j2
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "liked_posts")
    @ManyToMany(mappedBy = "likes")
    @JsonBackReference
    private Set<ImagePost> likedPosts;

    @Column(name = "created_image_posts")
    @JsonBackReference
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private Set<ImagePost> createdImagePosts = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(UUID id, String firstName, String lastName, String email, String password, Set<Role> roles, Set<ImagePost> likedPosts, Set<ImagePost> createdImagePosts) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.likedPosts = likedPosts;
        this.createdImagePosts = createdImagePosts;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getFullName() {
        return firstName + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Set<ImagePost> getLikedPosts() {
        return likedPosts;
    }

    @PrePersist
    public void logNewUserAttempt() {
        log.debug("Attempting to create new User with email: " + email);
    }

    @PostPersist
    public void logNewUserCreated() {
        log.info("Created User with email: " + email);
    }

    @PreRemove
    public void logDeleteUserAttempt() {
        log.debug("Deletion of User with email: " + email);
    }

    @PostRemove
    public void logDeleteUserSuccess() {
        log.info("Deletion of User successful");
    }

    @PreUpdate
    public void logUpdateUserAttempt() {
        log.debug("Updating User with email: " + email);
    }

    @PostUpdate
    public void logUpdateUserSuccess() {
        log.info("Updating User with email " + email + " successful");
    }
}
