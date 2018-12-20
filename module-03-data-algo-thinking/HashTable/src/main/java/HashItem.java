public class HashItem
{
    private String key;
    private String value;


    HashItem(String k , String v)
    {
        key = k;
        value = v;
    }

    public String GetKey()
    {
        return key;
    }

    public String GetValue()
    {
        return value;
    }

    public void SetKey(String key)
    {
        this.key = key;
    }

    public void SetValue(String value)
    {
        this.value = value;
    }
}
