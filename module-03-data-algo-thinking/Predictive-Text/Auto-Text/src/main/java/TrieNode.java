import java.util.HashMap;

public class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    char content;
    boolean isEnd;

    public TrieNode()
    {
    }
    public TrieNode(char c)
    {
        this.content = c;
        this.isEnd = false;
    }
}