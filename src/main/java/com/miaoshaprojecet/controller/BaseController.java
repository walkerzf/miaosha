package com.miaoshaprojecet.controller;

import com.miaoshaprojecet.error.BusinessException;
import com.miaoshaprojecet.error.EmBusinessError;
import com.miaoshaprojecet.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    //拦截异常
    //定义exceptionhandler解决未被controller层吸收的exception
    //不能返回之前那样的ResponseBody这样的model
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    //返回object的时，是会寻找本地路径上的一些文件
    public Object handlerException(HttpServletRequest request, Exception ex) {
        //如果返回的不是业务异常
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            //首先将异常强制转换为一个业务逻辑异常
            BusinessException businessException = (BusinessException) ex;
            /**
             CommonReturnType commonReturnType=new CommonReturnType();
             commonReturnType.setStatus("fail");
             */
            responseData.put("erroCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {

            responseData.put("erroCode", EmBusinessError.UNKNOW_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOW_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData, "failed");
    }
}
