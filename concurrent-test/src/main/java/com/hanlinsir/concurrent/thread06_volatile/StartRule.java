package com.hanlinsir.concurrent.thread06_volatile;

public class StartRule {

    static int x=0;

    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            //use x=10
        });

        x=10;

        t1.start();
    }
}
