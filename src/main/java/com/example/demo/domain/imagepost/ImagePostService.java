package com.example.demo.domain.imagepost;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Log4j2
@Service
public class ImagePostService {
    private final ImagePostRepository imagePostRepository;
    private final UserService userService;

    private static final String DOES_NOT_EXIST = " is non-existent";

    @Autowired
    public ImagePostService(ImagePostRepository imagePostRepository, UserService userService) {
        this.imagePostRepository = imagePostRepository;
        this.userService = userService;
    }

    /**
     * Checks if the ImagePost given is valid and if so, adds a new database entry.
     * ImagePost cannot have an Id set and will by default have the currently logged-in
     * user as the author.
     */
    public ImagePost createImagePost(ImagePost imagePost) {
        if (imagePost.getId() != null || imageUrlAlreadyExistsForAuthor(imagePost)) {
            log.warn("Invalid request body given. ImagePost not created");

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body given.");
        } else {
            imagePost.setLikes(new HashSet<>());
            imagePost.setAuthor(userService.getCurrentlyLoggedInUser());
            imagePostRepository.save(imagePost);

            return imagePost;
        }
    }

    /**
     * Returns all available ImagePosts created by an author with the given authorId.
     */
    public List<ImagePost> getImagePostsByAuthor(UUID authorId) {
        log.info(String.format("Getting all ImagePosts for author with ID %s", authorId));

        return imagePostRepository.findImagePostByAuthor(userService.getUserById(authorId));
    }

    /**
     * Returns a specific ImagePost when a given Id is valid.
     * Should it not be valid, the respective exception will be thrown.
     */
    public ImagePost getImagePostById(UUID imagePostId) {
        log.info(String.format("Getting ImagePost with ID %s", imagePostId));

        return imagePostRepository.findById(imagePostId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("ImagePost with ID %s does not exist", imagePostId)));
    }

    /**
     * Returns a list of all ImagePosts created by all of Our-Space's users.
     * Pagination is implemented, though optional. If left null, this will act like a standard GetAll request.
     */
    public List<ImagePost> getAllImagePosts(Integer pageNum, Integer pageLength) {
        if (pageNum != null) {
            if (pageLength == null) {
                pageLength = imagePostRepository.findAll().size();
            }
            return imagePostRepository.findAll(PageRequest.of(pageNum, pageLength)).getContent();
        }
        log.info(HttpStatus.OK + "Getting all ImagePosts");

        return imagePostRepository.findAll();
    }

    /**
     * With a given valid ImagePost Id, the entry can be updated.
     * The precondition for this method to be called is that the PreAuthorize checks in the ImageController passed.
     */
    public ImagePost updateImagePost(UUID imagePostId, ImagePost updatedImagePost) {
        ImagePost postToBeUpdated = maybeGetImagePost(imagePostId);

        updatedImagePost.setId(imagePostId);
        updatedImagePost.setAuthor(postToBeUpdated.getAuthor());
        updatedImagePost.setLikes(postToBeUpdated.getLikes());

        return imagePostRepository.save(updatedImagePost);
    }

    /**
     * When a logged-in user likes another user's ImagePost, the like status needs to be updated.
     * Prerequisite is a valid userId and a valid ImagePostId and a passing PreAuthorize check.
     */
    public ImagePost updateLikeStatus(UUID imagePostId) {
        ImagePost toUpdate = maybeGetImagePost(imagePostId);
        User user = userService.getCurrentlyLoggedInUser();

        if (toUpdate.getLikes().contains(user)) {
            toUpdate.getLikes().remove(user);
        } else {
            toUpdate.getLikes().add(user);
        }

        return imagePostRepository.save(toUpdate);
    }

    /**
     * To delete an ImagePost entry, a valid ImagePostId needs to be given.
     * This action only takes place if the PreAuthorize checks pass.
     */
    public void deleteImagePostById(UUID imagePostId) {
        if (imagePostRepository.existsById(imagePostId)) {
            imagePostRepository.deleteById(imagePostId);
        } else {
            String deletionError = "Deletion of ImagePost unsuccessful. ImagePost with ID: " + imagePostId + DOES_NOT_EXIST;
            log.warn(deletionError);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, deletionError);
        }
    }

    /**
     * Tries to find an ImagePost with the given id.
     * Either returns the ImagePost or throws an exception.
     */
    private ImagePost maybeGetImagePost(UUID imagePostId) {
        return imagePostRepository.findById(imagePostId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Image Post with ID: " + imagePostId + DOES_NOT_EXIST
        ));
    }

    private boolean imageUrlAlreadyExistsForAuthor(ImagePost imagePost) {
        List<ImagePost> imagePosts = imagePostRepository.findImagePostByImageUrl(imagePost.getImageUrl());

        if (imagePosts.isEmpty()) {
            return false;
        }
        return imagePosts.get(0).getAuthor().equals(imagePost.getAuthor());
    }
}
