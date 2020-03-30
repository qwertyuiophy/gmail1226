package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {

    public static void main(String[] args) {

        getUser_info();

        /*//client_id   2994188124
        //http://passport.gmall.com:8087/vlogin
        //String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=2994188124&response_type=code&redirect_uri=http://passport.gmall.com:8087/vlogin");
        //System.out.println(s1);

        //授权码   http://passport.gmall.com:8087/vlogin?code=9e9cee9a0815a238b0d12b8ae15d11e7
        //授权码code需要更新，有过期问题
        String s2 = "http://passport.gmall.com:8087/vlogin?code=220db343a20d3b12fc25acf1bd5addea";

        //用code交换acessToken
        //secret : 7ee3fb9069bc211469bfb0381e8599a1
        //String s3 = "https://api.weibo.com/oauth2/access_token?client_id=2994188124&client_secret=7ee3fb9069bc211469bfb0381e8599a1&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8087/vlogin&code=CODE";
        String s3 = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","2994188124");
        paramMap.put("client_secret","7ee3fb9069bc211469bfb0381e8599a1");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8087/vlogin");
        paramMap.put("code","42ea9f8560941938c826ad64cb1c8b7d");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        *//*String access_token = HttpclientUtil.doPost(s3, paramMap);

        Map<String,String> access_map = JSON.parseObject(access_token, Map.class);
        System.out.println(access_map.get("access_token"));*//*



        //用access_token获取用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.003xpx4GqG_dQD39e718059a8BGMmB&uid=1";
        String user_json = HttpclientUtil.doGet(s4);
        Map user_map = JSON.parseObject(user_json, Map.class);
        System.out.println(user_map.get("1"));*/
    }

    public static String getCode(){

        // 1 获得授权码


        //client_id   2994188124
        //http://passport.gmall.com:8087/vlogin
        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=2994188124&response_type=code&redirect_uri=http://passport.gmall.com:8087/vlogin");

        System.out.println(s1);

        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程

        // 2 返回授权码到回调地址

        return null;
    }

    public static String getAccess_token(){
        // 3 换取access_token
        //secret : 7ee3fb9069bc211469bfb0381e8599a1
        //String s3 = "https://api.weibo.com/oauth2/access_token?client_id=2994188124&client_secret=7ee3fb9069bc211469bfb0381e8599a1&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8087/vlogin&code=CODE";
        String s3 = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","2994188124");
        paramMap.put("client_secret","7ee3fb9069bc211469bfb0381e8599a1");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8087/vlogin");
        paramMap.put("code","42ea9f8560941938c826ad64cb1c8b7d");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token = HttpclientUtil.doPost(s3, paramMap);

        Map<String,String> access_map = JSON.parseObject(access_token, Map.class);
        System.out.println(access_map.get("access_token"));

        System.out.println(access_map.get("access_token"));
        System.out.println(access_map.get("uid"));

        return access_map.get("access_token");
    }

    public static Map<String,String> getUser_info(){

        // 4 用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.003xpx4GqG_dQD39e718059a8BGMmB&uid=1";
        String user_json = HttpclientUtil.doGet(s4);
        Map user_map = JSON.parseObject(user_json, Map.class);
        System.out.println(user_map.get("1"));

        return user_map;
    }



}
