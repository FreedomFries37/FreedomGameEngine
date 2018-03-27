package utils;

import engine.Serializable;

public class Plane extends Serializable {
    
    public Line line;
    public Vector3 point;
    
    public Plane(Line line, Vector3 point) {
        this.line = line;
        this.point = point;
    }
    
    public Plane(Vector3 p1, Vector3 p2, Vector3 p3){
        line = new Line(p1,p2);
        point = p3;
    }
}
