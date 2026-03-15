package br.com.libertadfacilities.blog.services;

import br.com.libertadfacilities.blog.exception.BusinessRuleException;
import br.com.libertadfacilities.blog.exception.ResourceNotFoundException;
import br.com.libertadfacilities.blog.model.Category;
import br.com.libertadfacilities.blog.model.Post;
import br.com.libertadfacilities.blog.model.User;
import br.com.libertadfacilities.blog.repositories.CategoryRepository;
import br.com.libertadfacilities.blog.repositories.PostRepository;
import br.com.libertadfacilities.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public final CloudinaryService cloudinaryService;

    public Post createPost(Post post,
                           String email,
                           Long categoryId,
                           MultipartFile file){

        User author = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));

        post.setAuthor(author);
        post.setCategory(category);

        if(file != null && !file.isEmpty()) {
            String mediaUrl = cloudinaryService.uploadFile(file);
            post.setMediaUrl(mediaUrl);
        }

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByCategoryId(categoryId, pageable);
    }
}
