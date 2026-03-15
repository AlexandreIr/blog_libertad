package br.com.libertadfacilities.blog.repositories;

import br.com.libertadfacilities.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
}
