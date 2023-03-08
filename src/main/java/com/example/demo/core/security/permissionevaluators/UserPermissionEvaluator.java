package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.imagepost.ImagePost;
import com.example.demo.domain.imagepost.ImagePostRepository;
import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class UserPermissionEvaluator {

  // TODO ask if this allowed
  @Autowired
  private final ImagePostRepository imagePostRepository;

  public UserPermissionEvaluator(ImagePostRepository imagePostRepository) {
    this.imagePostRepository = imagePostRepository;
  }

  public boolean isUserAboveAge(User principal, int age) {
    return true;
  }

  public boolean isUserRequestingOwnResourceBasedOnPost(User principal, UUID id) {
    ImagePost imagePost = imagePostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Image Post with ID: " + id + " is not existant"
    ));
    UUID authorId = imagePost.getAuthor().getId();
    return principal.getId().equals(authorId);
  }

  public boolean isUserRequestingOwnResource(User principal, UUID id) {
    return principal.getId().equals(id);
  }

}
