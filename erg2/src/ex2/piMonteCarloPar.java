package ex2;

import java.util.concurrent.ThreadLocalRandom;

public class piMonteCarloPar {

    public static void main(String[] args) {

        int systemCores = Runtime.getRuntime().availableProcessors();
        System.out.println(String.format("This system has [%d] Cores", systemCores));

        long numSteps = 0;

        numSteps = Long.parseLong("100000000");

//        start timing
        Thread[] threads = new Thread[systemCores];
        SharedData myData = new SharedData();

        long myIndex = numSteps / systemCores;

        long startTime = System.currentTimeMillis();

//        create and start threads
        for (int i = 1; i <= systemCores; i++) {
//            System.out.println("In main: create and start thread " + i);
            int finalI = i;
            long finalNumSteps = numSteps;
            threads[i - 1] = new Thread() {
                @Override
                public void run() {
                    super.run();
                    int inThreadCount = 0;
                    if (finalI == systemCores) {

                        inThreadCount = doComputation(myIndex * (finalI - 1), finalNumSteps);

                    } else {

                        inThreadCount = doComputation(myIndex * (finalI - 1), myIndex * finalI);

                    }
//                    System.out.println("inThreadCount " + inThreadCount);
                    myData.setGlobalCount(myData.getGlobalCount() + inThreadCount);
                }
            };
            threads[i - 1].start();
        }

//        wait for threads to finish
        for (int i = 1; i <= systemCores; i++) {
            try {
                threads[i - 1].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
        System.out.println(myData.getGlobalCount());
        double pi = (double) myData.getGlobalCount() / numSteps * 4;

//        end timing and print result
        long endTime = System.currentTimeMillis();
        System.out.printf("parallel program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n", pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }

    public static int doComputation(long myFirstIndex, long myLastIndex) {
//        System.out.println(myFirstIndex + "---" + myLastIndex);
        int prec = 100000000;
        int count = 0;
        for (long i = myFirstIndex; i <= myLastIndex; ++i) {
            double x = ThreadLocalRandom.current().nextInt(prec + 1) / (double) prec;
            double y = ThreadLocalRandom.current().nextInt(prec + 1) / (double) prec;
            double z = Math.pow(x, 2) + Math.pow(y, 2);
            if (z <= 1.0) count++;
        }
//        System.out.println("count " + count);
        return count;
    }
}
