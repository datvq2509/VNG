import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass
{
    public static void main(String[] args)throws IOException
    {
        Scanner in = new Scanner(System.in);
        String path = "";
        System.out.print("Please type your input to create Trie Tree: ");
        path = in.nextLine();
        File fi = new File(path);
        BufferedReader br = new BufferedReader(new FileReader (fi));

        Trie trie=new Trie();

        String text;

        while ((text = br.readLine()) != null)
            trie.insert(text);

        br.close();


        String wordfind;
        System.out.print ("Please type word you want predict : ");
        wordfind = in.nextLine();
        System.out.println("Top 5 predictive text is : ");

        ArrayList<String> prefixList= trie.GetPredict(wordfind);

        for(int i=0;i<prefixList.size();i++){
            System.out.println(prefixList.get(i));
        }
    }
}
