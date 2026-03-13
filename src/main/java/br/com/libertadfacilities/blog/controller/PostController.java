package br.com.libertadfacilities.blog.controller;

import br.com.libertadfacilities.blog.model.Post;
import br.com.libertadfacilities.blog.model.User;
import br.com.libertadfacilities.blog.repositories.UserRepository;
import br.com.libertadfacilities.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "file", required = false)MultipartFile file
            ){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado"));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        Post createdPost = postService.createPost(post, author.getId(), categoryId, file);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }
}
