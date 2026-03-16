package br.com.libertadfacilities.blog.services;

import br.com.libertadfacilities.blog.exception.ResourceNotFoundException;
import br.com.libertadfacilities.blog.model.Comment;
import br.com.libertadfacilities.blog.model.Post;
import br.com.libertadfacilities.blog.repositories.CommentRepository;
import br.com.libertadfacilities.blog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment addComment(Long postId, Comment comment){

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post não encontrado."));

        comment.setPost(post);

        return commentRepository.save(comment);
    }

    public List<Comment> getApprovedCommentsByPost(Long postId) {
        return commentRepository.findByPostIdAndApprovedTrue(postId);
    }

    public List<Comment> getPendingComments() {
        return commentRepository.findByApprovedFalse();
    }

    public Comment approveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));

        comment.setApproved(true);
        return commentRepository.save(comment);
    }

    public void rejectComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
        commentRepository.delete(comment);
    }
}
