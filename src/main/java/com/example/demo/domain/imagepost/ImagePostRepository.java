package com.example.demo.domain.imagepost;

import com.example.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImagePostRepository extends JpaRepository<ImagePost, UUID> {
    List<ImagePost> findImagePostByAuthor(User author);

    @Query(
            value = "SELECT * FROM image_post WHERE image_post.image_url = ?1",
            nativeQuery = true
    )
    List<ImagePost> findImagePostByImageUrl(String imageUrl);
}
