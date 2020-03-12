package client;

public class HomeWorkLesson4 {
    static final Object monitor = new Object();
    static int currentNum = 1;
    static final int num = 5;

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            try {
                for (int j = 0; j < num; j++) {
                    synchronized (monitor) {
                        while (currentNum != 1) {
                            monitor.wait();
                        }
                        System.out.print("A");
                        currentNum = 2;
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                for (int j = 0; j < num; j++) {
                    synchronized (monitor) {
                        while (currentNum != 2) {
                            monitor.wait();
                        }
                        System.out.print("B");
                        currentNum = 3;
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                for (int j = 0; j < num; j++) {
                    synchronized (monitor) {
                        while (currentNum != 3) {
                            monitor.wait();
                        }
                        System.out.print("C");
                        currentNum = 1;
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
