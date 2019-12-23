package com.miaoshaprojecet.controller;

import com.miaoshaprojecet.controller.viewObject.UserVo;
import com.miaoshaprojecet.error.BusinessException;
import com.miaoshaprojecet.error.EmBusinessError;
import com.miaoshaprojecet.response.CommonReturnType;
import com.miaoshaprojecet.service.Model.UserModel;
import com.miaoshaprojecet.service.UserService;
import org.apache.catalina.User;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true" ,allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //。。。并不是单例模式》。。

    //用户注册接口,注册的时候需要都绑定
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSeesionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        //druib里面队以字符串是否为空有一个判空处理
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSeesionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证不符合");
        }
        //用户的注册流程
        UserModel userModel=new UserModel();
        userModel.setAge(age);
        userModel.setGender( new Short(String.valueOf( gender.intValue())));
        userModel.setName(name);
        userModel.setTelephone(telphone);
        userModel.setRegisterMode("byeTelphone");
        //加密password 存在数据库中,getBytes
        userModel.setUserPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        //加密字符串
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    //getotp的方法必须映射到post的请求才能生效
    //method的元素其实是个数组
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {

        //需要按照一定规则生成OTP验证码,采用随机数方法
        Random random = new Random();
        int randomint = random.nextInt(99999);
        randomint += 100;
        String otpCode = String.valueOf(randomint);

        //将OTP验证码同对应用户手机号关联 ,Key-value 格式，使用httppsession的方式绑定它的手机号和Otpcode
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        //将OTP验证码通过短信通道发送给用户 省略..
        System.out.println("telephone" + telphone + "&otpCode" + otpCode);
        //这里在实际的项目中是不能这么做的，因为将敏感信息打印到日志中，全部暴露给了企业端

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    //定义通用的返回格式之后
    //public UserVo getUser(@RequestParam(name = "id") Integer id) {
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        //这里直接处理业务逻辑的模型直接返回了..不科学
        //return userModel;

        //若获取的对应用户信息不存在
        if (userModel == null) {
            userModel.setUserPassword("123");
            //throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVo userVo = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserModel userModel) {
        //首先判空
        if (userModel == null) return null;
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        //这里copy就行了
        return userVo;
    }


}
