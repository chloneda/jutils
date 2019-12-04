package com.chloneda.jutils.test;

/**
 * @author chloneda
 * @description: 同步代码块测试
 */
public class CodeBlock implements Runnable {
    @Override
    public void run() {
        synchronized (CodeBlock.class) {
            System.out.print("同步代码块!");
        }
    }

    public static void main(String[] args) {
        CodeBlock a = new CodeBlock();
        CodeBlock b = new CodeBlock();
        new Thread(a).start();
        new Thread(b).start();
    }
}

