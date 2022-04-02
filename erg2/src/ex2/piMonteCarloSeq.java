package ex2;

public class piMonteCarloSeq {

    public static void main(String[] args) {

        long numSteps = 0;

        numSteps = Long.parseLong("100000000");

        /* start timing */
        long startTime = System.currentTimeMillis();

        /* do computation */
        long count = 0;
        for (long i = 0; i < numSteps; ++i) {
            double x = Math.random();
            double y = Math.random();
            double z = Math.pow(x, 2) + Math.pow(y, 2);
            if (z <= 1.0) count++;
        }
        System.out.println(count);
        double pi = (double) count / numSteps * 4;

        /* end timing and print result */
        long endTime = System.currentTimeMillis();
        System.out.printf("sequential program results with %d steps\n", numSteps);
        System.out.printf("computed pi = %22.20f\n", pi);
        System.out.printf("difference between estimated pi and Math.PI = %22.20f\n", Math.abs(pi - Math.PI));
        System.out.printf("time to compute = %f seconds\n", (double) (endTime - startTime) / 1000);
    }
}
