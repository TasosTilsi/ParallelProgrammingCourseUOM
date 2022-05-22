import mpi.MPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class StringHistogramMpj {

    public static void main(String args[]) throws IOException {
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        String fileString = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/../resources/chromosome.txt")));//, StandardCharsets.UTF_8);
        int alphabetSize = 256;
        int n = fileString.length();
        char[] text = new char[n+50];
        int[] histogram = new int[alphabetSize];
        double totalTime = 0.0;

        totalTime = MPI.Wtime();


        for (int i = 0; i < alphabetSize; i++) {
            histogram[i] = 0;
        }

        char[] localText = new char[(n / size) + (n % size)];

        MPI.COMM_WORLD.Scatter(text, 0, size, MPI.CHAR, localText, 0, size, MPI.CHAR, 0);
        //-----------------------------

        System.out.println(String.format("I am rank %d from size %d, and i have localText.length = %d", rank, size, localText.length));

        for (int i = 0; i < localText.length; i++) {
            localText[i] = fileString.charAt(i);
        }

        MPI.COMM_WORLD.Barrier();

        for (char c : localText) {
            histogram[(int) c]++;
        }


        MPI.COMM_WORLD.Gather(localText, 0, localText.length, MPI.CHAR, text, 0, localText.length, MPI.CHAR, 0);
        MPI.COMM_WORLD.Barrier();

        if (rank == 0) {
            System.out.printf("text.length = %d%n", text.length);
        }

        //-----------------------------

        MPI.COMM_WORLD.Reduce(histogram, 0, histogram, 0, alphabetSize, MPI.INT, MPI.SUM, 0);
//        MPI.COMM_WORLD.Bcast(histogram, 0, alphabetSize, MPI.INT, 0);

        if (rank == 0) {
            for (int i = 0; i < histogram.length; i++) {
                System.out.println((char) i + "-->" + histogram[i]);
            }
//            Arrays.stream(histogram).distinct().forEach(System.out::println);
            System.out.println("The execution time was: " + (MPI.Wtime() - totalTime) + " seconds");
        }


        MPI.Finalize();
    }
}
