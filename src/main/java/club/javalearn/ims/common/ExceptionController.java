package club.javalearn.ims.common;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-03
 * Time: 15:58
 * Description: No Description
 */
@RestControllerAdvice
public class ExceptionController {

    /**
     * 捕捉shiro的异常
     *
     * @param e shiro异常
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ServerResponse handle401(ShiroException e) {
        return ServerResponse.createByErrorCodeMessage(401, e.getMessage(), null);
    }

    /**
     * 认证失败异常
     *
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ServerResponse handle401() {
        return ServerResponse.createByErrorCodeMessage(401, "您没有权限，请联系管理员", null);
    }


    /**
     * 捕捉其他所有异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse globalException(HttpServletRequest request, Throwable ex) {
        return ServerResponse.createByErrorCodeMessage(getStatus(request).value(), ex.getMessage(), null);
    }

    /**
     * 获取Http状态码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
