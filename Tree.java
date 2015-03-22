
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		System.out.println("blah");
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {

		this.traverseAndCheck(this.root, oldTag, newTag);

		private void traverseAndCheck(TagNode root, String oldTag, String newTag){
			if(root = null){
				return
			}
			if(this.tag.equals(oldTag){
				this.tag = newTag;
			}
			traverseAndCheck(this.firstChild);
			traverseAndCheck(this.sibling);
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		this.traverseAndCheck(this.root, row);
		private void traverseAndCheck(TagNode root, int row){
			if(root = null){
				return
			}
			if(this.tag.equals("tr") ){
				TagNode tmp = this;
				for (int i = 1; i <=row ;i++ ) {
					tmp = tmp.sibling;
				}
				for (TagNode columnNode = tmp.firstChild; columnNode != null; columnNode = columnNode.sibling ) {
					columnNode.firstChild = TagNode boldNode = TagNode("b", columnNode.firstChild, null);
				}
				return;
			}
			traverseAndCheck(this.firstChild);
			traverseAndCheck(this.sibling);			
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		this.traverseAndCheck(this.root, tag);

		private void traverseAndCheck(TagNode root, String tag){
			if(root = null){
				return
			}
			if(this.tag.equals(tag){
				if(this.tag.equals("ol") || this.tag.equals("ul")){
					this.tag = this.firstChild.tag;
					TagNode tmpSibling = this.sibling;
					this.sibling = this.firstChild.sibling;
					TagNode siblingNode = this.sibling;
					while( siblingNode.sibling != null){
						siblingNode = siblingNode.sibling;
					}
					siblingNode.sibling = tmpSibling;

					this.replaceTag("li", "p"); 					
				}
				else{
					this.tag = this.firstChild.tag;
					TagNode tmpSibling = this.sibling;
					this.sibling = this.firstChild.sibling;
					TagNode siblingNode = this.sibling;
					while( siblingNode.sibling != null){
						siblingNode = siblingNode.sibling;
					}
					siblingNode.sibling = tmpSibling; 
				}
			}
			traverseAndCheck(this.firstChild);
			traverseAndCheck(this.sibling);
		}	
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {

		private void traverseAndCheck(TagNode root, String word, String tag){
			if(root = null){
				return
			}
			
			this.tag = this.tag + " ";
			Stack<String> tmpStack = new Stack<String>();

			for(int i = 0; i < this.tag.length(); i++){
				String toBeStacked = "";
				int letterBegin = 0;
				if(this.tag.charAt(i) == " "){
					tmpStack.push = this.tag.substring(letterBegin, i);
					letterBegin = i+1;
				}
			}
			boolean multiWords = false;
			TagNode lastChild = this.firstChild;
			TagNode lastSibling = this.sibling;
			TagNode lastNode = null;
			while(!tmpStack.isEmpty()){
				if(tmpStack.peek().substring(0,word.length()).equals(word)){
					//get the wordTag
					//
					TagNode tmpTagNode =  TagNode(tag, null, lastSibling);
					lastSibling = tmpTagNode;
					multiWords = false;
					TagNode tmpChildNode = TagNode(tmpStack.pop(), lastChild, null);
					lastChild = null;
				}
				else{
					if(multiWords){
						lastSibling.tag = tmpStack.pop() + lastSibling.tag;
					}
					else{
						TagNode tmpTagNode =  TagNode(tmpStack.pop(), lastChild, lastSibling);
						lastSibling = tmpTagNode;
						lastChild = null;
						multiWords = true;
					}
				}
			}
			this = lastSibling;


			
			traverseAndCheck(this.firstChild);
			traverseAndCheck(this.sibling);
		}
	}		
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}

	public static void main(String[] args) {
		try{
			Stack<String> htmlStack = new Stack<String>();
      		Scanner sc = new Scanner(new File("ex1.html"));
      		while(sc.hasNextLine()){
      			String lineString = sc.nextLine();
      			if(lineString.charAt(1) != '/'){
					htmlStack.push(lineString);
				}
				else{
					while(htmlStack.peek() != ("<" + lineString.substring(2,lineString.length())) {
						if(htmlStack.peek.charA(0) != "<"){
							String storeThis = htmlStack.pop();
							TagNode newNode = new TagNode(storeThis, null, root);
							root = newNode;
						}
						else{
							String storeThis = htmlStack.pop();
							TagNode newNode = new TagNode(storeThis.substring(1,storeThis.length()-1), root, null);
							root = newNode;
						}
					}
					String storeThis = htmlStack.pop();
					TagNode newNode = new TagNode(storeThis.substring(1,storeThis.length()-1), root, null);
					root = newNode;
					htmlStack.push(newNode.tag);
				}
			}
      	}
      	catch(FileNotFoundException e) {
            e.printStackTrace();   
        }
	}	
}
