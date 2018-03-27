package utils;

import engine.Serializable;

public class Line extends Serializable {
    
    public Vector3 u;
    public Vector3 v;
    
    public Line(Vector3 u, Vector3 v) {
        this.u = u;
        this.v = v;
    }
}
