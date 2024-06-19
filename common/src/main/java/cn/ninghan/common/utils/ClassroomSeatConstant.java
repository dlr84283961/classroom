package cn.ninghan.common.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import javafx.util.Pair;

import java.util.List;

public class ClassroomSeatConstant {
    public static int seatTimePeriod;
    private static Table<Integer,Integer, Pair<Integer,Integer>> C2_SEAT_CONDITION = HashBasedTable.create();
    private static int C2_SEAT_NUM = 3;
    private static Table<Integer,Integer, Pair<Integer,Integer>> C7_SEAT_CONDITION = HashBasedTable.create();
    private static Table<Integer,Integer,Integer> C7_SPECIAL_CONDITION = HashBasedTable.create();
    static {
        int order = 1;
        for(int i=1;i<=4;i++){
            for(int j=1;j<=2;j++){
                C2_SEAT_CONDITION.put(i,j,new Pair<>(order,order+C2_SEAT_NUM-1));
                order+=C2_SEAT_NUM;
            }
        }

        order = 1;
        for(int i=1;i<=4;i++){
            for(int j=1;j<=3;j++){
                if(j%2==0){
                    C7_SEAT_CONDITION.put(i,j,new Pair<>(order,order+4));
                    order+=5;
                }else {
                    C7_SEAT_CONDITION.put(i,j,new Pair<>(order,order+2));
                    order+=3;
                }
            }
        }
    }

    public static Table<Integer,Integer,Pair<Integer,Integer>> getSeatTable(String seatType){
        if(seatType.equals("C2")){
            return C2_SEAT_CONDITION;
        }else if(seatType.equals("C7")){
            return C7_SEAT_CONDITION;
        }
        return HashBasedTable.create();
    }
}
