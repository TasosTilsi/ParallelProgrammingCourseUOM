public class ComputePI {

    public String compute(String inputNumSteps) {

        int systemCores = Runtime.getRuntime().availableProcessors();
        int NUMBER_OF_THREADS = systemCores;
        long numSteps = Long.parseLong(inputNumSteps);

        /* start timing */
        long startTime = System.currentTimeMillis();

        /* do computation */
        double step = 1.0 / (double) numSteps;
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        SharedData myData = new SharedData();

        long myIndex = numSteps / NUMBER_OF_THREADS;


        /* create and start threads */
        for (int i = 1; i <= NUMBER_OF_THREADS; i++) {
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
        return printData(numSteps, (double) (endTime - startTime) / 1000, pi, NUMBER_OF_THREADS);
    }

    private double doComputation(double step, long myFirstIndex, long myLastIndex) {
        double sum = 0.0;
        for (long i = myFirstIndex; i < myLastIndex; ++i) {
            double x = ((double) i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }
        return sum;
    }

    public String printData(long numSteps, double time, double pi, long numberOfThreads) {
        String outputMessage = String.format("Number of Threads=%d; Steps=%d; Time=%f; Diff=%22.20f; Comp PI=%22.20f;", numberOfThreads, numSteps, time, Math.abs(pi - Math.PI), pi);
//        System.out.printf(outputMessage);
        return outputMessage;
    }
}
