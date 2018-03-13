package tools;

import engine.Serializable;
import engine.special_objects.GameObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;

public class AssetTreeConverter {
    
    public class ValueOrList <T>{
        private boolean useValue;
        private LinkedList<ValueOrList> list;
        private T value;
        
        public ValueOrList(T val){
            useValue = true;
            value = val;
        }
        
        public ValueOrList(LinkedList<ValueOrList> list){
            useValue = false;
            this.list = list;
        }
    
        public boolean isUseValue() {
            return useValue;
        }
    
        public LinkedList<ValueOrList> getList() {
            return list;
        }
    
        public T getValue() {
            return value;
        }
    }
    
    public void testTreeConverter(){
        String type = "Double";
        String input = "3.0";
        
        ValueOrList vl1 = new ValueOrList<>(3.0);
        System.out.println(vl1.value.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T createFromType(String type, HashMap<String, ValueOrList> fieldsHashMap){
        try {
            T output = (T) Class.forName(type).newInstance();
            Field[] fields = output.getParams();
            int fieldIndex = 0;
            
            for (String fieldKey: fieldsHashMap.keySet()) {
                ValueOrList valueOrList = fieldsHashMap.get(fieldKey);
                Field currentField = fields[fieldIndex];
                if(valueOrList.isUseValue()){
                    currentField.set(output, valueOrList.getValue());
                }
                
                
                fieldIndex++;
            }
            
            
            
            return output;
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }
    
}
