package genesislab.ai.homework.service;

import genesislab.ai.homework.repository.UserRepository;
import genesislab.ai.homework.repository.VideoRepository;
import genesislab.ai.homework.repository.entity.Role;
import genesislab.ai.homework.repository.entity.User;
import genesislab.ai.homework.repository.entity.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class VideoServiceTest {

    @Autowired
    private VideoService service;

    @MockBean
    private VideoRepository repository;

    @MockBean
    private UserRepository userRepository;

    @Nested
    @DisplayName("비디오 재생할 때")
    class WhenGetVideo {
        @Test
        @DisplayName("내가 생성한 비디오만 볼 수 있음")
        void videoUserPosted() {
            // given
            Long videoId = 1L;
            Long uploaderUserId = 1L;

            Optional<User> uploader = getUser(uploaderUserId);
            Optional<Video> video = getVideo(videoId, uploader.get());

            // stub
            given(repository.findById(videoId)).willReturn(video);
            given(userRepository.findById(uploaderUserId)).willReturn(uploader);

            // when
            String videoTitle = service.getVideo(videoId, uploaderUserId);

            // then
            assertThat(videoTitle).isEqualTo(video.get().getTitle());
        }

        @Test
        @DisplayName("내가 생성한 비디오가 아니면 볼 수 없음")
        void videoUserDidNotPost() {
            // given
            Long videoId = 1L;
            Long uploaderUserId = 1L;
            Long requestorUserId = 2L;

            Optional<User> uploader = getUser(uploaderUserId);
            Optional<User> requestor = getUser(requestorUserId);
            Optional<Video> video = getVideo(videoId, uploader.get());

            // stub
            given(repository.findById(videoId)).willReturn(video);
            given(userRepository.findById(requestorUserId)).willReturn(requestor);

            // then
            assertThatThrownBy(() -> service.getVideo(videoId, requestorUserId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Role getUserRole() {
        return Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
    }

    private Optional<User> getUser(Long id) {
        User user = User.builder()
                .id(id)
                .email("user1@gmail.com")
                .password("1234")
                .role(getUserRole())
                .build();

        return Optional.of(user);
    }

    private Optional<Video> getVideo(Long id, User user) {
        Video video = Video.builder()
                .id(id)
                .user(user)
                .title("20230101abc.mp4")
                .build();
        return Optional.of(video);
    }
}