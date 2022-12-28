package com.example.demo;

public interface Tool {
    double masshtab = 4;
    int placeHolder = 100;
    double frameHeight = 150 * masshtab;

    //public static double location = frameHeight + placeHolder;

    static double getLocationY(double y) {
        return placeHolder + frameHeight - masshtab * y;
    }

    static double getLocationX(double x) {
        return placeHolder + masshtab * x;
    }

    //receive
    double E_ELEC = 5 * Math.pow(10, -5);

    //send to station
    double E_AMP = 13 * Math.pow(10, -16);

    //send to head
    double E_AMP_INTRA = E_AMP / 10;

    double PACKET_SIZE = 4000.0;
}
