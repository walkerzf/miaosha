package com.miaoshaprojecet.response;


public class CommonReturnType {

    //表明对应请求的返回处理结果  success或者fail

    private String status;
    //若status为success，则data内返回前端需要的json数据
    //若status为fail，则data内使用通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    //如果不带参数，默认成功
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    //带参数，返回通用的格式
    public static CommonReturnType create(Object result,String status){
       CommonReturnType type=new CommonReturnType();
       type.setStatus(status);
       type.setData(result);
       return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
