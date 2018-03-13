package tools;

import java.util.LinkedList;

class ParseNodeNonBinary {
    String data;
    LinkedList<ParseNodeNonBinary> children;
    ParseNodeNonBinary(String data){
        this.data = data;
        children = new LinkedList<>();
    }
    public String toString(){ return data;}
}
