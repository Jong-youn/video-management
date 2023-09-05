package genesislab.ai.homework.controller.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VideoCreateReq {

    private Long userId;
    private MultipartFile video;
}

