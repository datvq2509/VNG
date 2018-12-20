
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trie implements Dictionary
{
    private TrieNode root;

    public Trie()
    {
        root = new TrieNode();
    }

    public void insert(String word)
    {
        HashMap<Character, TrieNode> children = root.children;

        for(int i=0; i<word.length(); i++)
        {
            char c = word.charAt(i);

            TrieNode t;
            if(children.containsKey(c))
            {
                t = children.get(c);
            }
            else {
                t = new TrieNode(c);
                children.put(c, t);
            }

            children = t.children;

            //set leaf node
            if(i == word.length()-1)
                t.isEnd = true;
        }
    }

    public TrieNode searchNode(String str)
    {
        Map<Character, TrieNode> children = root.children;
        TrieNode t = null;
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if(children.containsKey(c)){
                t = children.get(c);
                children = t.children;
            }else{
                return null;
            }
        }

        return t;
    }








    public void Contains(String word)
    {
        TrieNode tmp_node = searchNode(word);
        if(tmp_node != null && tmp_node.isEnd)
            System.out.println ("In Trie ");
        else System.out.println ("Not In Trie");
    }


    public ArrayList<String> GetPredict(String word)
    {
        ArrayList<String> prefix=new ArrayList<String>();

        TrieNode t=searchNode(word);

        if(t==null) return null;

        FindPredict(t,word,prefix);

        return prefix;
    }

    public void FindPredict(TrieNode Node, String pre, ArrayList<String> alist)
    {
        if (alist.size() == 5)
        {
            return;
        }
        if (Node.isEnd)
        {
            alist.add(pre);
        }
        if (Node.children == null)
        {
            return;
        }

        HashMap<Character, TrieNode> child = Node.children;

        for (Map.Entry<Character, TrieNode> t : child.entrySet())
        {
            FindPredict(t.getValue(), pre + t.getKey(), alist);
        }
    }


}
