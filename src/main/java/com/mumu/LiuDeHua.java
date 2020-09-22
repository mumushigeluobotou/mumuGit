package com.mumu;

public class LiuDeHua implements Star {
    @Override
    public void singSong(Double price) {
        System.out.println("我刘德华唱歌要" + price + "钱");
    }

    @Override
    public void playMovie(Double price) {
        System.out.println("我刘德华演戏要" + price + "钱");
    }
}
