package com.miaoshaprojecet;

import com.miaoshaprojecet.dao.UserDOMapper;
import com.miaoshaprojecet.dataobject.UserDO;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 */

@SpringBootApplication(scanBasePackages = {"com.miaoshaprojecet"})  //自动配置的一个功能，自动启动Tomcat，加载一个默认的配置
@RestController             //SpringMVC的一个控制功能
@MapperScan("com.miaoshaprojecet.dao")

public class App {

    @Autowired
    private UserDOMapper userDOMapper;

//    @RequestMapping("/")
//    public String test(){
//        return "hello world!";
//    }

    @RequestMapping("/")    //当用户访问根路径的时候
    public String Home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null)
            return "no exsit";
        else return userDO.getName();
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(App.class, args);
    }
}
