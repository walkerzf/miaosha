package com.miaoshaprojecet.service.impl;


import com.miaoshaprojecet.dao.UserDOMapper;
import com.miaoshaprojecet.dao.UserPasswordDoMapper;
import com.miaoshaprojecet.dataobject.UserDO;
import com.miaoshaprojecet.dataobject.UserPasswordDo;
import com.miaoshaprojecet.error.BusinessException;
import com.miaoshaprojecet.error.EmBusinessError;
import com.miaoshaprojecet.service.Model.UserModel;
import com.miaoshaprojecet.service.UserService;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdommapper获取对应的用户dataobject，为什么不直接返回对象呢
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        //userPasswordDoMapper也是selectPrimaryKey根据id查询密码
        //但是业务的逻辑需求需要根据id查询加密的密码
        //所以这里要改变userPasswordMapper接口中的方法
        //这里做一个判空处理
        if (userDO == null) return null;
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDo);
    }

    //逼传的校验
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        //校验是为了代码的更好的完全性，也是对之后的改进的一个方法
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //https://mvnrepository.com/artifact/org.apache.commons/commons-lang3/3.7
        if (StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelephone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //不为空之后开始真正的注册,注册实际上是把数据写入数据库，你说是不是..
        UserDO userDO = new UserDO();
        //实现model->dataobject的方法
        userDO = convertFromUserModel(userModel);
        //使用user的insertselective
        userDOMapper.insertSelective(userDO);
        userModel.setId(userDO.getId());
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        userPasswordDo=convertFromPasswordModel(userModel);
        userPasswordDoMapper.insertSelective(userPasswordDo);
        return;
        /*
        使用insertSelective的原因是在xml中可以看到Select的方式insert的时候会判断插入的字段是否为null
        如果为null，则跳过这个字段，即保存数据库中的默认值，
          这种操作在update的时候比较实用
         */
    }

    private UserPasswordDo convertFromPasswordModel(UserModel userModel) {
        if (userModel == null) return null;
        UserPasswordDo userPasswordDo = new UserPasswordDo();
        userPasswordDo.setUserPassword(userModel.getUserPassword());
        //id很重要，将password和id关联
        userPasswordDo.setUserId(userModel.getId());
        return userPasswordDo;
    }


    //userModel转换为UserDO的方法
    private UserDO convertFromUserModel(UserModel userModel) {

        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    //将数据层模型中转变为Service中的UserModel
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDo userPasswordDo) {
        if (userDO == null) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if (userPasswordDo != null) {
            userModel.setUserPassword(userPasswordDo.getUserPassword());
        }
        return userModel;
    }
}
