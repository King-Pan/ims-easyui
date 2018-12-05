package club.javalearn.ims.repository;

import club.javalearn.ims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/6/22
 * Time: 下午5:50
 * Description: No Description
 */
@Transactional(rollbackFor = RuntimeException.class)
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, JpaSpecificationExecutor<User> {

    /**
     * 通过用户名查找用户
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User findByUserName(String userName);


    /**
     * 通过用户编号查询用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    User findByUserId(Long userId);

    /**
     * 批量删除用户
     * @param userIds 用户ID列表
     * @return 成功记录数
     */
    @Query(value = "update  User u set u.status=2  where u.userId in (:userIds) ")
    @Modifying
    int deleteUserByUserIds(@Param("userIds") List<Long> userIds);
}
