package com.xiaojihua.web;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * 通过实现SessionListener来实现各个事件的监听
 */
public class MySessionListener1 implements SessionListener {
    @Override
    public void onStart(Session session) {
        System.out.println("会话创建时触发：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        System.out.println("退出或者会话过期时触发：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("会话过期时触发：" + session.getId());
    }
}
