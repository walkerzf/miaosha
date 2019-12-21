package com.miaoshaprojecet.service;


import com.miaoshaprojecet.error.BusinessException;
import com.miaoshaprojecet.service.Model.UserModel;
import org.apache.catalina.User;


public interface UserService {
    //通过用户id获取用户对象的方法,数据库对应的有两张表，不能直接返回给前端
    //服务层就是写UserService返回真正的UserModel
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
}
