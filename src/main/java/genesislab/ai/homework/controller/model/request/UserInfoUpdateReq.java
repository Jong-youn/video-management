package genesislab.ai.homework.controller.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateReq {

    private Long id;
    private String name;
    private String phoneNumber;
}
