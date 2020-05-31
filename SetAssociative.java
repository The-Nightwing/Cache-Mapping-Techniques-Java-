import java.util.*;
import java.lang.Math;
import java.util.Random;

public class SetAssociative
{
    public static boolean nset_tag=false;
    public static boolean nset_tag1=false;
    public static int cache_hit=0;
    public static int cache_miss=0;

    public static int convt(String a)
    {
        int decimal=Integer.parseInt(a,2);
        return decimal;
    }

    public static void nsetWrite(int[][] data,int[][] mm,String[] tag,int num_sets,String address,int block_offset_size,int block_num_size,int set_num_size,int tag_bit_size,int input,int k)
    {
        Random random = new Random();

        int block_no=convt(address.substring(0,block_num_size));
        int block_offset=convt(address.substring(block_num_size,address.length()));

        int set_no=convt(address.substring(tag_bit_size,tag_bit_size+set_num_size));
        String tag_c=address.substring(0,tag_bit_size);

        for(int i=set_no*k;i<set_no*k+k;i++)
        {
            if(tag[i].equals(tag_c))
            {
                System.out.println("hit");
                cache_hit+=1;
                data[i][block_offset]=input;
                mm[block_no][block_offset]=input;
                //tag[i]=tag_c;
                nset_tag1=true;
                break;
            }
        }

        boolean null_tag=false;
        if(!nset_tag1)
        {
            System.out.println("miss");
            cache_miss+=1;
            for(int i=set_no*k;i<set_no*k+k;i++)
            {
                if(tag[i].equals("Null"))
                {
                    data[i]=mm[block_no];
                    data[i][block_offset]=input;
                    mm[block_no][block_offset]=input;
                    tag[i]=tag_c;
                    null_tag=true;
                    break;
                }
            }
            if(!null_tag)
            {
                //System.out.println("Hheee");
                int max=set_no*k+k;
                int min=set_no*k;
                int index=(int)(min+random.nextInt(max-min)); //random number
                data[index]=mm[block_no];
                data[index][block_offset]=input;
                mm[block_no][block_offset]=input;
                System.out.println("The tag will be replaced with: "+tag[index]);
                tag[index]=tag_c;
            }
        }
        nset_tag1=false;
    }

    public static int nsetRead(int[][] data,int[][] mm,String[] tag,int num_sets,String address,int block_offset_size,int block_num_size,int set_num_size,int tag_bit_size,int k)
    {
        int block_no=convt(address.substring(0,block_num_size));
        int block_offset=convt(address.substring(block_num_size,address.length()));
        Random random=new Random();
        int set_no=convt(address.substring(tag_bit_size,tag_bit_size+set_num_size));
        String tag_c=address.substring(0,tag_bit_size);
        int ret=0;
        for(int i=set_no*k;i<set_no*k+k;i++)
        {
            if(tag[i].equals(tag_c))
            {
                cache_hit+=1;
                //System.out.println("Address found in the Stack");
                tag[i]=tag_c;
                nset_tag=true;
                ret=data[i][block_offset];
            }
        }
        boolean null_tag=false;
        if(!nset_tag)
        {
            cache_miss+=1;
            for(int i=set_no*k;i<set_no*k+k;i++)
            {
                if(tag[i].equals("Null"))
                    {
                        data[i]=mm[block_no];
                        tag[i]=tag_c;
                        null_tag=true;
                        ret=data[i][block_offset];
                        break;
                    }
            }
            if(!null_tag)
            {
                int max=set_no*k+k;
                int min=set_no*k;
                int index=(int)(min+random.nextInt(max-min));
                data[index]=mm[block_no];
                System.out.println("The tag be replaced with: "+tag[index]);
                tag[index]=tag_c;
                ret=data[index][block_offset];                
            }
        }
        
        nset_tag=false;
		return ret;
    }
    public static void print(int[][] data,int l,int r)
    {
        for(int i=0;i<l;i++)
                {
                    for(int j=0;j<r;j++)
                    {
                        System.out.print(data[i][j]+" ");
                    }
                    System.out.println();
                
                }
            }
    public static void printtag(String[] a)
    {

        for(int i=0;i<a.length;i++)
        {
            System.out.println(a[i]);
        }

    }

    public static void nset(int cache_Size,int lines,int block_size,int main_memory_size,int no_blocks,int set,int k)
    {

        int[][] data=new int[lines][cache_Size/lines];
        int[][] mm=new int[no_blocks][block_size];
        String[] tag=new String[lines];
        int total_no_bits=(int)(Math.log(main_memory_size)/Math.log(2));

        //// for main memory mapping
        int block_offset_size=(int)(Math.log(block_size)/Math.log(2));
        int block_num_size=(int)(Math.log(no_blocks)/Math.log(2));
        //////////

        //////for cache memory mapping
        int set_num_size=(int)(Math.log(set)/Math.log(2));
        int tag_bit_size= total_no_bits-(set_num_size+block_offset_size);
        //////5,2   4,1,2

        //int num_sets=lines/k; //num of sets

        for(int i=0;i<lines;i++)
        {
            tag[i]="Null";
        }
        int num_sets=0;
        Scanner in=new Scanner(System.in);

        while(true)
        {
            System.out.print("Operation: ");
            String operation=in.nextLine();
            System.out.println();

            switch(operation)
            {
                case "Read":
                {
                    System.out.print("Address: ");
                    String address=in.nextLine();
                    System.out.println();
                    int show=nsetRead(data,mm,tag,num_sets,address,block_offset_size,block_num_size,set_num_size,tag_bit_size,k);
                    System.out.print("Data at this address: "+show);
                    System.out.println();
                    print(data,lines,cache_Size/lines);
                    System.out.println();
                    System.out.println("Cache Hit: "+cache_hit);
                    System.out.println("Cache Miss: "+cache_miss);
                    System.out.println();
                    printtag(tag);
                    System.out.println();
                    break;
                }
                case "Write":
                {
                    System.out.print("Address: ");
                    String address=in.nextLine();
                    System.out.println();
                    System.out.print("Data to Write: ");
                    int input=in.nextInt();
                    System.out.println();
                    nsetWrite(data,mm,tag,num_sets,address,block_offset_size,block_num_size,set_num_size,tag_bit_size,input,k);
                    System.out.println();
                    print(data,lines,cache_Size/lines);
                    System.out.println();
                    System.out.println("Cache Hit: "+cache_hit);
                    System.out.println("Cache Miss: "+cache_miss);
                    System.out.println();
                    printtag(tag);
                    System.out.println();
                    break;
                }
            }
        }

        
    }
    public static void main(String[] args) 
    {
        Scanner in=new Scanner(System.in);
        System.out.print("Enter cache size: ");
        int cache_Size=in.nextInt();
        System.out.println();
        System.out.print("No. of Lines: ");
        int lines=in.nextInt();
        System.out.println();
        System.out.print("Enter Block_Size: ");
        int block_size=in.nextInt();
        System.out.println();
        System.out.print("Enter mm size: ");
        System.out.println();
        int main_memory_size=in.nextInt();

        int no_blocks=main_memory_size/block_size;

        System.out.print("Enter K:");
        int k=in.nextInt();
        System.out.println();

        nset(cache_Size,lines,block_size,main_memory_size,no_blocks,lines/k,k);
    }
}