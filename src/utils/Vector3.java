package utils;

import java.math.*;

public class Vector3 {

    public double x, y, z;
    
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(double x, double y){
        this.x = x;
        this.y = y;
        z = 0;
    }
    
    public static double distanceBetween(Vector3 a, Vector3 b){
        return Math.sqrt(Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) + Math.pow(a.y-b.y, 2));
    }
}
