import mpi.*;
//import java.util.Scanner;

/**
 * Compile: javac -cp $MPJ_HOME/lib/mpj.jar:. program.java 
 * Execute in multicore mode:
			 mpjrun.sh -np 4 program
 * Execute in clustrer mode: 
			1)create a file name it
			  machines
			2) add name or IP of machines i.e.
			127.0.0.1
			3) start deamons
			mpjboot machines
			4) run program.. make sure machines file is in the same directory
			mpjrun.sh -np 4 Pi_MPJ
			5) stop daemons
			mpjhault machines
 */

public class CpiMpj {

	public static void main(String[] args) throws Exception {
		MPI.Init(args);
		int size = MPI.COMM_WORLD.Size();
		int rank = MPI.COMM_WORLD.Rank();
		
		int[] n = new int[1];
		if (rank == 0) {
			//Scanner keyboard = new Scanner(System.in);
			//System.out.println("enter number of steps");
			//n = keyboard.nextInt();
			n[0] = 100000000;
		}
		long totalTime = System.currentTimeMillis();
		
		MPI.COMM_WORLD.Bcast(n,0,1,MPI.INT,0);
		
		
		double h = 1.0 / (double)n[0];
        double sum = 0.0;
		int chunk = n[0]/size;
		int start = rank*chunk;
		int stop = start+chunk;
		if (rank == size-1) stop = n[0];
		
		for (int i = start; i < stop; i++) {
			double x = h * ((double)i - 0.5);
			sum += (4.0 / (1.0 + x * x));
		}
		double[] localpi = new double[1]; 
		localpi[0] = sum * h;
		
		double[] globalpi = new double[1];
		MPI.COMM_WORLD.Reduce(localpi,0,globalpi,0,1,MPI.DOUBLE,MPI.SUM,0);
    
		totalTime = System.currentTimeMillis() - totalTime;

		if (rank == 0) {
			System.out.println("Value of pi: " + globalpi[0]);
			System.out.println("Calculated in " + totalTime + " milliseconds");
		}
		MPI.Finalize();
	}
}


