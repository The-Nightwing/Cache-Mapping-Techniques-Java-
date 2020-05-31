import java.util.*;
import java.Math;

public class AssociativeMapping
{
    public static int counter=0;
    public static int cache_hit=0;
    public static int cache_miss=0;
    public static boolean tag_found=false;
    public static boolean tag_found1=false;

    public static int convt(String a)
    {  
        int decimal=Integer.parseInt(a,2);  
        return decimal;
    }

    public static void AssociativeWrite(int[][] mm,String[] tag, int[][] data,int bit_size,int bit_offset,String address,int data2,int block_size,int cl)
    {
        String bits=address.substring(0,bit_size);
        int bit=convt(address.substring(0,bit_size)); //to check in tag array 
        int bit_off=convt(address.substring(bit_size,address.length())); //to identify block number
    
        for(int j=0;j<tag.length;j++)
        {
            if(tag[j].equals(bits))
            {
                cache_hit+=1;
                data[j][bit_off]=data2;
                mm[bit][bit_off]=data2;
                tag_found1=true;
                break;
            }
        }
        boolean null_tags=false;
        for(int i=0;i<tag.length;i++)
        {
            if(tag[i].equals("Null"))
            {
                null_tags=true;
                break;
            }
        }

        if(!tag_found1)
        {  
            cache_miss+=1;
            int mod1=counter%cl;

                data[mod1]=mm[bit];
                data[mod1][bit_off]=data2;
                mm[bit][bit_off]=data2;
                if(!null_tags)
                {
                    System.out.println("Replacing with tag: "+tag[mod1]);
                }
                tag[mod1]=bits;
                counter++;
            
        }     
    tag_found1=false;
}

    public static int AssociativeRead(int[][] mm,String[] tag, int[][] data,int bit_size,int bit_offset,String address,int cl)
    {
        String bits=address.substring(0,bit_size);
        int bit=convt(address.substring(0,bit_size)); //to check in tag array 
        int bit_off=convt(address.substring(bit_size,address.length())); //to identify block number
        for(int i=0;i<tag.length;i++)
        {
            if(tag[i].equals(bits))
            {
                cache_hit+=1;
                tag_found=true;
                tag[i]=bits;
                return data[i][bit_off];
            }                                       //...new
                                                        //....new
        }                                              //......new
        boolean null_tags=false;                        //.....new
        for(int i=0;i<tag.length;i++)
        {
            if(tag[i].equals("Null"))
            {
                null_tags=true;
                break;
            }
        }
        int mod=counter%cl; //fifo 
        if(!tag_found)
        {
            cache_miss+=1;
            data[mod]=mm[bit];
            if(!null_tags)
            {
                System.out.println("Replacing with tag: "+tag[mod]);
            }
            tag[mod]=bits;
            counter++;
        }

        tag_found=false;
        return data[mod][bit_off];
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

    public static void associative(int cache_Size,int cl,int block_size,int mm_size,int blocks)
    {
        int bit_size=(int)(Math.log(blocks)/Math.log(2));
        int bit_offset=(int)(Math.log(block_size)/Math.log(2));
        int mmrows=mm_size/block_size;
        //System.out.println((blocks/block_size));

        int[][] mm =new int[mmrows][block_size]; //main_memory
        String[] tag=new String[cl]; //tag
        int[][] data=new int[cl][cache_Size/cl]; //data array

        for(int k=0;k<cl;k++)
        {
            tag[k]="Null"; //initializing tag array with all null;
        }

        Scanner in=new Scanner(System.in);
    
        while(true)
        {
        System.out.print("Operation:");
        String operation=in.nextLine();
        System.out.println();

        switch(operation)
        {
            case "Read": 
            {
                System.out.print("Address:");
                String address=in.nextLine();
                System.out.println();
                int show=AssociativeRead(mm,tag,data,bit_size,bit_offset,address,cl);
                System.out.print("Data on this address: "+show);
                System.out.println();
                System.out.println("Tags:");
                System.out.println();
                printTag(tag);
                System.out.println();
                print(data,cl,(cache_Size/cl));
                System.out.println();
                System.out.println("Cache Hit: "+cache_hit);
                System.out.println("Cache Miss: "+cache_miss);
                System.out.println();
               
                break;
            }
            case "Write":
            {
                System.out.print("Address:");
                String address=in.nextLine();
                System.out.println();
                System.out.print("Data to Write:");
                int dataE=in.nextInt();
                System.out.println();
                AssociativeWrite(mm,tag,data,bit_size,bit_offset,address,dataE,block_size,cl);
                System.out.println("Tags:");
                printTag(tag);
                System.out.println();
                print(data,cl,(cache_Size/cl));
                System.out.println();
                System.out.println("Cache Hit: "+cache_hit);
                System.out.println("Cache Miss: "+cache_miss);
                System.out.println();
                break;
            }
        }
    }
}
public static void printTag(String[] a)
{
    for(int i=0;i<a.length;i++)
    {
        System.out.println(a[i]);
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


        int blocks=mm_size/block_size; //no. of blocks

        associative(cache_Size,cl,block_size,mm_size,blocks);

        
    }
    
}