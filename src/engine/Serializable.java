package engine;

import engine.special_objects.GameObject;
import tools.AssetTreeConverter;

import javax.jws.Oneway;
import javax.lang.model.type.ArrayType;
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
                boolean firstRun = true;
                while(temp.isArray()){
                    if(firstRun) {
                        firstRun = false;
                        int size;
                        size = getSizeOfArrayObject(f.get(this));
                        String box = "[" + size + "]";
                        boxes.append(box);
                        temp = temp.getComponentType();
                    }else{
                        boxes.append("[]");
                        temp = temp.getComponentType();
                    }
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
               
                output.append(convertArrayToString(varClass.getComponentType(), f.get(this),indent+1));
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
    private <K> String convertArrayToString(Class<?> componentType, Object array, int indent){
        if(array == null) return "";
        StringBuilder output = new StringBuilder();
        int count = 0;
        
        if(componentType.isArray()){
            
            K[] newArray = convertArrayObjectToArray(array);
            System.out.println(Arrays.toString(newArray));
            
            
            
            for(K part : newArray){
                for (int i = 0; i < indent; i++) output.append("\t");
                String type;
                Class temp = componentType;
                StringBuilder boxes = new StringBuilder();
                boolean firstRun = true;
                while(temp.isArray()){
                    if(firstRun) {
                        firstRun = false;
                        int size;
                        size = getSizeOfArrayObject(part);
                        String box = "[" + size + "]";
                        boxes.append(box);
                        temp = temp.getComponentType();
                    }else{
                        boxes.append("[]");
                        temp = temp.getComponentType();
                    }
                }
                if(temp.isPrimitive()){
                    type = convertPrimitiveToClass(temp);
                }else{
                    type = temp.getName();
                }
    
                type += boxes.toString();
    
                String init = "[" + type + "]" + count + "=[\n";
                output.append(init);
                output.append(convertArrayToString(componentType.getComponentType(), part, indent+1));
                for (int i = 0; i < indent; i++) output.append("\t");
                output.append("]\n");
                count++;
            }
        }else{
            
            //Object[] objectAsArray = ((Object[]) array).length;
            K[] newArray = convertArrayObjectToArray(array);
            for(K part : newArray){
                for (int i = 0; i < indent; i++) output.append("\t");
                String type;
                if(componentType.isPrimitive()){
                    type = convertPrimitiveToClass(componentType);
                }else{
                    type = componentType.getName();
                }
                String init = "[" + type + "]" + count + "=";
                String data;
                try {
                   
                    if (Serializable.class.isAssignableFrom(part.getClass())) {
                        data = "{\n";
                        data += ((Serializable) part).stringOfFields(indent + 1);
                        for (int i = 0; i < indent; i++) data += '\t';
                        data += "}\n";
                    }else{
                        data = "(" + part.toString() + ")";
                    }
                   
                }catch (IllegalAccessException e){
                    data = "???";
                }
                
                String outcome = init + data + '\n';
                output.append(outcome);
                count++;
            }
            
            
        }
    
    
        return output.toString();
    }
    
    @SuppressWarnings("unchecked")
    public <T> T[] convertArrayObjectToArray(Object array){
        LinkedList<T> list = new LinkedList<>();
        Class componentType = array.getClass().getComponentType();
        try {
            if (componentType.isPrimitive()) {
                componentType = Class.forName(convertPrimitiveToClass(componentType));
            }
        }catch(ClassNotFoundException e){
            return null;
        }
        int index = 0;
        while(true){
            try{
                list.add((T) componentType.cast(Array.get(array, index)));
            }catch (ArrayIndexOutOfBoundsException e){
                break;
            }
            index++;
        }
    
        return (T[]) list.toArray();
    }
    
    @SuppressWarnings("unchecked")
    public int getSizeOfArrayObject(Object array){
        LinkedList<Object> list = new LinkedList<>();
       
    
        int index = 0;
        while(true){
            try{
                list.add(Array.get(array, index));
            }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                break;
            }
            index++;
        }
        
        return list.size();
    }
}
