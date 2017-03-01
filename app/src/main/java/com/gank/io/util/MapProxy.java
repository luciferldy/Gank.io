package com.gank.io.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by Lucifer on 2016/12/22.
 * 对 Map 进行封装，当调用 Map 中的某个方法时返回回执
 * 基于反射的代理？基于继承的代理？
 */
@Deprecated
public class MapProxy implements InvocationHandler {

    private Map map;

    private MapProxy(Map map) {
        this.map = map;
    }

    private Map getProxy() {
        return (Map) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Map.class}, this);
    }

    public static Map newProxy(Map map) {
        return new MapProxy(map).getProxy();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        String methodName = method.getName();
        if (methodName.equalsIgnoreCase("put")) {
            this.beforeInput(args);
            result = method.invoke(map, args);
            this.afterInput(result);
        }
        return result;
    }

    protected void beforeInput(Object[] args) {

    }

    protected void afterInput(Object result) {

    }
}
