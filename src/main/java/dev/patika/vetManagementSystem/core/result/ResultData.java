package dev.patika.vetManagementSystem.core.result;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class ResultData<T> extends Result  {

    private T data;

    public ResultData(boolean status, String message, String code, T data) {
        super(status, message, code);
        this.data = data;
    }

    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public static <T> ResultData<T> error(String message, String code) {
        return new ResultData<>(false, message, code, null);

    }

    public static ResultData<Object> notFoundError(String message, String code) {
        return new ResultData<>(false, message, code, null);
    }

}
