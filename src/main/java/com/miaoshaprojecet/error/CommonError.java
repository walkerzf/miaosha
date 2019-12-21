package com.miaoshaprojecet.error;

public interface CommonError {
    //接口的方法
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);

}
