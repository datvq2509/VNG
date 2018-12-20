public class MainClass
{

        public static void main(String[] args) {
                HashTable hashTable = new HashTable ();

                hashTable.Insert ("abc123","voquocdat");
                hashTable.Insert ("abcdefghijk","ngohuynhngockhanh");
                hashTable.Insert ("abc","vobuikhacluan");
                hashTable.Insert ("123","vothanhlong");
                System.out.println("--------------------------");

                System.out.println("Search value by key \"abcde\": " + hashTable.Search ("abcde"));
                System.out.println("Search value by key \"abd\": " + hashTable.Search ("abd"));

                System.out.println("--------------------------");
                hashTable.Delete ("abcde");
                System.out.println("Search value by key \"abcde\": " + hashTable.Search ("abcde"));
                System.out.println("Search value by key \"abc123\": " + hashTable.Search("abc123"));

                System.exit(0);
        }
}
