package com.example.demo.domain.user;

import com.example.demo.core.exception.UserNotFoundException;
import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.imagepost.ImagePost;
import com.example.demo.domain.imagepost.ImagePostRepository;
import com.example.demo.domain.role.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
@Log4j2
@Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
    private final UserRepository userRepository;

    private static final String DOES_NOT_EXIST = " is non-existent";
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private final ImagePostRepository imagePostRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService, PasswordEncoder passwordEncoder,
                           UserRepository userRepository, ImagePostRepository imagePostRepository) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.imagePostRepository =  imagePostRepository;
    }

    /**
     * Given an email address, this function tries to find a matching UserDetails object.
     * If no User is found, an exception is thrown.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Trying to load user with email: " + email);

        return ((UserRepository) repository).findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * Used to register a new User.
     * By default, the new user will be assigned the role DEFAULT, along with a generated password.
     */
    @Override
    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("DEFAULT"))));

        return save(user);
    }

    /**
     * Used to register a new User.
     * By default, the new user will be assigned the role DEFAULT, along with a password consisting of the full name.
     */
    @Override
    public User registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getFullName()));
        user.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("DEFAULT"))));

        return save(user);
    }

    /**
     * Updates the information of a User if the UserController PreAuthorize checks passed.
     * Roles and password will not be changed through this action.
     */
    @Override
    public User updateUserById(UUID id, User newUserData) {
        if (repository.existsById(id)) {

            User user = findById(id);
            user.setFirstName(newUserData.getFirstName());
            user.setLastName(newUserData.getLastName());
            user.setEmail(newUserData.getEmail());

            return repository.save(user);
        } else {
            String errorMessage = HttpStatus.NOT_FOUND + " User with id " + id + DOES_NOT_EXIST;
            log.warn(errorMessage);
            throw new NoSuchElementException(errorMessage);
        }
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Deletes a user by id. It first removes the user from the list of likes from the liked posts of the user, to not
     * violate the data integrity.
     */
    @Override
    public void deleteUserById(UUID userId) {
        User toBeDeleted = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for(ImagePost post : toBeDeleted.getLikedPosts()) {
            Set<User> modifiedUsers = post.getLikes();
            modifiedUsers.remove(toBeDeleted);
            post.setLikes(modifiedUsers);
            imagePostRepository.save(post);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Used to make a POST request. Since a user shouldn't be able to create an ImagePost under a different user's name,
     * the used DTO doesn't have anything but the imageUrl and the description.
     * The author gets set by using the SecurityContext and identifying the logged-in user making the request.
     */
    @Override
    public User getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.debug("Trying to find user with email: " + email);

        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
