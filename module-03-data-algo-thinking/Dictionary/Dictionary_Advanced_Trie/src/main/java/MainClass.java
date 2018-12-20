import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MainClass {


    public static class Trie {


        static final int ALPHABET_SIZE = 26;

        // Tạo node của cây Trie
        static class TrieNode
        {
            TrieNode[] children = new TrieNode[ALPHABET_SIZE];

            // isEndOfWord hàm này trả về true nếu như từ ở node đang xét là từ cuối cùng
            // của chuỗi trie
            boolean isEndOfWord;

            TrieNode(){
                isEndOfWord = false;
                for (int i = 0; i < ALPHABET_SIZE; i++)
                    children[i] = null;
            }
        };

        static TrieNode root;

        // Nếu không có trong cây trie thì insert vào
        // Nếu có trong cây trie thì
        // đánh dâu là node lá
        static void insert(String key)
        {
            int level;
            int length = key.length();
            int index;

            TrieNode pCrawl = root;

            for (level = 0; level < length; level++)
            {
                index = key.charAt(level) - 'a';
                if (pCrawl.children[index] == null)
                    pCrawl.children[index] = new TrieNode();

                pCrawl = pCrawl.children[index];
            }

            // đánh dau node cuối như node lá
            pCrawl.isEndOfWord = true;
        }

        // return true nếu có trong trie flase nếu không
        static boolean search(String key)
        {
            int level;
            int length = key.length();
            int index;
            TrieNode pCrawl = root;

            for (level = 0; level < length; level++)
            {
                index = key.charAt(level) - 'a';

                if (pCrawl.children[index] == null)
                    return false;

                pCrawl = pCrawl.children[index];
            }

            return (pCrawl != null && pCrawl.isEndOfWord);
        }

        // Chay chương trình
        public static void main(String args[]) throws IOException {
            // Input  (chỉ dùng các ký từ viết thường từ 'a' tới 'z' và các cụm từ viết thường)

            String output[] = {" Not present in trie", " Present in trie"};


            root = new TrieNode();
            BufferedReader br = new BufferedReader(new FileReader("dataset.txt"));
            String line;
            while ((line = br.readLine()) != null)
            {
                insert(line);
            }





            String str;
            // Search for different keys
            Scanner in = new Scanner(System.in);
            System.out.print("Enter your word : ");
            str = in.nextLine();


            if(search(str) == true)
                System.out.println(str + output[1]);
            else System.out.println(str + output[0]);

        }
    }
}
