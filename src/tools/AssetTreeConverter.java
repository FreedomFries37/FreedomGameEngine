package tools;

import engine.AssetFile;
import engine.Serializable;
import engine.special_components.Transform;
import engine.special_objects.Asset;
import engine.special_objects.GameObject;
import engine.special_objects.Sphere;

import utils.Vector3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class AssetTreeConverter {
    
    public class ValueOrHashMap <T>{
        private boolean useValue;
        private HashMap<String, ValueOrHashMap> stringToValueOrHashMap;
        private T value;
        
        @SuppressWarnings("unchecked")
        public ValueOrHashMap(T val){
            useValue = true;
            value = val;
        }
        
        public ValueOrHashMap(HashMap<String, ValueOrHashMap> stringToValueOrHashMap){
            useValue = false;
            this.stringToValueOrHashMap = stringToValueOrHashMap;
        }
    
        public boolean isUseValue() {
            return useValue;
        }
    
        public HashMap<String, ValueOrHashMap> getStringToValueOrHashMap() {
            return stringToValueOrHashMap;
        }
    
        public T getValue() {
            return value;
        }
    
    }
    
    public GameObject createGameObjectFromAssetTree(ParseTree tree){
        if(!tree.head.data.equals("<asset>")) return null;
        return parseGameObject(tree.head.children.get(0), "engine.special_objects.Void");
    }
    
    public ValueOrHashMap createPrimitiveValueOrList(String type, String input){
       
        try {
            Class<?> cls = Class.forName(type);
            boolean useString = false; //false = number
            boolean useChar = false; //false = number
            boolean useBool = false;
            String outputString = "";
            Number outputNumber = 0;
            Boolean outputBool = false;
            
            
            switch(cls.getName()){
                case "java.lang.Integer":
                    outputNumber = Integer.parseInt(input);
                    break;
                case "java.lang.Double":
                    outputNumber = Double.parseDouble(input);
                    break;
                case "java.lang.Character":
                    outputString = ""+ input.charAt(0);
                    useChar = true;
                    break;
                case "java.lang.Long":
                    outputNumber = Long.parseLong(input);
                    break;
                case "java.lang.Float":
                    outputNumber = Float.parseFloat(input);
                    break;
                case "java.lang.Boolean":
                    outputBool = Boolean.parseBoolean(input);
                    useBool = true;
                    break;
                case "java.lang.String":
                    outputString = input;
                    useString = true;
                    break;
                
            }
    
            ValueOrHashMap vl1;
            
            if(useChar){
                vl1 = new ValueOrHashMap<>(cls.cast(outputString.charAt(0)));
            }else if(useString){
                vl1 = new ValueOrHashMap<>(cls.cast(outputString));
            }else if(useBool){
                vl1 = new ValueOrHashMap<>(cls.cast(outputBool));
            }else {
                vl1 = new ValueOrHashMap<>(cls.cast(outputNumber));
            }
            
            
            System.out.println(vl1.value.getClass().getName() + ": " + vl1.value);
            return vl1;
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        
        //ValueOrHashMap vl1 = new ValueOrHashMap<>(3.0);
        return null;
    }
    
    public void testAssetTreeConverter(){
        ValueOrHashMap db1 = createPrimitiveValueOrList(Serializable.convertPrimitiveToClass(double.class), "3.0");
        ValueOrHashMap db2 = createPrimitiveValueOrList(Serializable.convertPrimitiveToClass(double.class), "4.0");
        ValueOrHashMap db3 = createPrimitiveValueOrList(Serializable.convertPrimitiveToClass(double.class), "5.0");
        
        
       
        HashMap<String, ValueOrHashMap> fieldsHashMapVector3 = new HashMap<>();
        fieldsHashMapVector3.put("x",db1);
        fieldsHashMapVector3.put("y",db2);
        fieldsHashMapVector3.put("z",db3);
        ValueOrHashMap vector3HashMap = new ValueOrHashMap(fieldsHashMapVector3);
    
        HashMap<String, ValueOrHashMap> fieldsHashMapTransform = new HashMap<>();
        fieldsHashMapTransform.put("position", vector3HashMap);
        
        
        Vector3 vector3 = createFromType("utils.Vector3", fieldsHashMapVector3);
        System.out.println(String.format("x:%f y:%f z:%f", vector3.x, vector3.y, vector3.z));
        Transform transform = createFromType("engine.special_components.Transform", fieldsHashMapTransform);
        System.out.println(String.format("Transform: x:%f y:%f z:%f", transform.position.x, transform.position.y, transform.position.z));
    
        ValueOrHashMap db4 = new ValueOrHashMap<>("Yolo");
        ValueOrHashMap db5 = createPrimitiveValueOrList(Serializable.convertPrimitiveToClass(double.class), "500");
    
        HashMap<String, ValueOrHashMap> fieldsHashMapSphere = new HashMap<>();
        fieldsHashMapSphere.put("radius",db5);
        fieldsHashMapSphere.put("gamma",db4);
        
        
        //Sphere s1 = createFromType("engine.special_objects.Sphere",fieldsHashMapSphere);
        Sphere s1 = createGameObjectFromType("MegaSphere", "engine.special_objects.Sphere", fieldsHashMapSphere);
        System.out.println(s1.radius);
    }
    
    public <T extends GameObject> T createGameObjectFromType(String name, String type, HashMap<String, ValueOrHashMap> fieldsHashMap){
        T output = createFromType(type, fieldsHashMap);
        output.setName(name);
        //output.initizalize();
        return output;
    }
    

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T createFromType(String type, HashMap<String, ValueOrHashMap> fieldsHashMap){
        try {
            Class clazz = Class.forName(type);
            
            //T output = (T) clazz.cast(Serializable.class.newInstance());
            T output = spoofConstructor(clazz);
            LinkedList<Field> fields = null;
            Field[] fArray = output.getParams();
            if(fArray != null) fields = new LinkedList<>(Arrays.asList(fArray));
            else return output;
           
    
            //Constructor constructor = clazz.getConstructors()[0];
            
            for (String fieldKey: fieldsHashMap.keySet()) {
                ValueOrHashMap valueOrHashMap = fieldsHashMap.get(fieldKey);
                Field currentField = null;
                for(Field f : fields){
                    String fieldName = f.getName();
                    if(fieldName.equals(fieldKey)){
                        currentField = f;
                        break;
                    }
                }
                if(currentField == null) continue;
                if(valueOrHashMap.isUseValue()){
                    currentField.set(output, valueOrHashMap.getValue());
                }else{
                    String nextType = currentField.getType().toString();
                    nextType = nextType.substring(nextType.indexOf(' ') + 1);
                    currentField.set(output, createFromType(nextType, valueOrHashMap.getStringToValueOrHashMap()));
                }
                
                
                
            }
            
            
            
            return output;
        }catch(ClassNotFoundException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Finds a constructor from a class and tries to fill it in and return an instance of that class
     * @param type the class of the object
     * @param <T> allows the method to work as a generic
     * @return an instance of the class type
     */
    @SuppressWarnings("unchecked")
    public static <T> T spoofConstructor(Class<?> type){
        T output = null;
        Constructor<?>[] constructors = type.getConstructors();
        Constructor lowestArgs = null;
        try {
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
            
                    return (T) constructor.newInstance();
                    
                }
                if(lowestArgs == null || lowestArgs.getParameterCount() > constructor.getParameterCount()){
                    lowestArgs = constructor;
                }
            }
            if(lowestArgs == null) return null;
            Object passThrough[] = new Object[lowestArgs.getParameterCount()];
            for(int i = 0; i < passThrough.length; i++){
                if(lowestArgs.getParameterTypes()[i].isPrimitive()){
                    passThrough[i] = 0;
                }
            }
            output = (T) lowestArgs.newInstance(passThrough);
            
            
            
            
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
            return null;
        }
        
        
        return output;
    }
    
    
    private String parseString(ParseNodeNonBinary node){
        if(!(node.data.equals("<stringEx>") || node.data.equals("<string>"))) return null;
        
        
        return node.children.get(0).data;
    }
    
    private GameObject parseGameObject(ParseNodeNonBinary node, String type){
        if(!node.data.equals("<gameobject>")) return null;
        ParseNodeNonBinary objectName, fieldlist, childrenlist, componentlist;
        objectName = node.children.get(0);
        fieldlist = node.children.get(1);
        childrenlist = node.children.get(2);
        componentlist = node.children.get(3);
        
        
        
        String name = parseString(objectName.children.get(0));
        HashMap<String, ValueOrHashMap> fieldlistmap = parseFieldList(fieldlist);
        LinkedList<GameObject> childrenList = parseChildrenList(childrenlist);
        
        GameObject output = createGameObjectFromType(name, type, fieldlistmap);
        if(childrenList != null){
            for(GameObject g : childrenList){
                output.addChild(g);
            }
        }
        
        return output;
    }
    
    private LinkedList<GameObject> parseChildrenList(ParseNodeNonBinary node){
        if(!node.data.equals("<childrenlist>")) return null;
        if(node.children.size() == 0) return new LinkedList<>();
        LinkedList<GameObject> list = new LinkedList<>();
        ParseNodeNonBinary type, gameObject, next;
        type = node.children.get(0);
        gameObject = node.children.get(1);
        next = node.children.get(2);
        
        String typeString = parseType(type);
        list.add(parseGameObject(gameObject, typeString));
        if(next.children.size() > 0){
            list.addAll(parseChildrenList(next));
        }
        
        return list;
    }
    
    private HashMap<String, ValueOrHashMap> parseFieldList(ParseNodeNonBinary node){
        if(!node.data.equals("<fieldlist>")) return null;
        if(node.children.size() == 0) return new HashMap<>();
        
        HashMap<String, ValueOrHashMap> map = new HashMap<>();
        ParseNodeNonBinary type, name, tail, data, next;
        type=node.children.get(0);
        name=node.children.get(1);
        tail=node.children.get(2);
        data=tail.children.get(0);
        next = (tail.children.size()>1) ? tail.children.get(1) : null;
        
        String typeString = parseType(type);
        String nameString = parseString(name);
    
        ValueOrHashMap valueOrHashMap = null;
        
        if(data.data.equals("<fieldlist>")) {
            valueOrHashMap = new ValueOrHashMap(parseFieldList(data));
            
        }else if(data.data.equals("<value>") || data.data.equals("<stringEx>")){
            valueOrHashMap = createPrimitiveValueOrList(typeString, data.children.get(0).data);
        }
    
        if(valueOrHashMap!= null) map.put(nameString, valueOrHashMap);
        if(next != null){
            HashMap<String, ValueOrHashMap> nextMap = parseFieldList(next);
            if(nextMap!= null) map.putAll(nextMap);
        }
    
        return map;
    }
    
    private String parseType(ParseNodeNonBinary node){
        if(!node.data.equals("<type>")) return null;
        StringBuilder builder = new StringBuilder();
        
        ParseNodeNonBinary ptr = node;
        while(ptr != null){
            builder.append(parseString(ptr.children.get(0)));
            
            if(ptr.children.size() == 1) ptr = null;
            else{
                ptr = ptr.children.get(1).children.get(0);
                builder.append('.');
            }
            
        }
        
        
        return builder.toString();
    }
    
    
    
}
