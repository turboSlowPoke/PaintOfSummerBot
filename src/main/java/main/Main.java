package main;

public class Main {
    public static void main(String[] a){
        int floorsInHouse=18;
        int buildNumbersOnFloor=18;
        int flat = 129;
        int floor;
        int builnumber;

        int tempfloor = flat/floorsInHouse;
        if (flat%floorsInHouse==0)
            floor=tempfloor+1;
        else
            floor=tempfloor+2;
        int firstFlat = tempfloor*floorsInHouse;
        if (firstFlat==flat)
            builnumber=buildNumbersOnFloor;
        else
            builnumber = flat-firstFlat;
    }
}
