package club.javalearn.ims.service.impl;

import club.javalearn.ims.common.BootstrapMessage;
import club.javalearn.ims.common.Message;
import club.javalearn.ims.entity.Role;
import club.javalearn.ims.entity.User;
import club.javalearn.ims.entity.info.UserInfo;
import club.javalearn.ims.repository.RoleRepository;
import club.javalearn.ims.repository.UserRepository;
import club.javalearn.ims.service.UserService;
import club.javalearn.ims.utils.Constants;
import club.javalearn.ims.utils.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/6/22
 * Time: 下午5:50
 * Description: No Description
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User save(UserInfo userInfo) {
        User user = userInfo.convertUser();
        User result;
        if (user.getUserId() == null) {
            //生成加密密码
            passwordHelper.encryptPassword(user, true);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setStatus(Constants.DEFAULT_STATUS);
            result = userRepository.save(user);
        } else {
            User u = userRepository.getOne(user.getUserId());
            u.setUpdateTime(new Date());
            u.setNickName(user.getNickName());
            u.setUserName(user.getUserName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPhoneNum());
            result = userRepository.save(u);
        }

        return result;
    }

    @Override
    public void deleteRole(Long userId, List<Long> roleId) {
        User user = userRepository.findByUserId(userId);
        Set<Role> roles = user.getRoles();
        roles.removeAll(roleRepository.findAllById(roleId));
        userRepository.save(user);
    }

    @Override
    public void updateLastLoginTime(User user) {
        //更新登录信息u
        user.setLastLoginTime(new Date());
        userRepository.save(user);
    }

    @Override
    public void addRole(Long userId, List<Long> roleId) {
        User user = userRepository.findByUserId(userId);
        Set<Role> roles = user.getRoles();
        roles.addAll(roleRepository.findAllById(roleId));
        userRepository.save(user);
    }


    @Override
    public Message<User> getPageList(UserInfo userInfo, Pageable pageable) {
        BootstrapMessage<User> message = new BootstrapMessage<>();
        final User user = userInfo.convertUser();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "updateTime"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "status"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "userId"));
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(orders));

        Page<User> userPage = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> nickNamePath = root.get("nickName");
                Path<String> userNamePath = root.get("userName");
                Path<String> statusPath = root.get("status");
                List<Predicate> wherePredicate = new ArrayList<>();
                //final User user = convertUser(param);
                if (user != null) {
                    if (StringUtils.isNoneBlank(user.getNickName())) {
                        wherePredicate.add(cb.like(nickNamePath, "%" + user.getNickName() + "%"));
                    }
                    if (StringUtils.isNoneBlank(user.getUserName())) {
                        wherePredicate.add(cb.like(userNamePath, "%" + user.getUserName() + "%"));
                    }
                    if (StringUtils.isNoneBlank(user.getStatus()) && !Constants.NOT_SELECT.equals(user.getStatus())) {
                        wherePredicate.add(cb.equal(statusPath, user.getStatus()));
                    }
                }

                Predicate[] predicates = new Predicate[]{};
                //这里可以设置任意条查询条件
                if (CollectionUtils.isNotEmpty(wherePredicate)) {
                    query.where(wherePredicate.toArray(predicates));
                }
                //这种方式使用JPA的API设置了查询条件，所以不需要再返回查询条件Predicate给Spring Data Jpa，故最后return null;即可。
                return null;
            }
        }, pageRequest);

        message.setLimit(userPage.getSize());
        message.setRows(userPage.getContent());
        message.setStart(userPage.getNumber());
        message.setTotal(userPage.getTotalElements());

        log.debug("limit=" + userPage.getSize() + ",total=" + userPage.getTotalElements() + ",start=" + userPage.getNumber() + ",numberOfElements=" + userPage.getNumberOfElements());
        return message;
    }

    @Override
    public int deleteUsers(List<Long> userIds) {
        return userRepository.deleteUserByUserIds(userIds);
    }
}
