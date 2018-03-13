package tools;

class ParseTree {
    
    ParseNodeNonBinary head;
    
    public ParseTree(ParseNodeNonBinary head){
        this.head = head;
    }
    
    
    public void printParseTree(){
        printParseTree(head,0);
    }
    private void printParseTree(ParseNodeNonBinary root, int indent){
        for (int i = 0; i < indent; i++) {
            System.out.print("\t");
        }
        System.out.println(root.data);
        
        for(ParseNodeNonBinary p : root.children){
            printParseTree(p, indent+1);
        }
    }

}
