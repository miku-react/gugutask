package org.fengling.gugutask.util;

import lombok.Data;

@Data
public class R<T> {
    private int code;
    private String message;
    private T data;

    public R() {
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 快捷成功响应方法
    public static <T> R<T> success(T data) {
        return new R<>(20039, "成功", data);
    }

    // 通用错误响应方法
    public static <T> R<T> error(String message) {
        return new R<>(50039, message, null);
    }

    // 404 Not Found 错误
    public static <T> R<T> notFound(String message) {
        return new R<>(40439, message, null);
    }

    // 403 Forbidden 错误
    public static <T> R<T> forbidden(String message) {
        return new R<>(40339, message, null);
    }

    // 401 Unauthorized 错误
    public static <T> R<T> unauthorized(String message) {
        return new R<>(40139, message, null);
    }

    // 自定义状态码和消息
    public static <T> R<T> customError(int code, String message) {
        return new R<>(code, message, null);
    }
}
