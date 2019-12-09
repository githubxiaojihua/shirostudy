package com.xiaojihua.strategy;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;

import java.util.Collection;

public class C02AtLestTwoStrategy extends AbstractAuthenticationStrategy {
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }

    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else if (aggregateInfo == null) {
            info = singleRealmInfo;
        } else {
            info = this.merge(singleRealmInfo, aggregateInfo);
        }

        return info;
    }


    /**
     * 要实现至少两个realm验证成功才算是成功，需要在afterAllAttempts方法中设置
     * 
     * @param token
     * @param aggregate
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        /**
         * 判断是认证成功的返回是否为空或者数量是否小于2，是的话抛出异常
         */
        if(aggregate == null || CollectionUtils.isEmpty(aggregate.getPrincipals()) || aggregate.getPrincipals().asList().size() < 2){
            throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                    "could not be authenticated by any configured realms.  Please ensure that at least two realm can " +
                    "authenticate these tokens.");
        }
        return aggregate;
    }
}
