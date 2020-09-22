package com.mumu;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyTest {
    public static void main(String[] args)  {
        LiuDeHua liuDeHua = new LiuDeHua();
        Star star = (Star) Proxy.newProxyInstance(LiuDeHua.class.getClassLoader(), new Class[]{Star.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("singSong")){
                    Double price = (Double) args[0];
                    Double realPrice = price * 0.75;
                    System.out.println("刘德华唱歌收了" + price + "钱");
                    liuDeHua.singSong(100.00);
                    System.out.println("经济公司挣了" + (price - realPrice) + "钱");
                    return null;
                }else{
                    return method.invoke(liuDeHua,args);
                }

            }
        });
        star.playMovie(100.00);
        star.singSong(100.00);
    }
}

