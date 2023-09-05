package genesislab.ai.homework.controller.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {

    private String email;
    private String password;
}