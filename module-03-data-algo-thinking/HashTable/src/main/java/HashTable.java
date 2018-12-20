public class HashTable {
    private int size;
    private int count;
    private int base_size;
    private HashItem items_table[];

    private HashItem HT_DELETED_ITEM = new HashItem (null, null);

    HashTable ()
    {
        base_size = Var_Constant.HT_INITIAL_BASE_SIZE;
        count = 0;
        size = PrimeFunc.next_prime(base_size);
        items_table = new HashItem[size];
    }

    HashTable(int prime_size)
    {
        items_table = new HashItem[prime_size];
    }



    public void Preresize(int new_size)
    {

        HashItem tmp_table[] = new HashItem[size];
        tmp_table = items_table;


        items_table = new HashItem[new_size];
        count = 0;

        int old_size = size;

        size = new_size;

        for(int i =0; i < old_size;i++)
        {
            if(tmp_table[i]!= null && tmp_table[i].GetKey ()!= null && tmp_table[i].GetValue ()!= null )
                this.Insert (tmp_table[i].GetKey (),tmp_table[i].GetValue ());
        }

    }

    public void resize_up()
    {
        final int double_size = PrimeFunc.next_prime (size*2);
        Preresize(double_size);
    }

    public void resize_down()
    {
        final int half_size = PrimeFunc.next_prime(size/2);
        Preresize(half_size);
    }


    void Insert(String key, String value)
    {

        int load = count * 100 / size;
        if (load > 70)
        {
            this.resize_up();
        }

        HashItem item = new HashItem(key, value);
        HashFuncTion hashfunc = new HashFuncTion (item.GetKey (),this.size);
        int index = hashfunc.DoubleHashing (0);

        HashItem cur_item = items_table[index];
        int i = 1;
        while (cur_item != null && cur_item != HT_DELETED_ITEM)
        {
            index = hashfunc.DoubleHashing (i);
            cur_item = this.items_table[index];
            i++;
        }


        this.items_table[index] = item;
        this.count++;
    }


    public String Search(String key)
    {

        HashFuncTion hashfunc = new HashFuncTion(key, this.size);
        int index = hashfunc.DoubleHashing (0);

        HashItem item_tmp = this.items_table[index];

        int i = 1;

        while ( item_tmp != null && item_tmp.GetKey() != null && item_tmp.GetValue() != null)
        {

            if(item_tmp.GetKey() == key)
            {
                return item_tmp.GetValue();
            }

            index = hashfunc.DoubleHashing (i);
            item_tmp = this.items_table[index];
            i++;
        }

        return null;
    }


    public void Delete(final String key) {
        final int load = count*100/size;
        if(load < 10 )
        {
            this.resize_down();
        }
        HashFuncTion hashfunc = new HashFuncTion(key, this.size);

        int index = hashfunc.DoubleHashing (0);

        HashItem item_tmp = items_table[index];

        int i=1;

        while(item_tmp !=null)
        {
            if(items_table[index].GetKey() != null && items_table[index].GetValue()!=null)
            {
                if(items_table[index].GetKey()== key)
                {
                    this.items_table[index] = HT_DELETED_ITEM;
                    return;
                }
            }
            index = hashfunc.DoubleHashing (i);
            i++;
        }
        this.count--;
    }

}