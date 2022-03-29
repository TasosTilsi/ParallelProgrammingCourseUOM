public class NumIntPar {

    public static void main(String[] args) {

        int systemCores = Runtime.getRuntime().availableProcessors();
        System.out.println(String.format("This system has [%d] Cores", systemCores));
        int NUMBER_OF_THREADS = systemCores;
        long numSteps = 0;

        /* parse command line */
//        numSteps = Long.parseLong("100000000000");
        if (args.length != 1) {
            System.out.println("arguments:  number_of_steps");
            System.exit(1);
        }
        try {
            numSteps = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("argument " + args[0] + " must be long int");
            System.exit(1);
        }

        /* start timing */
        long startTime = System.currentTimeMillis();

        /* do computation */
        double step = 1.0 / (double) numSteps;
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        SharedData myData = new SharedData();

        long myIndex = numSteps / NUMBER_OF_THREADS;

//        System.out.println(step + " : " + myIndex + " = " + numSteps + " / " + NUMBER_OF_THREADS + "\nmyIndex mod 4 = " + myIndex % 4);

        /* create and start threads */
        for (int i = 1; i <= NUMBER_OF_THREADS; i++) {
//            System.out.println("In main: create and start thread " + i);
            int finalI = i;
            long finalNumSteps = numSteps;
            threads[i - 1] = new Thread() {
                @Override
                public void run() {
                    super.run();
                    double inThreadSum = 0.0;
                    if (finalI == NUMBER_OF_THREADS) {

                        inThreadSum = doComputation(step, myIndex * (finalI - 1), finalNumSteps);

                    } else {
                        inThreadSum = doComputation(step, myIndex * (finalI - 1), myIndex * finalI);
                    }

                    myData.getGlobalSumLock().lock();
                    try {
                        myData.setGlobalSum(myData.getGlobalSum() + inThreadSum);
                    } finally {
                        myData.getGlobalSumLock().unlock();
                    }
                }
            };
            threads[i - 1].start();
        }

        /* wait for threads to finish */
        for (int i = 1; i <= NUMBER_OF_THREADS; i++) {
            try {
                threads[i - 1].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        double pi = myData.getGlobalSum() * step;

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        printData(numSteps, (double) (endTime - startTime) / 1000, pi, NUMBER_OF_THREADS);
    }

    public static double doComputation(double step, long myFirstIndex, long myLastIndex) {
        double sum = 0.0;
        for (long i = myFirstIndex; i < myLastIndex; ++i) {
            double x = ((double) i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
//        System.out.println(myFirstIndex + "|---|" + myLastIndex + "|---|" + staticSum * step);
        return sum;
    }

    private static void printData(long numSteps, double time, double pi, long numberOfThreads) {
//        System.out.printf("sequential program results with %d steps\n", numSteps);
//        System.out.printf("computed pi = %22.20f\n" , pi);
//        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
//        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
        System.out.printf("Number of Threads;\t\tSteps;\t\tTime;\t\tDiff;\t\tComp PI;\n");
        System.out.printf("%d;\t\t%d;\t\t%f;\t\t%22.20f;\t\t%22.20f;\n", numberOfThreads, numSteps, time, Math.abs(pi - Math.PI), pi);
    }
}