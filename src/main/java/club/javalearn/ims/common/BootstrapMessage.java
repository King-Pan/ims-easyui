package club.javalearn.ims.common;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/11
 * Time: 下午1:32
 * Description: No Description
 */
@Data
public class BootstrapMessage<T>  implements Message {
    private List<T> rows;
    private Integer start;
    private Integer limit;
    private Long total;
}
