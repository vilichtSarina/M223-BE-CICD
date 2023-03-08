package com.example.demo.domain.imagepost;

import com.example.demo.domain.imagepost.dto.ImagePostDTO;
import com.example.demo.domain.imagepost.dto.ImagePostEditDTO;
import com.example.demo.domain.imagepost.dto.ImagePostEditMapper;
import com.example.demo.domain.imagepost.dto.ImagePostMapper;
import com.example.demo.domain.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/images")
public class ImagePostController {
    private final ImagePostService imagePostService;
    private final ImagePostMapper imagePostMapper;
    private final ImagePostEditMapper imagePostEditMapper;

    @Autowired
    public ImagePostController(ImagePostService imagePostService, ImagePostMapper imagePostMapper, ImagePostEditMapper imagePostEditMapper) {
        this.imagePostService = imagePostService;
        this.imagePostMapper = imagePostMapper;
        this.imagePostEditMapper = imagePostEditMapper;
    }

    @Operation(
            summary = "Create an image post",
            description = "Minimum required role: 'DEFAULT'. Minimum required Authority: 'DEFAULT'"
    )
    @PostMapping({"/", ""})
    public ResponseEntity<ImagePostDTO> createImagePost(@Valid @RequestBody ImagePostEditDTO imagePostDTO) {
        ImagePost imagePost = imagePostService.createImagePost(imagePostEditMapper.fromDTO(imagePostDTO));
        return new ResponseEntity<>(imagePostMapper.toDTO(imagePost), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete an image post by id",
            description = "Minimum required identity: Either has role 'ADMIN' or is acting on owned resource with authority 'DEFAULT'"
    )
    @DeleteMapping({"/{id}", "/{id}/"})
    @PreAuthorize("@userPermissionEvaluator.isUserRequestingOwnResourceBasedOnPost(authentication.principal.user,#id) || hasAuthority('DELETE_FOREIGN_POST')")
    public ResponseEntity<UserDTO> deleteImagePostById(@PathVariable UUID id) {
        imagePostService.deleteImagePostById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Like or dislike an image post by id",
            description = "Minimum required role: 'DEFAULT'. Minimum required Authority: 'DEFAULT'"
    )
    @PutMapping({"/like/{imagePostId}", "/{imagePostId}/"})
    @PreAuthorize("hasAuthority('DEFAULT')")
    public ResponseEntity<ImagePostDTO> updateLikeStatusById(@PathVariable UUID imagePostId) {
        ImagePostDTO updatedImagePost = imagePostMapper
                .toDTO(imagePostService.updateLikeStatus(imagePostId));
        return new ResponseEntity<>(updatedImagePost, HttpStatus.OK);
    }

    @Operation(
            summary = "Modify an image post by id",
            description = "Minimum required identity: Either has role 'ADMIN' or is acting on owned resource with authority 'DEFAULT'"
    )
    @PutMapping({"/{id}", "/{id}/"})
    @PreAuthorize("@userPermissionEvaluator.isUserRequestingOwnResourceBasedOnPost(authentication.principal.user,#id) || hasAuthority('MODIFY_FOREIGN_POST')")
    public ResponseEntity<ImagePostDTO> updateImagePostById(@PathVariable UUID id, @Valid @RequestBody ImagePostEditDTO imagePostDTO) {
        ImagePostDTO updatedImagePost = imagePostMapper
                .toDTO(imagePostService.updateImagePost(id, imagePostEditMapper.fromDTO(imagePostDTO)));
        return new ResponseEntity<>(updatedImagePost, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all image posts by id of the author",
            description = "Minimum required role: 'DEFAULT'. Minimum required Authority: 'DEFAULT'"
    )
    @GetMapping({"/author/{userId}", "/author/{userId}/"})
    @PreAuthorize("hasAuthority('DEFAULT')")
    public ResponseEntity<List<ImagePostDTO>> getImagePostsByAuthor(@PathVariable("userId") UUID authorId) {
        return new ResponseEntity<>(
                imagePostMapper.toDTOs(imagePostService.getImagePostsByAuthor(authorId)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get all image posts",
            description = "Minimum required role: 'DEFAULT'. Minimum required Authority: 'DEFAULT'"
    )
    @GetMapping({"/", ""})
    @PreAuthorize("hasAuthority('DEFAULT')")
    public ResponseEntity<List<ImagePostDTO>> getAllImagePosts(@RequestParam(name = "page", required = false) Integer pageNum,
                                                               @RequestParam(name = "page-len", required = false) Integer pageLength) {
        return new ResponseEntity<>(
                imagePostMapper.toDTOs(imagePostService.getAllImagePosts(pageNum, pageLength)),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get an image post by id",
            description = "Minimum required role: 'DEFAULT'. Minimum required Authority: 'DEFAULT'"
    )
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<ImagePostDTO> getImagePostById(@PathVariable UUID id) {
        return new ResponseEntity<>(
                imagePostMapper.toDTO(imagePostService.getImagePostById(id)),
                HttpStatus.OK
        );
    }
}
