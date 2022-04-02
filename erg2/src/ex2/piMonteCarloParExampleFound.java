package ex2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class Task implements Callable<Integer> {
    final int points;

    public Task(int points) {
        this.points = points;
    }

    @Override
    public Integer call() {
        int insidePoints = 0;
        int prec = 100000000;
        for (int i = 0; i < points; ++i) {
            double x = (double) ThreadLocalRandom.current().nextInt(prec + 1) / (double) prec;
            double y = (double) ThreadLocalRandom.current().nextInt(prec + 1) / (double) prec;
            if ((x * x + y * y) <= 1) {
                ++insidePoints;
            }
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " running and Inside points are " + insidePoints);
        return insidePoints;
    }
}

class A {
    public static void main(String[] args) {
        int totalPoints = 100000000;
        int threadsToBeUsed = Runtime.getRuntime().availableProcessors();

        int eachThreadPoints = totalPoints / threadsToBeUsed;
        ExecutorService pool = Executors.newFixedThreadPool(threadsToBeUsed);
        List<Future<Integer>> results = new ArrayList<>(threadsToBeUsed);
        for (int i = 0; i < threadsToBeUsed; i++) {
            Future<Integer> insidePointsThr = pool.submit(new Task(eachThreadPoints));
            results.add(insidePointsThr);
        }
        int insidePoints = results.stream().mapToInt(A::getFutureResult).sum();

        System.out.println("Number of inside points :" + insidePoints);
        System.out.println("Pi/4 = " + (double) insidePoints / (double) totalPoints);
        System.out.println("Pi   = " + 4 * (double) insidePoints / (double) totalPoints);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(4 * (double) insidePoints / (double) totalPoints - Math.PI));

        pool.shutdown();
    }

    private static int getFutureResult(Future<Integer> f) {
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            // handle the error
        }
        return 0;
    }
}