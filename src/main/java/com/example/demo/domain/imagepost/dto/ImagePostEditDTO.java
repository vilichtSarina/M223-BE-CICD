package com.example.demo.domain.imagepost.dto;

import com.example.demo.core.generic.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class ImagePostEditDTO extends AbstractDTO {
    @NotBlank
    @NotNull(message = "imageUrl must not be null")
    @Length(min = 10, max = 200,message = "The URL cannot be shorter than 10 or longer than 200 characters")
    private String imageUrl;
    @Length(max = 200,message = "The description cannot be longer than 200 characters")
    private String description;

    public ImagePostEditDTO(UUID id, String imageUrl, String description) {
        super(id);
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public ImagePostEditDTO() {

    }
}
