package com.example.demo.domain.imagepost.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.dto.UserDTO;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ImagePostDTO extends AbstractDTO {
    @NotBlank
    @NotNull(message = "imageUrl must not be null")
    @Length(min = 10, max = 200,message = "The URL cannot be shorter than 10 or longer than 200 characters")
    private String imageUrl;
    @NotNull(message = "author must not be null")
    private UserDTO author;
    @Length(max = 200,message = "The description cannot be longer than 200 characters")
    private String description;
    @NotNull
    private Set<UserDTO> likes;

    public ImagePostDTO(UUID id, String imageUrl, UserDTO author, String description, Set<UserDTO> likes) {
        super(id);
        this.imageUrl = imageUrl;
        this.author = author;
        this.description = description;
        this.likes = likes;
    }

    public ImagePostDTO() {

    }
}
