package com.example.demo.LogischTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class Solution {

    ListNode list1;
    ListNode list2;
    @BeforeEach
    void setUp() {

 list1=new ListNode(1,new ListNode(2,new ListNode(4,new ListNode(6,null))));

 list2 =new ListNode(1,new ListNode(3,new ListNode(4,new ListNode(6,null))));



    }
    @Test
    public void  mergeTwoLists() {

        ListNode list = new ListNode() ;

        ListNode curr=list  ;

        while(list1!=null && list2 !=null){


            if(list1.val<list2.val) {



                curr.next=list1 ;
                list1=list1.next;

            }


            else{


                curr.next=list2 ;
                list2=list2.next;

            }
            curr=curr.next;


        }

        if(list1!=null){

            curr.next=list1 ;


        }
        else{
            curr.next=list2 ;


        }

       while (list!=null){

           System.out.println("\n valeur= "+list.val);
           list=list.next;
       }

    }

}
