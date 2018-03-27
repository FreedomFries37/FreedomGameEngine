package engine;

import engine.special_components.Transform;
import engine.special_objects.GameObject;

public abstract class StandardBehavior extends Serializable{
    
    public GameObject gameObject;
    
    
    public void setGameObject(){
        if(this instanceof GameObject) {
            gameObject = (GameObject) this;
            gameObject.addComponent(Transform.class);
        }else if(this instanceof Component){
            if(((Component) this).getParent() != null)
                gameObject = ((Component) this).getParent();
        }
    }
    
    public void update(){}
    
    public void start(){}
    
    public void initizalize(){}
}
