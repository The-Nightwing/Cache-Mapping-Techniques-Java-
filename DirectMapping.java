import java.util.*;
import java.lang.Math;

public class DirectMapping

{
    public static int cache_hit=0;
    public static int cache_miss=0;
    public static int counter=0;

    public static int convt(String a)
    {  
        int decimal=Integer.parseInt(a,2);  
        return decimal;
    }

    public static int count=0;

    public static int counter1=0;
    ////
    
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
    
    
/////

static boolean direct_tag=false;
static boolean direct_tag1=false;

public static void DirectWrite(int[][] mm,int[][] data,String[] tag,int cl,int data_rows,int bits,int block_size,int block_no,int block_offset,int input,String address,int tag_size)
{
    int blockno=convt(address.substring(0,block_no)); //for mm
    int blockoff=convt(address.substring(block_no,address.length())); //for mm

    //for putting in cache

    int lineno=(int)(Math.log(cl)/Math.log(2));
    
    int line_no=convt(address.substring(tag_size,tag_size+lineno));
    

    String tag_c=address.substring(0,tag_size);
        if(tag[line_no].equals(tag_c))
        {
            data[line_no][blockoff]=input;
            mm[blockno][blockoff]=input;
            direct_tag1=true;
            cache_hit+=1;
        }

    if(!direct_tag1)
   {
        cache_miss+=1;
        data[line_no]=mm[blockno];
        data[line_no][blockoff]=input;
        mm[blockno][blockoff]=input;
        if(tag[line_no].equals("Null"))
           {tag[line_no]=tag_c;}
        else
           {
               System.out.println("The tag bit will get replaced with: "+tag[line_no]);
               tag[line_no]=tag_c;
           }
    }

    direct_tag1=false;
}


public static int DirectRead(int[][] mm,int[][] data,String[] tag,int cl,int data_rows,int bits,int block_size,int block_no,int block_offset,String address,int tag_size)
{

    int blockno=convt(address.substring(0,block_no)); //extracted block number of address.(mm)
    int blockoff=convt(address.substring(block_no,address.length())); //extracted block offset from address.(mm)

    //for putting in cache
    int lineno=(int)(Math.log(cl)/Math.log(2)); //extracted line no

    int line_no=convt(address.substring(tag_size,tag_size+lineno)); //tag_bits
    String tag_c=address.substring(0,tag_size); //extracted tagbits from given address
    //int Tag=convt(address.substring(0,tag_size));

      if(tag[line_no].equals(tag_c)) //check for hits/miss
        {
            cache_hit+=1;                                 
            direct_tag=true; //true means hits.

            return data[line_no][blockoff];
        }

    if(!direct_tag)
    {
        cache_miss+=1;
        data[line_no]= mm[blockno]; //loaded block from main memory
        if(tag[line_no].equals("Null")) //replacement--> yes or no decision.
            {tag[line_no]=tag_c;}
        else
        {
            System.out.println("The tag bit will get replaced with: "+tag[line_no]); //replacement
            tag[line_no]=tag_c;
        }
    }
                                                

    direct_tag=false;

    return data[line_no][blockoff];
}
public static void printTag(String[] a)
{
    for(int i=0;i<a.length;i++)
        {System.out.print(a[i]+"  ");}
}
    public static void Directt(int cache_Size,int cl,int block_size,int mm_size,int blocks)
    {
        int data_rows=cache_Size/cl; //no.of lines in cache.
        int mmrows=mm_size/block_size; //no. of lines in main memory.
        int blockno=(int)(Math.log(blocks)/Math.log(2)); //no. of bits to represent block number.
        int total_bits=(int)(Math.log(mm_size)/Math.log(2)); //totals bits that one can enter.
        int blockoffset=(int)(Math.log(block_size)/Math.log(2)); //no. of bits to represent block offset.
        int lineno=(int)(Math.log(cl)/Math.log(2)); //no. of bits to represent line number.
//1001001
        int block_no=total_bits-blockoffset; //for cache mapping
        int tag_size=total_bits-(lineno+blockoffset); //no. of bits to represent tag bits.
        int[][] data=new int[cl][cache_Size/cl]; //cache array
        int[][] mm=new int[mmrows][block_size]; //main memory
        String[] tag=new String[cl]; //tag array

        for(int t=0;t<tag.length;t++)
        {
            tag[t]="Null";
        }

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
                    int show=DirectRead(mm,data,tag,cl,data_rows,total_bits,block_size,blockno,blockoffset,address,tag_size);
                    System.out.print("Data: "+show);
                    System.out.println();
                    print(data,cl,(cache_Size/cl));
                    System.out.println();
                    System.out.println("Cache Hit: "+cache_hit);
                    System.out.println("Cache Miss: "+cache_miss);
                    System.out.println();
                    System.out.print("Tags: ");
                    printTag(tag);
                    System.out.println();
                    break;
                   }
                
                case "Write":
                {
                    System.out.print("Address: ");
                    String address=in.nextLine();
                    System.out.println();
                    System.out.print("Data to Write: ");
                    int dataD=in.nextInt(); 
                    System.out.println();
                    DirectWrite(mm,data,tag,cl,data_rows,total_bits,block_size,block_no,blockoffset,dataD,address,tag_size);
                    print(data,cl,data_rows);
                    System.out.println();
                    System.out.println("Cache Hit: "+cache_hit);
                    System.out.println("Cache Miss: "+cache_miss);
                    System.out.println();
                    System.out.print("Tags: ");
                    printTag(tag);
                    System.out.println();
                    break;
                }
            }
        }
}
    public static void main(String[] args) 
    {
        Scanner in=new Scanner(System.in);
        System.out.print("Enter cache size:");
        int cache_Size=in.nextInt();
        System.out.println();
        System.out.print("Enter no. of lines:");
        int cl=in.nextInt();
        System.out.println();
        System.out.print("Enter Block Size:");
        int block_size=in.nextInt();
        System.out.println();
        //block size is always equal to Cl;
        System.out.print("Enter memory size:");
        int mm_size=in.nextInt();
        System.out.println();

        int blocks=mm_size/block_size; //no. of blocks//128/4
        Directt(cache_Size,cl,block_size,mm_size,blocks);
        
    }
}