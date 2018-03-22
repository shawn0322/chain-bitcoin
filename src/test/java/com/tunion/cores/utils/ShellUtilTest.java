package com.tunion.cores.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Think on 2018/1/26.
 */
public class ShellUtilTest implements Runnable{

    private static Logger log = LoggerFactory.getLogger(ShellUtilTest.class);

    public  static int successRequest = 0;
    public  static int failRequest = 0;

    private CountDownLatch begin;
    private CountDownLatch end;

    ShellUtilTest(CountDownLatch begin, CountDownLatch end) {
        this.begin = begin;
        this.end = end;
    }

    private  static synchronized void incrementSuccessCount(){
        successRequest++;
    }

    private  static synchronized void incrementFailCount(){
        failRequest++;
    }

    private static synchronized void printSucessCount()
    {
        //System.out.println("success:"+successRequest);
        int cnt = successRequest;
        log.info("success:"+cnt);
    }

    @Override
    public void run() {

        try {
            begin.await();

            String shStr = "bitcoin-cli getbalance";

            String os = System.getProperty("os.name").toLowerCase();

            if (os.indexOf("windows") >= 0) {
                shStr = "bitcoin-cli listtransactions \"*\" 10 0 true";
            }else{
                shStr = "./bitcoin-cli listtransactions \"*\" 10 0 true";
            }

            boolean bflag=false;

            try {
                bflag = true;
                printSucessCount();
//                List<String> list = ShellUtil.runShell(shStr);
//
//                if (!list.isEmpty())
//                {
//                    bflag = true;
//                }
//                String retStr="";
//                for(String line:list)
//                {
//                    retStr+=line;
//                }
//
//                retStr.replaceAll("bip125-replaceable","bip125Replaceable");
//
//                List<Transactions> lstTrans=JsonSerializer.readListBean(retStr,List.class, Transactions.class);
//
//                Results results = new Results("0","sucess",lstTrans);
//
//                System.out.println(JacksonUtil.getJackson(results));
//
//                //System.out.println(retStr);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bflag) {
                incrementSuccessCount();
            } else {
                incrementFailCount();
            }
        } catch (Exception e) {

        } finally {
            end.countDown();
        }

    }

    public static void multiThreadTest()
    {
        CountDownLatch begin = new CountDownLatch(1);

        //设置最大的并发数量为60
        ExecutorService exec = Executors.newFixedThreadPool(10);

        int totalCnt = 100;
        CountDownLatch end = new CountDownLatch(totalCnt);
        for (int i=0;i<totalCnt;i++) {
            exec.execute(new ShellUtilTest(begin, end));
        }
        long startTime = System.currentTimeMillis();
        //当60个线程，初始化完成后，解锁，让六十个线程在4个双核的cpu服务器上一起竞争着跑，来模拟60个并发线程访问tomcat
        begin.countDown();

        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("the success size: " + ShellUtilTest.successRequest);
            log.info("the fail size: " + ShellUtilTest.failRequest);

            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            log.info("the total time cost is: " + costTime + " ms");
        }
        exec.shutdown();
        log.info("main method end");
    }

    public static void test()
    {
        String shStr = "bitcoin-cli getbalance";

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("windows") >= 0) {
            shStr = "bitcoin-cli getnewaddress1";
        }else{
            shStr = "./bitcoin-cli listtransactions \"*\" 10 0 true";
        }

        try {
                List<String> list = ShellUtil.runShell(shStr);

                String retStr="";
                for(String line:list)
                {
                    retStr+=line;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //test();
        String fee="0.00100000";
        Double dfee = new Double(fee);
        Double re=dfee*1000000;
        System.out.println(re.longValue());
    }
}
