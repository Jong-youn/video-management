package genesislab.ai.homework.service;

import genesislab.ai.homework.controller.model.request.VideoCreateReq;
import genesislab.ai.homework.repository.UserRepository;
import genesislab.ai.homework.repository.VideoRepository;
import genesislab.ai.homework.repository.entity.User;
import genesislab.ai.homework.repository.entity.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository repository;
    private final UserRepository userRepository;

    @Value("${storage.path}")
    private String STORAGE_PATH;
    private static final String DEFAULT_EXTENSION = ".mp4";
    private static final Long MAX_VIDEO_SIZE = 104857600L;

    public Long create(VideoCreateReq request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            throw new AccessDeniedException("로그인 해주시기 바랍니다.");
        }

        String fileName = downloadVideo(request.getVideo());

        Video video = Video.builder()
                .user(user.get())
                .title(fileName)
                .createdAt(getNowLocalDateTime())
                .build();
        repository.save(video);
        return video.getId();
    }

    public String getVideo(Long videoId, Long userId) {
        Optional<Video> optionalVideo = repository.findById(videoId);
        Optional<User> optionalRequestUser = userRepository.findById(userId);

        if (optionalVideo.isEmpty() || optionalRequestUser.isEmpty()) {
            throw new IllegalArgumentException("잘못된 접근입니다. 다시 시도해주세요.");
        }

        Video video = optionalVideo.get();
        User uploader = video.getUser();
        User requestor = optionalRequestUser.get();
        if (uploader == null || isImproperAccess(uploader, requestor)) {
            log.warn("부적절한 비디오 접근시도가 의심됩니다.");
            throw new IllegalArgumentException("비디오을 찾을 수 없습니다. 다시 시도해주세요.");
        }

        return video.getTitle();
    }

    private boolean isImproperAccess(User uploader, User requestor) {
        if (requestor.getRole().getName().equals("ROLE_ADMIN")) {
            return false;
        }
        return !requestor.getId().equals(uploader.getId());
    }

    private String downloadVideo(MultipartFile video) {
        if (video.isEmpty()) {
            throw new IllegalArgumentException("비디오 파일을 첨부해주시기 바립니다.");
        }

        long size = video.getSize();

        if (size > MAX_VIDEO_SIZE) {
            throw new MaxUploadSizeExceededException(100);
        }

        File directory = new File(STORAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileNameWithExtension = getFileNameWithExtension(video.getOriginalFilename());
        Path destinationFile = Paths.get(STORAGE_PATH).resolve(Paths.get(fileNameWithExtension)).normalize().toAbsolutePath();

        try (InputStream inputStream = video.getInputStream()){
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileNameWithExtension;
    }

    private String getFileNameWithExtension(String originalFilename) {
        return generateFileName() + getExtension(originalFilename);
    }

    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStamp = dateFormat.format(new Date());

        String randomString = UUID.randomUUID().toString().replace("-", "").substring(0,6);
        return timeStamp + randomString;
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return DEFAULT_EXTENSION;
        }

        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            return "." + parts[parts.length - 1];
        }

        return DEFAULT_EXTENSION;
    }

    private LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}

