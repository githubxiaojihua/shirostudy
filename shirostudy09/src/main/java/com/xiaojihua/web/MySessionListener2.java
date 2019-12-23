package com.xiaojihua.web;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

/**
 * 通过扩展SessionListenerAdapter来实现监听某一事件
 */
public class MySessionListener2 extends SessionListenerAdapter {
    @Override
    public void onStart(Session session){
        System.out.println("会话创建时触发2：" + session.getId());
    }
}
