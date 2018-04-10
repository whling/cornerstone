package com.whl.cornerstone.zookeeper.leader.proxy;

import com.whl.cornerstone.util.ReflectionUtils;
import com.whl.cornerstone.zookeeper.leader.LeaderManager;
import com.whl.cornerstone.zookeeper.leader.event.LeaderEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by whling on 2018/4/12.
 */
public class OnceEventJDKProxy
        implements InvocationHandler {
    private Object target;

    public Object bind(Object target) {
        this.target = target;

        Class<?>[] interfaces = {LeaderEvent.class};

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaces, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result = null;

        result = method.invoke(this.target, args);
        LeaderManager.getInstance()
                .clearDrmData((String) ReflectionUtils.getFieldValue(this.target, "nodeName"));
        return result;
    }
}