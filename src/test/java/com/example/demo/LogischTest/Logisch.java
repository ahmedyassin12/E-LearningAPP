package com.example.demo.LogischTest;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Logisch {

    List<List<Integer>> Lists=new ArrayList<>();

    @BeforeEach
    void setUp() {


        List<Integer> L1=List.of(1,1,2,3,5) ;


        List<Integer> L2=List.of(1,5);



    }
    private int  sum(int n ,boolean b){



        if(n==0) {

            System.out.println("bool = "+b);
            return 0 ;
        }


        b=true;
        return n+sum(n-1,b) ;



    }
    private int  sumOfCubs(int n,int m  ){


/*
33=23 +32
                 f(3,3)
      f(3,2)=3                  f(2,3)
f(3,1)=1   +   f(2,2)=2
        f(1,2)=1 +  f(2,1)=1
13=1
22=12+21=2
1+1

 */
        if(n<=1||m<=1) return 1 ;

        return sumOfCubs(n-1,m)+sumOfCubs(n,m-1)  ;



    }

    private int FrogKjump(int indx,int []arr,int k){

        if(indx==0)return 0;
        // int []arr={30,10,60,10,60,50} ;
        /*

         */

        int min =FrogKjump(indx-1,arr,k)+Math.abs(arr[indx]-arr[indx-1]);



        for(int j=2;j<=k;j++){

            if(indx-j>=0){
                int l= FrogKjump(indx-j,arr,k)+Math.abs(arr[indx]-arr[indx-j]) ;
                min=Math.min(min,l) ;
            }



        }



        return min ;


    }
    private int FrogJump(int indx,int []arr){


        if(indx==0)return 0;

        int left =FrogJump(indx-1,arr)+Math.abs(arr[indx]-arr[indx-1]);
        int right=9999;
        if(indx>1)
            right=FrogJump(indx-2,arr)+Math.abs(arr[indx]-arr[indx-2]) ;

       return Math.min(left,right) ;


    }

    //using left right one ;
    private int maximumSubNonAdjacent1(int indx,int []arr){


        if(indx==0||indx==1)return arr[indx];


            int left =maximumSubNonAdjacent1(indx-2,arr)+arr[indx];
            int right=-9999;
            if(indx>3)
                right=maximumSubNonAdjacent1(indx-3,arr)+arr[indx] ;



        return Math.max(left,right) ;


    }

    private void takeNotTake(int indx,List<Integer> list,int []fullarr,int n){

        if(indx==n) {
            //print subArray :
            System.out.println(list.toString());
            fullarr[fullarr.length-1]+=1;
return;
        }

    //pick or not pick
        list.add(fullarr[indx]); ;

        takeNotTake(indx + 1, list, fullarr,n);

        list.remove(list.size()-1);

        takeNotTake(indx+1,list,fullarr,n);


    }
    private int maximumSubNonAdjacent2(int indx, int[] arr, int[] dp,int lastindx) {

        if(indx==lastindx)return arr[indx];
        if(indx<lastindx)return 0 ;
        if(dp[indx]!=-1)return dp[indx];

        int pick = arr[indx]+maximumSubNonAdjacent2(indx-2,arr, dp,lastindx);

        int notPick=0+maximumSubNonAdjacent2(indx-1,arr, dp,lastindx) ;



        return dp[indx]=Math.max(pick,notPick);


    }

    private int minimumGridUniquePaths(int m,int n ){

        if(m==0&&n==0) return 1 ;


        int left=0,up=0;

        if(m!=0)
             up=0+minimumGridUniquePaths(m-1,n);


        if(n!=0)
             left=0+minimumGridUniquePaths(m,n-1);


        return up+left;

        /*

                                         f(2,1)   =3


                 f(1,1)=2                                               f(2,0)=1
  f(0,1)=1                  f(1,0)=1                                f(1,0)=1

        f(0,0)=1      f(0,0)                                       f(0,0)=1

         */

    }
    private int NinjaMaximum(int day,int[][]arr,int [][]dp,int task){

        if(day==0){

            int max =-999;

            for(int i=0;i<3;i++){


                if(i!=task){
                    if(max<arr[day][i])max=arr[day][i];
                }


            }


            return max;
        }
if(task!=3&&dp[day][task]!=-1)return dp[day][task];



    int maxi =0;
            for(int i=0;i<3;i++){

                if(i!=task){

                    int point=arr[day][i]+ NinjaMaximum(day-1,arr,dp,i);

                    maxi=Math.max(maxi,point);

                }


            }

        return   dp[day][task]=maxi;



    }




            public int minimumTriangle(int [][]triangl,int indx,int task,int[][]dp){

                if(indx==triangl.length-1){

                    return triangl[indx][task];

                }
                if(dp[indx][task]!=-1)return  dp[indx][task];


                      int  down = triangl[indx][task]+minimumTriangle(triangl,indx+1,task,dp) ;




                       int downright=triangl[indx][task]+minimumTriangle(triangl,indx+1,task+1,dp);


        return dp[indx][task]=Math.min(downright,down);



            }



           private int  subarraySum(int []arr,int k ){


        int sum = 0 ;

               int returnsum=0;

        for (int i=0;i<arr.length;i++){


            sum = arr[i];
            if(sum==k)returnsum++;
            for(int j=i+1;j<arr.length;j++){


                if(sum+arr[j]==k){

                    returnsum++;
                }


                else {

                    if(sum+arr[j]>k) {
                        if(arr[i]+arr[j]==k)            returnsum++;

                    }
                    else {

                        sum+=arr[j];


                    }


                }

/*

 */
            }


        }

        return returnsum;
           }
           

private boolean isThereSubset(int []arr,int target,int indx,int [][]dp){


       if(target==0)return true;
       if(indx==0) return (target==arr[0]) ;
        if(dp[indx][target-1]!=-1){
            if(dp[indx][target]==0)return  true;
            return false;
        }

    boolean nottake=false;

    nottake=isThereSubset(arr,target,indx-1,dp);

    boolean take =false;
    if(target>arr[indx])
       take=isThereSubset(arr,target-arr[indx],indx-1,dp);



     dp[indx][target-1] = take||nottake ?1:0;

     return take || nottake;

     /*

     -1 1    0

    0 2   1  if +1
     wrong

     -2 2 1 if +-
     1-2=-1,-1-2 =-3

      */


}


    private boolean canPartition(int indx,int[] nums,int target) {


        if(indx==0) return (target==nums[0]) ;
        if(nums[indx]==target)return  true;


       boolean nottake=canPartition(indx-1,nums,target);

        boolean take =false;
        if(nums[indx]<target)take=canPartition(indx-1,nums,target-nums[indx]);


        return take||nottake ;



    }

    @Test
    void Recursion(){
        List<Integer>list=new ArrayList<>();
        HashMap<Integer,Integer> m = new HashMap<>();

        m.put(1,100);

        System.out.println(m.get(2));

         /*
        int []arr= {1,2,1,2,1} ;
        int target =4;
        int [][]dp = new int[arr.length][target] ;

        for(int i=0;i<arr.length;i++){

            for(int j=0;j<target;j++){

                dp[i][j]=-1;
            }

        }
        System.out.println(" is there sub ? = "+isThereSubset(arr,target,arr.length-1,dp));




        int [][]arr= {
                {2},
                {3,4},
                {6,5,7},
                {4,1,8,3}

        };


        int n = arr.length;



            int [][]dp = new int[arr.length][arr[arr.length-1].length] ;
            for(int i=0;i<4;i++){

                for(int j=0;j<3;j++){

                    dp[i][j]=-1;
                }

            }

        System.out.println("min Triangle  = "+minimumTriangle(arr,0,0,dp));


        int []dp=new int [arr.length];
        for(int i=0;i<arr.length;i++){
            dp[i]=-1;
        }
        System.out.println("max1 = "+maximumSubNonAdjacent2(arr.length-2,arr,dp,0)+"\nmax2 = "+
                maximumSubNonAdjacent2(arr.length-1,arr,dp,1) );

        int k=0;
        int []dp=new int[arr.length];
        int prev1=0,prev2,curr=arr[0];
        int pick,notPick ;
        for(int i=1;i<arr.length;i++){

            prev2=prev1;
            prev1=curr;

                if (i - 2 == -1) pick = arr[i] + 0;


                else{

                    pick = arr[i] + prev2;
                }

                notPick = 0 + prev1;

            if(i==2&&pick>notPick){

                k=arr[0];
            }
            if(i==arr.length-1) pick-=k ;

                curr =Math.max(pick,notPick);


        }

        System.out.println("max = "+curr);




  int []arr2={3,1,2,0} ;
        List<Integer>list=new ArrayList<>(20);

      takeNotTake(0,list,arr2,arr2.length-1);
        System.out.println("number of subarrays is :"+arr2[3]);

tabulation of frog k steps
        int []dp =new int[arr.length];
        int k =5;
        for(int i=0;i<arr.length;i++){

            if(i==0)dp[i]=0 ;

            else{

                dp[i]=9999;
for(int j=1;j<=k;j++){
    if(i-j>=0){
        int l= dp[i-j]+Math.abs(arr[i]-arr[i-j]) ;
        dp[i]=Math.min(dp[i],l) ;
    }
    else break ;
}

            }

        }
        System.out.println(dp[dp.length-1]);

 */
/*
Tabulation of Frog 1 or 2 steps
int []dp=new int [arr.length];
for(int i=0;i<arr.length;i++){

    if(i==0)dp[0]=0 ;
else {
        int left = dp[i - 1] + Math.abs(arr[i - 1] - arr[i]);
        int right = 99999;
        if (i > 1)
            right = dp[i - 2] + Math.abs(arr[i - 2] - arr[i]);

        dp[i] = Math.min(left, right);
    }
}
        System.out.println("min energy for Frog  = "+dp[dp.length-1]);

*/

    }



    @Test
    void MaxListOfIntegers (  ) {

        int MaxList = 0;

        MaxList = Lists.stream().mapToInt(list->list.stream().mapToInt(Integer::intValue)
                .sum())
                .sum()
                ;

        System.out.println(MaxList);

    }

    @Test
    void MaxListOfString (  ) {

        int MaxNameLength = 0 ;
        List<String> namen = List.of("Anna", "Ben", "Christoph");
         MaxNameLength = namen.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);



          String a = namen.get(0);

          a.indexOf('a');
          a+=1;
        System.out.println("b="+ (int)'b' ) ;

        System.out.println("last endew ===" ) ;
        char lastOP[] ={' '};
if(a.charAt(0)=='A') System.out.println("a="+a.charAt(0));

        System.out.println(MaxNameLength);
String []strs ={"Anna","Ben"};
        System.out.println(strs.length);
        strs[0].length();

    }
int []s=new int[10];

}
