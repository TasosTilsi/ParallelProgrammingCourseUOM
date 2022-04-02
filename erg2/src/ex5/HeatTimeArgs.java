package ex5;/* Simple 2D Heat equation using a       */
/* 4 point stencil and point heat source */

class HeatTimeArgs
{
  public static void main(String args[])
  {
    int size = 0;
    int iterations = 0;

    if (args.length != 2) {
	System.out.println("Usage: java HeatTimeArgs <size> <iterations>");
	System.exit(1);
    }

    try {
	size = Integer.parseInt(args[0]);
        iterations = Integer.parseInt(args[1]);
    }
    catch (NumberFormatException nfe) {
	System.out.println("Integer argument expected");
	System.exit(1);
    }
    if (size <= 0) {
	System.out.println("Attention: <size> should be >0");
	System.exit(1);
    }
    if (iterations <= 0) {
	System.out.println("Attention: <iterations> should be >0");
	System.exit(1);
    }
 
    /* initialization */
    double[][] table1 = new double[size][size];
    double[][] table2 = new double[size][size];
    for (int i = 0; i < size; i++)
    	for (int j = 0; j < size; j++){
	    table1[i][j] = 0;
            table2[i][j] = 0;
        }
    
    // get current time
    long start = System.currentTimeMillis();

    int row = 1;
    int col = 1;
    double heat_source = 1;
    double diff1 = 1.0, diff2;
    double maxDiff = 0.0000000001;
    for(int k = 0; k < iterations; k++) {

	  /* create a heat source : can be active for some iterations only */
	  table1[row][col] = heat_source;
	  
	  /* difference initialization */
	  diff2 = 0.0;

	  /* perform the calculations */
	  for(int i=1;i<size-1;i++)
	    for(int j=1;j<size-1;j++) {
	      table2[i][j] = 0.25 *(table1[i-1][j] + table1[i+1][j] + table1[i][j-1] + table1[i][j+1]);
	      diff2 += (table2[i][j]-table1[i][j])*(table2[i][j]-table1[i][j]);
	    }

	  /* print result 
	  for(int i=0;i<size;i++)
	  {
	    for(int j=0;j<size;j++)
	      System.out.printf("%12.10f ", table2[i][j]);
	    System.out.println();
	  }
	  System.out.println();*/
	 
	  /* print difference and check convergence */
       	  diff2 = Math.sqrt(diff2)/(size*size);
	  System.out.println("diff1 = "+ diff1 + " diff2 = " + diff2);

	  if ((diff1 - diff2) <= maxDiff) {
		System.out.println("Convergence in " + k +" iterations");
	        break;
          }

          diff1 = diff2;
          /* copy new table to old table */ 
	  for(int i=0;i<size;i++)
	    for(int j=0;j<size;j++)
	      table1[i][j]=table2[i][j];
	 
    }	
 
    // get current time and calculate elapsed time
    long elapsedTimeMillis = System.currentTimeMillis()-start;
    System.out.println("time in ms = "+ elapsedTimeMillis);

   }
}