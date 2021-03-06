package club.javalearn.ims.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/6/22
 * Time: 下午5:38
 * Description: No Description
 */
@Getter
@Setter
@ToString
public class PasswordProperties {
    /**
     * 加密次数
     */
    private int hashIterations = 10;
    /**
     * 加密算法
     */
    private String algorithmName = "sha-1";
    /**
     * 默认密码
     */
    private String defaultPassword = "888888";

}
