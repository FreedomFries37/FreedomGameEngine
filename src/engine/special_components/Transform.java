package engine.special_components;

import engine.Component;
import utils.Vector3;

public class Transform extends Component {
    public Vector3 position;
    
    @Override
    public void initialize() {
        position = new Vector3(0,0,0);
    }
}
