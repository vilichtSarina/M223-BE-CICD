package com.example.demo.domain.imagepost;

import com.example.demo.core.generic.AbstractEntity;
import com.example.demo.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Log4j2
@Getter
@Setter
@Entity
@Table(name = "image_post")
public class ImagePost extends AbstractEntity {

    @Column(name = "image_url", nullable = false)
    @NotBlank
    @NotNull(message = "imageUrl must not be null")
    @Length(min = 10, max = 200,message = "The URL cannot be shorter than 10 or longer than 200 characters")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonBackReference
    @NotNull(message = "author must not be null")
    private User author;

    @Column(name = "post_description")
    @Length(max = 200,message = "The description cannot be longer than 200 characters")
    private String description;

    @Column(name = "likes")
    @ManyToMany
    @JoinTable(
            name = "image_like",
            joinColumns = @JoinColumn(name = "id_image_post"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    @JsonBackReference
    @NotNull
    private Set<User> likes;

    public ImagePost() {
    }
    
    public ImagePost(UUID id, String imageUrl, User author, String description, Set<User> likes) {
        super(id);
        this.imageUrl = imageUrl;
        this.author = author;
        this.description = description;
        this.likes = likes;
    }

    @PrePersist
    public void logNewPostAttempt() {
        log.debug("Attempting to create new ImagePost with id: " + getId());
    }

    @PostPersist
    public void logNewPostCreated() {
        log.info("Created ImagePost with id: " + getId());
    }

    @PreRemove
    public void logDeletePostAttempt() {
        log.debug("Deletion of ImagePost with id: " + getId());
    }

    @PostRemove
    public void logDeletePostSuccess() {
        log.info("Deletion successful");
    }

    @PreUpdate
    public void logUpdatePostAttempt() {
        log.debug("Updating ImagePost with id: " + getId());
    }

    @PostUpdate
    public void logUpdatePostSuccess() {
        log.info("Updating ImagePost with id " + getId() + " successful");
    }
}
