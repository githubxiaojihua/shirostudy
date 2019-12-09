package com.xiaojihua.strategy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * 每个AuthenticationStrategy实例都是无状态的，因此在每次调用的时候都会将认证信息
 * AuthenticationInfo，通过参数进行传递
 *
 * 本类实现只允许有一个realm认证成功，否则抛出异常中断认证，认为认证失败
 */
public class C01OnlyOneStrategy extends AbstractAuthenticationStrategy {

    /**
     * 在所有realm验证之前调用
     * @param realms
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();//返回一个权限的认证信息
    }

    /**
     * 在每个realm验证之前调用
     * @param realm
     * @param token
     * @param aggregate
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;//返回之前合并的
    }

    /**
     * 在每个realm验证之后调用
     * @param realm
     * @param token
     * @param singleRealmInfo
     * @param aggregateInfo
     * @param t
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        /**
         * 判断当前realm返回的singleRealmInfo是否为空
         * 可如果为空则返回上次传递的已合并的aggregateInfo
         * 如果singleRealmInfo不为空并且上次传递的已合并aggrregateInfo为空
         * 则返回singleRealmInfo.
         * 若两者都不为空则合并，并返回。
         */
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else if (aggregateInfo == null) {
            info = singleRealmInfo;
        } else {
            info = this.merge(singleRealmInfo, aggregateInfo);
            //判断如果认证通过的realm>1则抛出异常
            if(info.getPrincipals().asList().size() > 1){
                System.out.println(info.getPrincipals().getRealmNames());
                throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                        "could not be authenticated by any configured realms.  Please ensure that only one realm can " +
                        "authenticate these tokens.");
            }
        }

        return info;
    }

    /**
     * 在所有realm验证之后调用
     * @param token
     * @param aggregate
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;//返回之前合并的
    }

}
