import mpi.MPI;

class CountSortArgsTimeMpj {

    public static void main(String[] args) {
        MPI.Init(args);
        int coreSize = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        int size = 10;

        int[] a = new int[size];
        int[] b = new int[size];

        if (rank==0) {
            for (int i = 0; i < size; i++) {
//            a[i] = i;
                a[i] = (int) (Math.random() * size);
                b[i] = 0;
            }
        }

        MPI.COMM_WORLD.Bcast(a, 0, size, MPI.INT, 0);
//        MPI.COMM_WORLD.Bcast(b, 0, size, MPI.INT, 0);
        int[] localB = new int[size];

        for (int i = 0; i < size; i++) {
            localB[i] = 0;
        }

        /* for debugging */
        if (rank==1) {
            for (int i = 0; i < size; i++)
                System.out.println(a[i]);
        }

        // get current time
        long start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            int mynum = a[i];
            int myplace = 0;


            for (int j = rank; j < size; j+=rank)
                if (mynum > a[j] || (mynum == a[j] && j < i))
                    myplace++;
            localB[myplace] = mynum;
        }

        MPI.COMM_WORLD.Reduce(localB, 0, b, 0, size, MPI.INT, MPI.SUM, 0);

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - start;


        if (rank==0) {
            System.out.println("time in ms = " + elapsedTimeMillis);

            /* for debugging */
            for (int i = 0; i < size; i++)
                System.out.println(b[i]);
        }

        MPI.Finalize();
    }
}
