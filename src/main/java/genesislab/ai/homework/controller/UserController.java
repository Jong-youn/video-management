package genesislab.ai.homework.controller;

import genesislab.ai.homework.controller.model.request.UserInfoUpdateReq;
import genesislab.ai.homework.controller.model.request.UserSignupReq;
import genesislab.ai.homework.controller.model.response.CommonRes;
import genesislab.ai.homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public CommonRes<Long> signup(@RequestBody @Valid UserSignupReq request) {
        Long id = service.signup(request);
        return new CommonRes<>(id);
    }

    @PatchMapping
    public void updateInfo(@RequestBody UserInfoUpdateReq request, Principal principal) {
        request.setId(Long.valueOf(principal.getName()));
        service.updateInfo(request);
    }

    @DeleteMapping
    public void delete(Principal principal) {
        service.delete(Long.valueOf(principal.getName()));
    }
}
