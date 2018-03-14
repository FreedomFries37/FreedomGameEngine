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
    
    public int numberOfNodes(){
        return 1+numberOfChildren(head);
    }
    private int numberOfChildren(ParseNodeNonBinary root){
        int count = 0;
        for (ParseNodeNonBinary p: root.children) {
            count += numberOfChildren(p);
        }
        return count + 1;
    }
    
    public int height(){
        return height(head);
    }
    private int height(ParseNodeNonBinary root){
        int largestChildSize = 0;
        for(ParseNodeNonBinary p : root.children){
            int height = height(p);
            if(height > largestChildSize){
                largestChildSize = height;
            }
        }
        
        return 1 + largestChildSize;
    }

}
