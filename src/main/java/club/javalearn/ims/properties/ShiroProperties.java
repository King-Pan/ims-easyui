package club.javalearn.ims.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author king-pan
 * Date: 2018/6/22
 * Time: 下午5:37
 * Description: Shiro配置属性
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {
    private int expire = 30;
    PasswordProperties password = new PasswordProperties();
}
