public class PrimeFunc
{
    private PrimeFunc()
    {}

    public static boolean is_prime(int x) {

        if (x < 2)
        {
            return false;
        }

        if (x < 4)
        {
            return true;
        }

        if ((x % 2) == 0)
        {
            return false;
        }
        for (int i = 3; i <= StrictMath.floor(Math.sqrt((double) x)); i += 2)
        {
            if ((x % i) == 0)
            {
                return false;
            }
        }
        return true;
    }



    public static int next_prime(int x)
    {
        while (is_prime(x)!= true )
        {
            x++;
        }

        return x;
    }
}