package demo.lucius.javatestlib;

/***
 * 用于测试线程之间的通信
 */
public class TestThread {

    public static Object object = new Object();


    static class Thread1 extends Thread {

        @Override
        public void run() {
//            super.run();
            synchronized (object) {
                System.out.println("线程" + Thread.currentThread().getName());
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "阻塞");
                    object.wait();
                } catch (InterruptedException e) {

                }
                System.out.println("线程" + Thread.currentThread().getName() + "执行完成");
            }

        }
    }


    static class Thread2 extends Thread {
        @Override
        public void run() {
//            super.run();
            System.out.println("线程" + Thread.currentThread().getName());
            System.out.println("线程" + Thread.currentThread().getName() + "唤醒正在阻塞的线程");
            object.notify();
            System.out.println("线程" + Thread.currentThread().getName() + "执行完成");
        }
    }

    /**
     * 0-1背包问题
     *

     * @return
     */
    public static int ZeroOnePack(int[] weight, int zeroIndex) {

        if (zeroIndex < weight.length) {
            weight[zeroIndex] = 0;

        }
        int sum = 0;
        int V = 0;
        for (int i = 0; i < weight.length; i++) {
            V += weight[i];
        }
        sum = V;
        V /= 2;
        int[] value = weight;
        int N = weight.length;

        //初始化动态规划数组
        int[][] dp = new int[N + 1][V + 1];
        //为了便于理解,将dp[i][0]和dp[0][j]均置为0，从1开始计算
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < V + 1; j++) {
                //如果第i件物品的重量大于背包容量j,则不装入背包
                //由于weight和value数组下标都是从0开始,故注意第i个物品的重量为weight[i-1],价值为value[i-1]
                if (weight[i - 1] > j)
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight[i - 1]] + value[i - 1]);
            }
        }
        //则容量为V的背包能够装入物品的最大值为
        int maxValue = dp[N][V];
        //逆推找出装入背包的所有商品的编号
        int j = V;
        String numStr = "";

        int numTmp = 0;
        for (int i = N; i > 0; i--) {
            //若果dp[i][j]>dp[i-1][j],这说明第i件物品是放入背包的
            if (dp[i][j] > dp[i - 1][j]) {
                numStr = weight[i - 1] + " " + numStr;
                numTmp = weight[i - 1] + numTmp;
                j = j - weight[i - 1];
            }
            if (j == 0)
                break;
        }
        System.out.println(sum - 2 * numTmp);

        return sum - 2 * numTmp;
//        return numStr;
    }

    /**

     * @return
     */
    public static int ZeroOnePack2(int[] weight) {

        int V = 0;
        for (int i = 0; i < weight.length; i++) {
            V += weight[i];
        }
        V /= 2;
        int N = weight.length;

        //动态规划
        int[] dp = new int[V + 1];
        for (int i = 1; i < N + 1; i++) {
            //逆序实现
            for (int j = V; j >= weight[i - 1]; j--) {
                dp[j] = Math.max(dp[j - weight[i - 1]] + 1, dp[j]);
                System.out.println(dp[j]);
            }
        }
        return dp[V];
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread1 thread1 = new Thread1();
//        Thread2 thread2 = new Thread2();
//        thread1.start();
//        Thread.sleep(2000);
//        thread2.start();

        //用于表示重量
        int[] weight = {9, 10, 11, 12};

//        int [weight.length] tmp={0};
        int[] tmp = weight;
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = 0;
        }

//        ZeroOnePack(weight);
        int result=0;

        for (int i = 0; i < weight.length; i++) {
            tmp[i] = ZeroOnePack(weight, i);
        }

        int allWeight=ZeroOnePack(weight,weight.length);
    }

}
