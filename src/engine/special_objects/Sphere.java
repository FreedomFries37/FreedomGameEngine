package engine.special_objects;

import engine.special_components.Transform;
import utils.Vector3;

import java.util.LinkedList;

public class Sphere extends GameObject{
    
    public double radius;
    public String gamma;
    public Vector3 postition2;
    //public int[][][] array = {{{0,1,2},{3,4,5}},{{1,2,3},{3,2,2},{9,9,9}}};
    public int[][][][][] array = new int[2][2][2][2][2];
    
    private static int spheresCreated = 0;
    
    @Override
    public void initizalize() {
        //radius = 1;
        setName("Sphere" + spheresCreated);
        spheresCreated++;
        gamma = getName();
        //addComponent(Transform.class);
    }
    
    
}
