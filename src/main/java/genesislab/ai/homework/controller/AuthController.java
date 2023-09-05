package genesislab.ai.homework.controller;

import genesislab.ai.homework.controller.model.request.LoginReq;
import genesislab.ai.homework.controller.model.response.TokenRes;
import genesislab.ai.homework.repository.entity.User;
import genesislab.ai.homework.security.JwtTokenProvider;
import genesislab.ai.homework.service.UserAuthDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserAuthDetailService userDetailService;


    @PostMapping
    public ResponseEntity<TokenRes> login(@RequestBody LoginReq request) {
        final User user = userDetailService.authenticateByEmailAndPassword(request.getEmail(), request.getPassword());
        final String token = jwtTokenProvider.generateToken(String.valueOf(user.getId()));
        return ResponseEntity.ok(new TokenRes(token));
    }
}

