package org.fengling.gugutask.handler;

import org.fengling.gugutask.util.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理文件大小超过限制的异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置返回的 HTTP 状态码
    @ResponseBody
    public R<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return R.error("文件大小超过最大限制，请上传小于5MB的文件！");
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R<?> handleGenericException(Exception ex) {
        ex.printStackTrace(); // 打印异常堆栈信息，便于调试
        return R.error("服务器内部错误，请稍后再试！");
    }
}