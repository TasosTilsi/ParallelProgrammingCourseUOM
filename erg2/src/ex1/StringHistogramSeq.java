package ex1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class StringHistogramSeq {

    public static void main(String[] args) throws IOException {

        double startTime = System.currentTimeMillis();

        String fileString = new String(Files.readAllBytes(Paths.get("/home/tasostilsi/Development/Projects/pamak/parallelProgramming/erg2/resources/chromosome.txt")));//, StandardCharsets.UTF_8);
        char[] text = new char[fileString.length()];
        long n = fileString.length();
        for (int i = 0; i < n; i++) {
            text[i] = fileString.charAt(i);
        }

        int alphabetSize = 256;
        int[] histogram = new int[alphabetSize];
        for (int i = 0; i < alphabetSize; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < n; i++) {
            histogram[text[i]]++;
        }

        /*for (int i = 0; i < alphabetSize; i++) {
            System.out.println(histogram[i]);
        }*/

        Arrays.stream(histogram).distinct().forEach(System.out::println);

        double endTime = System.currentTimeMillis();

        System.out.println("The execution time was: " + (endTime - startTime) / 1000.0 / 60.0 + " seconds");
    }
}



