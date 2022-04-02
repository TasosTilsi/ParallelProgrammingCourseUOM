package ex4;

import java.util.Random;

class BackSubArgsTime
{
	public static void main(String[] args)
	{  
		
        int size = 0;
        if (args.length != 1) {
	        System.out.println("Usage: java CountSortArgsTime <problem size>");
	        System.exit(1);
		}

		try {
			size = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException nfe) {
	   		System.out.println("Integer argument expected");
	    	System.exit(1);
		}
        if (size <= 0) {
            System.out.println("size should be positive integer");
	        System.exit(1);
		}

        double[][] a = new double[size][size];
        double[] b = new double[size];
        double[] x = new double[size];
        
        //random doubles between 1.0 and 2.0
        Random r = new Random();
		for(int i = 0; i < size; i++){
		    x[i] = 0.0;
		    b[i] = 1.0 + (2.0 - 1.0) * r.nextDouble(); 
		    a[i][i] = 2.0 + (1.0 + (2.0 - 1.0) * r.nextDouble()); 
		    for (int j = 0; j < i; j++) 
			     a[i][j] = 1.0 + (2.0 - 1.0) * r.nextDouble(); 
        }
		
 
		// get current time 
        long start = System.currentTimeMillis();

		for (int i = 0; i < size; i++) {
		    double sum = 0.0;
		    for (int j = 0; j < i; j++) {
			     sum = sum + (x[j] * a[i][j]);
		    }	
		    x[i] = (b[i] - sum) / a[i][i];
	    }
   
        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis()-start;
        System.out.println("time in ms = "+ elapsedTimeMillis);

		/* for debugging */
		for(int i = 0; i < size; i++) 
			System.out.println(x[i]); 
	}

}
