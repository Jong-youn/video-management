package genesislab.ai.homework.controller;


import genesislab.ai.homework.controller.model.request.VideoCreateReq;
import genesislab.ai.homework.controller.model.response.CommonRes;
import genesislab.ai.homework.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final VideoService service;

    @PostMapping
    @ResponseBody
    public CommonRes<Long> create(VideoCreateReq request, Principal principal) {
        request.setUserId(Long.valueOf(principal.getName()));
        Long id = service.create(request);
        return new CommonRes<>(id);
    }

    @GetMapping("/{videoId}")
    public String getImage(@PathVariable long videoId, Model model, Principal principal) {
        validateVideoId(videoId);

        Long userId = Long.valueOf(principal.getName());
        String fileName = service.getVideo(videoId, userId);
        model.addAttribute("fileName", fileName);
        return "video/video";
    }

    private void validateVideoId(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("잘못된 접근입니다. 다시 시도해주세요.");
        }
    }

}
