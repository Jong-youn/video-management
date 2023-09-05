package genesislab.ai.homework.controller.model.response;


import lombok.Getter;

@Getter
public class CommonRes<T> {

    private T data;

    public CommonRes(T data) {
        this.data = data;
    }
}
