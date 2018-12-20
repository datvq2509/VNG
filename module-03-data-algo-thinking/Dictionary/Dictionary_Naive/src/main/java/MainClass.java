
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class MainClass {
    public static void main(String[] args ) throws IOException {
        // Path to the .txt file
        Path path;

        {
            path = Paths.get("dataset.txt").toAbsolutePath();
        }

        List<String> titles = Files.lines(path).collect(Collectors.toList());
        String searchword = getInput();

        //Show Ket Qua

        displayResults(searchword, titles);
    }
    public static String getInput(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a word to search in dataset : ");
        String titlename = sc.nextLine();
        sc.close();

        return titlename;
    }




    public static void displayResults(final String searchname, List<String> titles ) throws IOException {

        boolean infile =titles.stream().anyMatch(p->p.equalsIgnoreCase(searchname));

        if(infile){
            System.out.println("\nYes , " + searchname + "is in the input file !");
        } else {
            System.out.println("\nNo , " + searchname + "is not in the input file !");
        }

    }
}