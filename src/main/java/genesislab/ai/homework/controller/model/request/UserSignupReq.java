package genesislab.ai.homework.controller.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserSignupReq {

    private static final String EMAIL_PATTERN = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$";

    @NotBlank
    @Pattern(regexp = EMAIL_PATTERN, message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String name;

    private String phoneNumber;

    @NotBlank
    private String password;
}
