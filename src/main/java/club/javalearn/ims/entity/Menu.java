package club.javalearn.ims.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 14:40
 * Description: easyui 菜单实体从权限表转换过来
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键字段，菜单id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String text;
    /**
     * 菜单图标
     */
    private String iconCls;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 排序字段
     */
    private Integer orderValue;

    private List<Menu> children = new ArrayList<>();

    private Attribute attributes;


    public Menu(Permission permission) {
        this.id = permission.getPermissionId();
        this.text = permission.getPermissionName();
        this.orderValue = permission.getOrderNum();
        this.iconCls = permission.getIcon();
        this.parentId = permission.getParentId();
        if (StringUtils.isNoneBlank(permission.getUrl())) {
            this.attributes = new Attribute(permission.getUrl());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Attribute implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 属性url
         */
        private String url;

    }
}
