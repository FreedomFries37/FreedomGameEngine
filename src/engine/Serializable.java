package engine;

import engine.special_objects.GameObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;

public class Serializable {
    
    
    
    boolean initialized = false;
    
    public Field[] getParams(){
        System.out.println("Current Class: " + this.getClass());
        
        LinkedList<Field> fields = new LinkedList<>(
                Arrays.asList(this.getClass().getFields()));
    
        
        for (int i = fields.size()-1; i >= 0; i--) {
            if(!getClass().isAssignableFrom(fields.get(i).getDeclaringClass()) &&
                    !fields.get(i).getDeclaringClass().isPrimitive()) fields.remove(i);
            
        }
        
        
        return fields.toArray(new Field[fields.size()]);
    }
    
    
    public String toStringExtended(int indent) throws IllegalAccessException{
        if(this.getClass() == GameObject.class) return ((GameObject)(this)).toStringExtended();
        
        StringBuilder output = new StringBuilder();
        
       
        output.append(stringOfFields(indent));
       
        
        return output.toString();
    }
    
    public String stringOfFields(int indent) throws IllegalAccessException{
        StringBuilder output = new StringBuilder();
        for(Field f : getParams()) {
            for (int i = 0; i < indent; i++) output.append("\t");
            String type = f.getType().getName();
            String varName = f.getName();
            String init = "[" + type + "]" + varName + "=";
            output.append(init);
        
            Class varClass = f.getType();
        
            if(Serializable.class.isAssignableFrom(varClass)){ //Serializable
                output.append("{\n");
                Serializable object = (Serializable) f.get(this);
                output.append(object.toStringExtended(indent+1));
                for (int i = 0; i < indent; i++) output.append("\t");
                output.append("}");
            }else if(varClass.isPrimitive()){ //primative
                output.append('(');
                output.append(f.get(this));
                output.append(')');
            }else if(varClass.equals(String.class)){
                output.append('"');
                output.append(f.get(this));
                output.append('"');
            }else{
                output.append("???");
            }
        
            output.append("\n");
        }
        
        return output.toString();
    }
    
    public void setInitialized(){
        initialized = true;
    }
    
    public boolean initialzed(){
        return initialized;
    }
    
    
}
