import static java.lang.Math.pow;

public class HashFuncTion
{
    private String str;
    private int num_buckets;

    HashItem hi;




    HashFuncTion(String s, int number)
    {
        this.str = s;
        this.num_buckets = number;
    }

    public int Hashing(String s, int prime, int size)
    {
        long hash = 0;
        int len_s = s.length();
        for (int i = 0; i < len_s; i++) {
            hash += (long)pow(prime, len_s - (i+1)) * s.charAt(i);
            hash = hash % size;
        }
        return (int)hash;
    }

    public int DoubleHashing(int attempt)
    {
        int first_hash = Hashing (str,Var_Constant.HT_PRIME_1,num_buckets);

        int second_hash = Hashing (str,Var_Constant.HT_PRIME_2,num_buckets);


        return (first_hash + (attempt * (second_hash + 1))) % num_buckets;
    }

}