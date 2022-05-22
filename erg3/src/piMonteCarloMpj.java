import mpi.MPI;
//import java.util.Scanner;

/**
 * Compile: javac -cp $MPJ_HOME/lib/mpj.jar:. program.java
 * Execute in multicore mode:
 * mpjrun.sh -np 4 program
 * Execute in clustrer mode:
 * 1)create a file name it
 * machines
 * 2) add name or IP of machines i.e.
 * 127.0.0.1
 * 3) start deamons
 * mpjboot machines
 * 4) run program.. make sure machines file is in the same directory
 * mpjrun.sh -np 4 Pi_MPJ
 * 5) stop daemons
 * mpjhault machines
 */

public class piMonteCarloMpj {

    public static void main(String[] args) {
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        double totalTime = 0.0;
        double computationalTime = 0.0;
        int[] numSteps = new int[1];
        numSteps[0] = 1000000000;


        totalTime = MPI.Wtime();


//        MPI.COMM_WORLD.Bcast(numSteps, 0, 1, MPI.INT, 0);

        computationalTime = MPI.Wtime();

        /* do computation */
        long count = 0;
        long latestI = 0;
        for (long i = rank; i <= numSteps[0]; i += size) {
            double x = Math.random();
            double y = Math.random();
            double z = Math.pow(x, 2) + Math.pow(y, 2);
            if (z <= 1.0) count++;
            latestI = i;
        }


        double[] localPi =new double[1];
        localPi[0] = (double) count / numSteps[0] * 4;

        double[] globalPi = new double[1];
        globalPi[0] = 0;

        System.out.println(String.format("I am rank %d from size %d, i have counted until %d and my localPi = %f",rank,size,latestI,localPi[0]));
        computationalTime = MPI.Wtime() - computationalTime;

        MPI.COMM_WORLD.Reduce(localPi, 0, globalPi, 0, 1, MPI.DOUBLE, MPI.SUM, 0);


        if (rank == 0) {
            System.out.printf("%d steps%n", numSteps[0]);
            System.out.printf("computed pi = %22.20f%n", globalPi[0]);
            System.out.printf("difference between estimated pi and Math.PI = %e%n", Math.abs(globalPi[0] - Math.PI));
            System.out.printf("totalTime = %f seconds%n", (MPI.Wtime() - totalTime));
            System.out.printf("computationalTime = %f seconds%n", computationalTime);
        }

        MPI.Finalize();
    }
}
