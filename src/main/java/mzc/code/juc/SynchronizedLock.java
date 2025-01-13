package mzc.code.juc;

import java.util.concurrent.TimeUnit;

/**
 * 下面通过代码演示了使用 synchronized 的情况
 * <p>
 *  一个对象里面如果有多个 synchronized 方法，某一时刻内，只要有一个线程去调用其中的一个 synchronized 方法，
 *  其他的线程都只能等待，换句话说，某一时刻内只能有唯一一个线程去访问这些 synchronized 方法
 * <p>
 *    synchronized 实现同步的基础：
 *    java 中的每一个对象都可以作为锁。具体表现为以下三种形式：
 *      对于普通同步方法，锁的是当前实例对象，也就是当前对象的this
 *      对于同步方法块，锁的是synchronized括号里配置的对象
 *      对于静态同步方法，锁的是当前类的Class对象
 *
 *    静态同步方法与普通同步方法不存在竞态关系，静态同步方法获取的是类锁，普通同步方法获取的是this锁
 *
 */
public class SynchronizedLock {

    public static void main(String[] args) {

        staticSimpleMultiResourceAccess();

    }

    /**
     * 标准访问：不同线程操作同一资源类
     * 当前场景下先打印 sendEmail，锁的是资源类对象的 this
     */
    public static void simpleAccess() {
        Phone phone = new Phone();
        new Thread(() -> phone.sendEmail(), "sendEmail").start();

        new Thread(() -> phone.sendSms(), "sendSms").start();
    }

    /**
     * 无锁访问：同步方法与非同步方法竞争
     * 非同步方法与同步方法间不存在竞态关系
     */
    public static void unLockAccess() {
        Phone phone = new Phone();
        new Thread(() -> phone.sendEmail(), "sendEmail").start();

        new Thread(() -> phone.sayHello(), "sayHello").start();
    }

    /**
     * 多资源访问：多资源类直接锁竞争
     * 多资源场景下访问实例同步方法，不存在竞态关系
     */
    public static void multiResourceAccess() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> phone1.sendEmail(), "sendEmail").start();

        new Thread(() -> phone2.sendSms(), "sayHello").start();
    }

    /**
     * 静态访问：两个静态同步方法，同一资源类访问
     * 当前场景下先打印 sendEmail，对于静态同步方法，锁的是当前类的Class对象
     */
    public static void staticAccess() {
        Phone phone = new Phone();
        new Thread(() -> phone.sendEmailStatic(), "sendEmail").start();

        new Thread(() -> phone.sendSmsStatic(), "sayHello").start();
    }

    /**
     * 多资源静态访问：两个静态同步方法，多个资源类访问
     * 当前场景下先打印 sendEmail，对于静态同步方法，锁的是当前类的Class对象
     */
    public static void multiResourceStaticAccess() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> phone1.sendEmailStatic(), "sendEmail").start();

        new Thread(() -> phone2.sendSmsStatic(), "sayHello").start();
    }

    /**
     * 静态标准访问：一个静态同步方法，一个普通同步方法，同一资源类
     * 静态同步方法与普通同步方法不存在竞态关系，静态同步方法获取的是类锁，普通同步方法获取的是this锁
     */
    public static void staticSimpleAccess() {
        Phone phone = new Phone();
        new Thread(() -> phone.sendEmailStatic(), "sendEmail").start();

        new Thread(() -> phone.sendSms(), "sayHello").start();
    }

    /**
     * 静态标准多资源访问：一个静态同步方法，一个普通同步方法，多个资源类
     * 静态同步方法与普通同步方法不存在竞态关系，静态同步方法获取的是类锁，普通同步方法获取的是this锁
     */
    public static void staticSimpleMultiResourceAccess() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> phone1.sendEmailStatic(), "sendEmail").start();

        new Thread(() -> phone2.sendSms(), "sayHello").start();
    }

}


class Phone {

    public synchronized void sendEmail() {
        System.out.println("====== sendEmail ======");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendSms() {
        System.out.println("====== sendSms ======");
    }

    public void sayHello() {
        System.out.println("====== sayHello ======");
    }

    public synchronized static void sendEmailStatic() {
        System.out.println("====== sendEmail ======");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void sendSmsStatic() {
        System.out.println("====== sendSms ======");
    }

}