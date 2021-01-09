package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.dto.UserDto;
import com.hanwei.mapper.DeptMapper;
import com.hanwei.mapper.RoleMapper;
import com.hanwei.utils.AppMd5Utils;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hanwei.domain.User;
import com.hanwei.mapper.UserMapper;
import com.hanwei.service.UserService;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.COL_PHONE, phone);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getOne(Long userId) {
        return userMapper.selectById(userId);
    }


    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        Page<User> page=new Page<>(userDto.getPageNum(), userDto.getPageSize());
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(userDto.getUserName()), User.COL_USER_NAME, userDto.getUserName());
        qw.like(StringUtils.isNotBlank(userDto.getPhone()), User.COL_PHONE, userDto.getPhone());
        qw.eq(StringUtils.isNotBlank(userDto.getStatus()), User.COL_STATUS, userDto.getStatus());
        qw.eq(userDto.getDeptId()!=null, User.COL_DEPT_ID, userDto.getDeptId());
        qw.ge(null!= userDto.getBeginTime(), User.COL_CREATE_TIME, userDto.getBeginTime());
        qw.le(null!= userDto.getEndTime(), User.COL_CREATE_TIME, userDto.getEndTime());
        qw.orderByAsc(User.COL_USER_ID);
        this.userMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int addUser(UserDto userDto) {
        User user=new User();
        BeanUtil.copyProperties(userDto,user);
        user.setUserType(Constants.USER_NORMAL);
        String defaultPwd=user.getPhone().substring(5);
        user.setCreateBy(userDto.getSimpleUser().getUserName());
        user.setCreateTime(DateUtil.date());
        user.setSalt(AppMd5Utils.createSalt());
        user.setPassword(AppMd5Utils.md5(defaultPwd, user.getSalt(),2));
        return this.userMapper.insert(user);
    }

    @Override
    public int updateUser(UserDto userDto) {
        User user=this.userMapper.selectById(userDto.getUserId());
        if(null==user){
            return 0;
        }
        BeanUtil.copyProperties(userDto,user);
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        return this.userMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        List<Long> ids = Arrays.asList(userIds);
        //根据用户IDS删除sys_role_user里面的数据
        this.roleMapper.deleteRoleUserByUserIds(ids);
        return this.userMapper.deleteBatchIds(ids);
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_STATUS, Constants.STATUS_TRUE);
        qw.eq(User.COL_USER_TYPE,Constants.USER_NORMAL);
        qw.orderByAsc(User.COL_USER_ID);
        return this.userMapper.selectList(qw);
    }
    @Override
    public void resetPassWord(Long[] userIds) {
        for (Long userId : userIds) {
            User user=this.userMapper.selectById(userId);
            String defaultPwd="";
            if (user.getUserType().equals(Constants.USER_ADMIN)){
                defaultPwd="123456";
            }else{
                defaultPwd=user.getPhone().substring(user.getPhone().length() - 6);
            }
            user.setSalt(AppMd5Utils.createSalt());
            user.setPassword(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
            this.userMapper.updateById(user);
        }
    }

    @Override
    public List<User> querySchedulingUsers(Long userId, Long deptId) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(null!=deptId, User.COL_DEPT_ID,deptId);
        qw.eq(null!=userId, User.COL_USER_ID,userId);
        qw.eq(User.COL_SCHEDULING_FLAG, Constants.SCHEDULING_FLAG_TRUE);
        return this.userMapper.selectList(qw);
    }
}
