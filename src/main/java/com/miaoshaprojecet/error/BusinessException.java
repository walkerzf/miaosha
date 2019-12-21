package com.miaoshaprojecet.error;

/**
 * 包装器业务异常类实现
 * BusinessException和EmBusinessError都共同集成了CommonError对应的方法
 * 外部不仅通过new 上面两者都可以由errorCode和ErrorMsg的组装定义
 * 共同实现SerErrMsg的方法将原本EM中定义的ErrMsg覆盖掉
 */

public class BusinessException extends Exception implements CommonError {
    //这里需要强关联一个CommonError的类，

    private CommonError commonError;

    //构造方法
    //直接接受EmBuinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        //调用super方法，因为Exception里面会有一些初始化的方法
        super();
        this.commonError=commonError;
    }

    //接受自定义errMsg的方式构造业务异常
    public BusinessException (CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
