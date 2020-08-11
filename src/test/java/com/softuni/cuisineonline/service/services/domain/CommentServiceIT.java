package com.softuni.cuisineonline.service.services.domain;

import com.softuni.cuisineonline.data.models.Comment;
import com.softuni.cuisineonline.data.models.User;
import com.softuni.cuisineonline.data.repositories.CommentRepository;
import com.softuni.cuisineonline.data.repositories.UserRepository;
import com.softuni.cuisineonline.errors.ServerException;
import com.softuni.cuisineonline.errors.ValidationException;
import com.softuni.cuisineonline.service.base.TestServiceBase;
import com.softuni.cuisineonline.service.models.comment.CommentEditServiceModel;
import com.softuni.cuisineonline.service.models.comment.CommentServiceModel;
import com.softuni.cuisineonline.util.TestUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.softuni.cuisineonline.service.services.util.Constants.COMMENT_CONTENT_LENGTH_UPPER_BOUND;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class CommentServiceIT extends TestServiceBase {

    private static final int COMMENT_CONTENT_VALID_LENGTH = 20;
    private static final String INVALID_LONG_CONTENT =
            TestUtils.getRandomString(COMMENT_CONTENT_LENGTH_UPPER_BOUND + 1);

    @Autowired
    CommentService service;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthenticatedUserFacade authenticationFacade;

    @MockBean
    UserService userService;

    private final List<Comment> comments = new ArrayList<>();
    private final List<User> users = TestUtils.getUsers();

    @Override
    protected void beforeEach() {
        super.beforeEach();
        for (User user : users) {
            final Comment comment = new Comment();
            comment.setId(TestUtils.getRandomString(10));
            comment.setContent(TestUtils.getRandomString(COMMENT_CONTENT_VALID_LENGTH));
            comment.setInstant(Instant.now());
            comment.setUploader(user.getProfile());
            user.getProfile().setComments(List.of(comment));
            comments.add(comment);
        }
    }

    @Test
    void getAll_ShouldReturnAllCommentsSorted() {
        List<Comment> sortedComments = new ArrayList<>(comments);
        sortedComments.sort(Comparator.comparing(Comment::getInstant));

        Mockito.when(commentRepository.findAllByOrderByInstantAsc())
                .thenReturn(sortedComments);
        Mockito.when(authenticationFacade.getPrincipalName())
                .thenReturn(TestUtils.getRandomListValue(users).getUsername());

        List<CommentServiceModel> actualComments = service.getAll();

        Assert.assertEquals(comments.size(), actualComments.size());
        for (int i = 0; i < actualComments.size(); i++) {

            Comment expected = sortedComments.get(i);
            CommentServiceModel actual = actualComments.get(i);

            Mockito.when(userRepository.findByUsername(eq(expected.getUploader().getUser().getUsername())))
                    .thenReturn(Optional.of(expected.getUploader().getUser()));

            assertEqualsComments(expected, actual);
        }
    }

    @Test
    void getAll_WhenPrincipalIsROOT_ShouldBeAbleToModifyAllComments() {
        Mockito.when(commentRepository.findAllByOrderByInstantAsc())
                .thenReturn(comments);
        User rootUser = users.stream().filter(u ->
                u.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(toList()).contains("ROOT"))
                .findAny().get();
        Mockito.when(authenticationFacade.getPrincipalName())
                .thenReturn(rootUser.getUsername());
        Mockito.when(userService.canModify(eq(rootUser.getUsername()), anyString()))
                .thenReturn(Boolean.TRUE);

        List<CommentServiceModel> actualComments = service.getAll();

        Assert.assertEquals(comments.size(), actualComments.size());
        for (CommentServiceModel actual : actualComments) {
            Assert.assertTrue(actual.isCanModify());
        }
    }

    @Test
    void getById_WhenValidId_ShouldReturnCorrectServiceModel() {
        for (Comment comment : comments) {
            String commentId = comment.getId();

            Mockito.when(commentRepository.findById(eq(commentId)))
                    .thenReturn(Optional.of(comment));

            CommentServiceModel actualComment = service.getById(commentId);

            assertEqualsComments(comment, actualComment);
        }
    }

    @Test
    void post_WhenValidCommentModel_ShouldSetCorrectValues() {
        Comment comment = TestUtils.getRandomListValue(comments);

        String uploaderUsername = comment.getUploader().getUser().getUsername();

        Mockito.when(userRepository.findByUsername(uploaderUsername))
                .thenReturn(Optional.of(comment.getUploader().getUser()));

        CommentServiceModel serviceModel = buildServiceModel(comment);

        service.post(serviceModel);

        ArgumentCaptor<Comment> argument = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(argument.capture());
        final Comment postedComment = argument.getValue();

        Assert.assertEquals(serviceModel.getId(), postedComment.getId());
        Assert.assertEquals(serviceModel.getInstant(), postedComment.getInstant());
        Assert.assertEquals(serviceModel.getUploaderUsername(), postedComment.getUploader().getUser().getUsername());
        Assert.assertEquals(serviceModel.getContent(), postedComment.getContent());
    }

    @Test
    void post_WhenPostModelWithNullUploaderUsername_ShouldThrow() {
        Comment comment = TestUtils.getRandomListValue(comments);
        CommentServiceModel serviceModel = new CommentServiceModel();
        serviceModel.setContent(comment.getContent());
        serviceModel.setInstant(Instant.now());
        serviceModel.setUploaderUsername(null);

        Assert.assertThrows(ServerException.class, () ->
                service.post(serviceModel));
    }

    @Test
    void post_WhenPostModelWithNullPostingDate_ShouldThrow() {
        Comment comment = TestUtils.getRandomListValue(comments);
        CommentServiceModel serviceModel = new CommentServiceModel();
        serviceModel.setContent(comment.getContent());
        serviceModel.setUploaderUsername(comment.getUploader().getUser().getUsername());
        serviceModel.setInstant(null);

        Assert.assertThrows(ServerException.class, () ->
                service.post(serviceModel));
    }

    @Test
    void post_WhenPostModelWithInvalidContentLength_ShouldThrow() {
        Comment comment = TestUtils.getRandomListValue(comments);
        CommentServiceModel serviceModel = new CommentServiceModel();
        serviceModel.setInstant(Instant.now());
        serviceModel.setUploaderUsername(comment.getUploader().getUser().getUsername());
        serviceModel.setContent("");

        Assert.assertThrows(ValidationException.class, () ->
                service.post(serviceModel));

        serviceModel.setContent(INVALID_LONG_CONTENT);

        Assert.assertThrows(ValidationException.class, () ->
                service.post(serviceModel));
    }

    @Test
    void edit_WhenValidCommentModel_ShouldChangeContent() {
        Comment comment = TestUtils.getRandomListValue(comments);
        CommentEditServiceModel serviceModel = new CommentEditServiceModel();
        serviceModel.setId(comment.getId());
        serviceModel.setContent(TestUtils.getRandomString(COMMENT_CONTENT_VALID_LENGTH));
        Mockito.when(commentRepository.findById(eq(comment.getId())))
                .thenReturn(Optional.of(comment));

        service.edit(serviceModel);

        ArgumentCaptor<Comment> argument = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(argument.capture());
        final Comment editedComment = argument.getValue();

        Assert.assertEquals(comment.getId(), editedComment.getId());
        Assert.assertEquals(comment.getUploader(), editedComment.getUploader());
        Assert.assertEquals(comment.getInstant(), editedComment.getInstant());
        Assert.assertEquals(serviceModel.getContent(), editedComment.getContent());
    }

    @Test
    void edit_WhenEditModelWithInvalidNameLength_ShouldThrow() {
        CommentEditServiceModel serviceModel = new CommentEditServiceModel();
        serviceModel.setContent("");

        Assert.assertThrows(ValidationException.class, () ->
                service.edit(serviceModel));

        serviceModel.setContent(INVALID_LONG_CONTENT);

        Assert.assertThrows(ValidationException.class, () ->
                service.edit(serviceModel));
    }

    @Test
    void deleteById_ShouldRemoveTheCorrectComment() {
        Comment comment = TestUtils.getRandomListValue(comments);
        Mockito.when(commentRepository.findById(eq(comment.getId())))
                .thenReturn(Optional.of(comment));

        service.delete(comment.getId());

        verify(commentRepository).delete(comment);
    }

    private CommentServiceModel buildServiceModel(Comment comment) {
        CommentServiceModel serviceModel = new CommentServiceModel();
        serviceModel.setId(comment.getId());
        serviceModel.setInstant(comment.getInstant());
        serviceModel.setUploaderUsername(comment.getUploader().getUser().getUsername());
        serviceModel.setContent(comment.getContent());
        return serviceModel;
    }

    private void assertEqualsComments(Comment comment, CommentServiceModel actualComment) {
        Assert.assertEquals(comment.getId(), actualComment.getId());
        Assert.assertEquals(comment.getContent(), actualComment.getContent());
        Assert.assertEquals(comment.getInstant(), actualComment.getInstant());
    }
}