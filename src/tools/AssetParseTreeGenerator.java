package tools;

import engine.AssetFile;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class AssetParseTreeGenerator {
    
    //Use Recrusive Parser
    private char currentPointer;
    private int index;
    private String fileAsString;
    
    
    
    
    
    
    
    public AssetParseTreeGenerator(AssetFile assetFile){
        try {
            FileReader reader = new FileReader(assetFile);
            StringBuilder temp = new StringBuilder();
            while(reader.ready()){
               temp.append((char)reader.read());
            }
            fileAsString = temp.toString();
            index = 0;
            currentPointer = fileAsString.toCharArray()[index];
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    public ParseTree ReadAssetFile(){
        ParseNodeNonBinary nonBinaryTree = new ParseNodeNonBinary("<asset>");
        
        if(!checkGameObject(nonBinaryTree)) return null;
        ParseTree output = new ParseTree(nonBinaryTree);
        
        return output;
    }
    
    private void continuePointer(){
        index++;
        if(index < fileAsString.length()) currentPointer = fileAsString.toCharArray()[index];
    }
    
    /**
     * Checks if 'c' and the current char are equal
     * @param c the char to check against the current char pointer
     * @return if they are equal
     */
    private boolean matchChar(char c){
        return c == currentPointer;
    }
    
    /**
     *  Checks if 'c' and the current char are equal and consumes it
     * @param c the char to check against the current char pointer
     * @return if they are equal
     */
    private boolean consumeChar(char c){
        if(matchChar(c)){
            continuePointer();
            return true;
        }
        return false;
    }
    
    private boolean matchString(String s){
        int tempIndex = index;
        for(char c : s.toCharArray()){
            if(c!=fileAsString.toCharArray()[tempIndex]) return false;
            tempIndex++;
        }
        
        return true;
    }
    
    private boolean consumeString(String s){
        if(matchString(s)){
            for (int i = 0; i < s.length(); i++) {
                continuePointer();
            }
            return true;
        }
        
        return false;
    }
    
    
    
    private boolean checkString(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<string>");
    
        StringBuilder builder = new StringBuilder();
        int charValue = (int) currentPointer;
        boolean firstChecked = false;
        
        while(charValue == 95 || (firstChecked && charValue >= 48 && charValue <= 57) || (charValue>=65 && charValue <= 90) || (charValue>=97 && charValue <= 122)){
            builder.append(currentPointer);
            continuePointer();
            charValue = (int) currentPointer;
            if(!firstChecked) firstChecked = true;
        }
    
        nextNode.children.add(new ParseNodeNonBinary(builder.toString()));
        
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkStringFull(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<stringEx>");
    
        StringBuilder builder = new StringBuilder();
        int charValue = (int) currentPointer;
        boolean firstChecked = false;
    
        while(charValue != '"'){
            builder.append(currentPointer);
            continuePointer();
            charValue = (int) currentPointer;
            if(!firstChecked) firstChecked = true;
        }
    
        nextNode.children.add(new ParseNodeNonBinary(builder.toString()));
    
        parent.children.add(nextNode);
        return true;
    
    
        
    }
    
    
    
    
    
    private void consumeWhiteSpace(){
        while(currentPointer == '\n' || currentPointer == '\t' || currentPointer == ' '){
            consumeChar(currentPointer);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    private boolean checkGameObject(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<gameobject>");
        
        if(!checkObjectName(nextNode)) return false;
        consumeWhiteSpace();
        if(!checkFieldList(nextNode)) return false;
        consumeWhiteSpace();
        
        if(!consumeString("children={")) return false;
        consumeWhiteSpace();
        if(!checkChildrenList(nextNode)) return false;
        consumeWhiteSpace();
        if(!consumeChar('}')) return false;
        consumeWhiteSpace();
        
        if(!consumeString("components={")) return false;
        consumeWhiteSpace();
        if(!checkComponentList(nextNode)) return false;
        consumeWhiteSpace();
        if(!consumeChar('}')) return false;
        
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkObjectName(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<objectname>");
        if(!consumeString("name=\"")) return false;
        //nextNode.children.add(new ParseNodeNonBinary("name"));
        if(!checkStringFull(nextNode)) return false;
        //if(!consumeString("\"")) return false;
        if(!consumeChar('"')) return false;
    
        parent.children.add(nextNode);
        return true;
    }
    
    
    private boolean checkFieldList(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<fieldlist>");
        if(matchChar('[')){
            consumeChar('[');
            if(!checkType(nextNode)) return false;
            consumeChar(']');
            if(!checkString(nextNode)) return false;
            consumeChar('=');
            if(!checkFieldListTail(nextNode)) return false;
        }
    
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkFieldListTail(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<fieldlisttail>");
    
        if(consumeChar('{')){
            consumeWhiteSpace();
            if(!checkFieldList(nextNode)) return false;
            if(!consumeChar('}')) return false;
        }else if(consumeChar('(')){
            if(!checkValue(nextNode)) return false;
            if(!consumeChar(')')) return false;
        }else if(consumeChar('"')){
            if(!checkStringFull(nextNode)) return false;
            if(!consumeChar('"')) return false;
        }
        consumeWhiteSpace();
        if(matchChar('[')) {
            if(!checkFieldList(nextNode)) return false;
        }
    
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkType(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<type>");
        if(!checkString(nextNode)) return false;
        if(!checkTypeTail(nextNode)) return false;
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkTypeTail(ParseNodeNonBinary parent){
        if(consumeChar('.')) {
            ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<typetail>");
            if(!checkType(nextNode)) return false;
    
    
            parent.children.add(nextNode);
        }
        return true;
    }
    
    private boolean checkValue(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<value>");
        //if(!checkString(nextNode)) return false;
        
        StringBuilder builder = new StringBuilder();
        while(!matchChar(')')){
            builder.append(currentPointer);
            continuePointer();
        }
        nextNode.children.add(new ParseNodeNonBinary(builder.toString()));
        
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkChildrenList(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<childrenlist>");
        if(!matchChar('}')){
            if(matchString("[A]")) {
                consumeString("[A]");
                ParseNodeNonBinary nextNextNode = new ParseNodeNonBinary("<assetlocation>");
                if(!checkStringFull(nextNextNode)) return false;
                nextNode.children.add(nextNextNode);
            }else {
                if (!checkType(nextNode)) return false;
                if (!consumeChar('{')) return false;
                consumeWhiteSpace();
                if (!checkGameObject(nextNode)) return false;
                consumeWhiteSpace();
                if (!consumeChar('}')) return false;
               
            }
            consumeWhiteSpace();
            if (!checkChildrenList(nextNode)) return false;
        }
        
        
        
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkComponentList(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<componentlist>");
        if(!matchChar('}')){
            if(!checkComponent(nextNode)) return false;
            consumeWhiteSpace();
            if(!matchChar('}')){
                if(!checkComponentList(nextNode)) return false;
            }
        }
        
        
        parent.children.add(nextNode);
        return true;
    }
    
    private boolean checkComponent(ParseNodeNonBinary parent){
        ParseNodeNonBinary nextNode = new ParseNodeNonBinary("<component>");
    
        if(!checkType(nextNode)) return false;
        if(!consumeChar('{')) return false;
        consumeWhiteSpace();
    
        if(!checkFieldList(nextNode)) return false;
        consumeWhiteSpace();
        if(!consumeChar('}')) return false;
        
        parent.children.add(nextNode);
        return true;
    }
    
    
    
    
    
}
