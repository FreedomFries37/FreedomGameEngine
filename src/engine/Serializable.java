package engine;

import engine.special_objects.GameObject;
import tools.AssetTreeConverter;

import javax.jws.Oneway;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Serializable {
    
    public Serializable(){}
    
    boolean initialized = false;
    
    public Field[] getParams() {
        System.out.println("Current Class: " + this.getClass());
    
        LinkedList<Field> fields = new LinkedList<>(
                Arrays.asList(this.getClass().getFields()));
    
        
        for (int i = fields.size() - 1; i >= 0; i--) {
            if (!Serializable.class.isAssignableFrom(fields.get(i).getDeclaringClass())) {
                boolean isPrimitive = fields.get(i).getDeclaringClass().isPrimitive();
                if (!isPrimitive) fields.remove(i);
            } else if (fields.get(i).getName().equals("gameObject")) {
                fields.remove(i);
            }
        }
    
        
    
       
        
        
        return fields.toArray(new Field[fields.size()]);
    }
    
    
    public String toStringExtended(int indent) throws IllegalAccessException{
        if(this.getClass() == GameObject.class) return ((GameObject)(this)).toStringExtended();
        
        StringBuilder output = new StringBuilder();
        
       
        output.append(stringOfFields(indent));
       
        
        return output.toString();
    }
    
    public static String convertPrimitiveToClass(Class<?> varClass){
        String initial = "java.lang.";
        String modifiedType = "";
        switch(varClass.getName()){
            case "int":
                modifiedType = "Integer";
                break;
            case "double":
                modifiedType = "Double";
                break;
            case "char":
                modifiedType = "Character";
                break;
            case "long":
                modifiedType = "Long";
                break;
            case "float":
                modifiedType = "Float";
                break;
            case "boolean":
                modifiedType = "Boolean";
                break;
        }
        return initial + modifiedType;
    }
    
    public <T> String stringOfFields(int indent) throws IllegalAccessException{
        StringBuilder output = new StringBuilder();
        for(Field f : getParams()) {
            for (int i = 0; i < indent; i++) output.append("\t");
    
            Class varClass = f.getType();
            String type;
            
            
            if(varClass.isPrimitive()){
                type = convertPrimitiveToClass(varClass);
            }else if(varClass.isArray()){
                
                Class temp = f.getType();
                StringBuilder boxes = new StringBuilder();
                while(temp.isArray()){
                    boxes.append("[]");
                    temp = temp.getComponentType();
                }
                if(temp.isPrimitive()){
                    type = convertPrimitiveToClass(temp);
                }else{
                    type = temp.getName();
                }
                
                type += boxes.toString();
            }else{
                type = varClass.getName();
            }
            
            
            
            
            
            String varName = f.getName();
            String init = "[" + type + "]" + varName + "=";
            output.append(init);
        
            
        
            if(Serializable.class.isAssignableFrom(varClass)){ //Serializable
                output.append("{\n");
                Serializable object = (Serializable) f.get(this);
                if(object == null) object = AssetTreeConverter.spoofConstructor(varClass);
                if(object == null) output.append("???");
                else output.append(object.toStringExtended(indent+1));
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
            }else if(varClass.isArray()) {
                output.append("[\n");
                output.append(convertArrayToString(varClass.getComponentType(), (T[]) f.get(this),indent+1));
                for (int i = 0; i < indent; i++) output.append("\t");
                output.append("]");
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
    
    @SuppressWarnings("unchecked")
    private <T,K> String convertArrayToString(Class<?> componentType, T[] array, int indent){
        if(array == null) return "";
        StringBuilder output = new StringBuilder();
        int count = 0;
        
        if(componentType.isArray()){
            
            for(int i = 0; i < array.length; i++){
                
                componentType.cast(array[i]);
            
                
            }
            
            
            
            
            for(T part : array){
                for (int i = 0; i < indent; i++) output.append("\t");
                String type;
                Class temp = componentType;
                StringBuilder boxes = new StringBuilder();
                while(temp.isArray()){
                    boxes.append("[]");
                    temp = temp.getComponentType();
                }
                if(temp.isPrimitive()){
                    type = convertPrimitiveToClass(temp);
                }else{
                    type = temp.getName();
                }
    
                type += boxes.toString();
    
                String init = "[" + type + "]" + count + "=";
                output.append(init);
                output.append(convertArrayToString(componentType.getComponentType(), (K[]) part, indent+1));
            }
        }else{
            
            //Object[] objectAsArray = ((Object[]) array).length;
            List<Object> list = Arrays.asList((T[]) array);
            System.out.println(Array.getLength(list));
            for(Object part : list){
                System.out.println(part.getClass().getName());
            }
            
            
        }
    
    
        return output.toString();
    }
    
    
}
