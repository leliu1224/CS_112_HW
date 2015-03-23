package structures;

import java.util.*;

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
			Stack<String> htmlStack = new Stack<String>();
      		Scanner sc = this.sc;
      		
      		while(sc.hasNextLine()){ //while there is still a line in the file
      			String lineString = sc.nextLine(); // string of the entire line
      			
      			if(lineString.charAt(1) != '/'){ // if the char at the second spot != /
					htmlStack.push(lineString); // push the entire line into the stack
				}
				else{ //if the char at the second spot == /
					while(htmlStack.peek() != ("<" + lineString.substring(2,lineString.length()+1))) { // start popping: while the next in the stack != < + the string from 2 to end
						if(htmlStack.peek().charAt(0) != '<'){ // if what is the next spot != tag
							String storeThis = htmlStack.pop();
							TagNode newNode = new TagNode(storeThis, null, this.root);//create new node and point it sibling to the previous root 
							this.root = newNode;//set the new node as the new root
						}
						else{ // if the next spot == a tag
							String storeThis = htmlStack.pop();
							TagNode newNode = new TagNode(storeThis.substring(1,storeThis.length()), root, null); //store the tag word only, make the child point to the last root
							root = newNode;// set the new node as the new root
						}
					}
					// when the tag equals the end tag </em> == <em>
					String storeThis = htmlStack.pop();
					htmlStack.push(storeThis.substring(1,storeThis.length())); // push it back into the stack, but as a word instead of tag
				}
			}
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		this.traverseAndReplace(this.root, oldTag, newTag);
	}
	
	
	private void traverseAndReplace(TagNode r, String oldTag, String newTag){
		if(r == null){
			return;
		}
		if(r.tag.equals(oldTag)){
			r.tag = newTag;
		}
		traverseAndReplace(r.firstChild, oldTag, newTag);
		traverseAndReplace(r.sibling, oldTag, newTag);
	}
	

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		this.traverseAndBold(this.root, row);
	}
	private void traverseAndBold(TagNode r, int row){
		if(r == null){
			return;
		}
		if(r.tag.equals("tr") ){ // notice the first table row
			TagNode tmp = r; // tmp TagNode to keep track of the first table row
			for (int i = 1; i <=row ;i++ ) { // move to the selected row
				tmp = tmp.sibling; // by setting the previous row equal to the sibling
			}
			//reached the selected row
			for (TagNode columnNode = tmp.firstChild; columnNode.sibling != null; columnNode = columnNode.sibling ) { // creat new columnNode, set it equal to first td node; while columnNode's sibling is not null; 
				TagNode boldNode = new TagNode("b", columnNode.firstChild, null); // create a new boldNode and set it's child to the td' child 
				columnNode.firstChild = boldNode; // set the td's child to the new boldNode
			}
			return; //return so the recursion doesn't keep running
		}

		traverseAndBold(r.firstChild, row); // to keep traversing if the tag != tr
		traverseAndBold(r.sibling, row);			
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		this.traverseAndRemove(this.root, tag);
	}

		private void traverseAndRemove(TagNode r, String tag){
			if(root == null){
				return;
			}
			TagNode nextItChild = r.firstChild;
			TagNode nextItSibling = r.sibling;
			if(r.tag.equals(tag)){
				if(r.tag.equals("ol") || r.tag.equals("ul")){ // if the tag is ol or ul
					TagNode tmpSibling = r.sibling; // temporary hold the sibling of the ol or ul tag
					
					for(TagNode looping= r.firstChild; looping.sibling != null; looping = looping.sibling){ // loop through all the child of the ul or ol
						if(looping.tag.equals("li")){ // if they are li tags
							looping.tag = "p"; // replace them with p tags
						}
					}
					
					
					r.tag = r.firstChild.tag; //set tag to its child's tag which would now be a <p>
					TagNode tmpSiblingA = r.sibling; // temporary hold its sibling
					r.sibling = r.firstChild.sibling; // set it's sibling to its child's sibling; possibly a <p> tag
					r.firstChild = r.firstChild.firstChild; //set its child to its child's child
					
					TagNode siblingNode = r.sibling; // create a TagNode to hold the child(now its) siblings
					while(siblingNode.sibling != null){ // loop through all the siblings 
						siblingNode = siblingNode.sibling;
					}
					siblingNode.sibling = tmpSiblingA; // set the next sibling to the original's sibling
					

				}
				else{ // if they are not ol or ul tags 
					if(r.firstChild != null){ // if it has a child
						r.tag = r.firstChild.tag; //set tag to its child's tag
						TagNode tmpSibling = r.sibling; // temporary hold its sibling
						r.sibling = r.firstChild.sibling; // set it's sibling to its child's sibling 
						r.firstChild = r.firstChild.firstChild; //set its child to its child's child
						
						TagNode siblingNode = r.sibling; // great a TagNode to hold the child(now its) siblings
						while(siblingNode.sibling != null){ // loop through all the siblings 
							siblingNode = siblingNode.sibling;
						}
						siblingNode.sibling = tmpSibling; // set the next sibling to the original's sibling
					}
					else if(r.sibling != null){ // it has no child but has a sibling
						r.tag = r.sibling.tag; // set its tag to its sibling's tag
						r.sibling = r.sibling.sibling; // set it's sibling to its sibling's sibling
						r.firstChild = r.sibling.firstChild; // set its child to its sibling's child
						
					}
					else{ // no child and no sibling
						r = null; // just simply removal
					}
				}
			}
			traverseAndRemove(nextItChild, tag);
			traverseAndRemove(nextItSibling, tag);
		}	
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		this.traverseAndAdd(this.root, word, tag);
	}

		private void traverseAndAdd(TagNode r, String word, String tag){
			if(r == null){
				return;
			}
			TagNode nextItChild = r.firstChild;
			TagNode nextItSibling = r.sibling;
			
			r.tag = r.tag + " "; // add a space to the tag
			Stack<String> tmpStack = new Stack<String>(); // new stack to hold stuff

			for(int i = 0; i < r.tag.length(); i++){ // loop through the entire string of the tag
				String toBeStacked = "";
				int letterBegin = 0; //beging of each word
				if(r.tag.charAt(i) == ' '){ // if the loop reaches a space
					tmpStack.push(r.tag.substring(letterBegin, i)); // push the word into the stack
					letterBegin = i+1; //set the next beginning of the new word
				}
			}
			boolean multiWords = false; // boolean to keep track of if it is multiple word that don't match
			TagNode lastChild = r.firstChild; // keep track of the child and siblings
			TagNode lastSibling = r.sibling;
			TagNode lastNode = null;
			while(!tmpStack.isEmpty()){ // while there are thing in the stack
				if(tmpStack.peek().length() >= word.length() && tmpStack.peek().length() <= (word.length()+1) && tmpStack.peek().substring(0,word.length()).equalsIgnoreCase(word) && (tmpStack.peek().length() == word.length() || isPunctuation(tmpStack.peek().charAt(word.length())))){ //make sure the beginning matches the word; the word does not exceed word +1 and is big enough 
					// is the string is larger than word, make sure the last index is a punctuation
					TagNode tmpTagNode = new TagNode(tag, null, lastSibling); //create new node and set it's sibling to the last sibling(starting with the r.sibling)
					lastSibling = tmpTagNode; // change the last sibling
					multiWords = false;
					TagNode tmpChildNode = new TagNode(tmpStack.pop(), lastChild, null);// create new node to hold the word, set child to the r.firstchild(only when its at the end)
					lastChild = null; // indicates it is no longer at the end, so no more childs
					lastSibling.firstChild = tmpChildNode;//make the tag's child the word
				}
				else{// if the words don't match at all
					if(multiWords){
						lastSibling.tag = tmpStack.pop() + lastSibling.tag;
					}
					else{
						TagNode tmpTagNode = new TagNode(tmpStack.pop(), lastChild, lastSibling);
						lastSibling = tmpTagNode;
						lastChild = null;
						multiWords = true;//is the next word doesnt match, it will just add to the tag
					}
				}
			}
			
			traverseAndAdd(nextItChild, word, tag);
			traverseAndAdd(nextItSibling, word, tag);
		}
	     
	     private static boolean isPunctuation(char c) {
	         return c == ','
	             || c == '.'
	             || c == '!'
	             || c == '?'
	             || c == ':'
	             || c == ';'
	             ;
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
	public static void main(String[] args){
		System.out.println("blah");
	}
}
