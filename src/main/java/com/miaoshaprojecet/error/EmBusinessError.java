package com.miaoshaprojecet.error;

public enum EmBusinessError implements CommonError {

    //之后在这里在进行各种的定制化错误类型就可以了...

    //通用错误类型10001

    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),
    //20000开头为用户信息相关错误定义
    //分布式开发时的错误码更需要在一个通用的文件中进行实现
    USER_NOT_EXIST(20001,"用户不存在")

    ;

    private  EmBusinessError(int errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    //Java里面的枚举是可以拥有成员变量的，本质上是一个面向对象的类
    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
