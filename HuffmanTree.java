// Navya Mangipudi
//
// HuffmanTree is a class in which text files can be compressed using 
// Huffman coding. Huffman coding is a coding scheme that is based on the 
// frequency of characters in the text file. Those characters are then
// encoded in bits, with the amount of bits for each characters depending on 
// the frequency of the character.

import java.util.*;
import java.io.*;

public class HuffmanTree {
   private HuffmanNode overallRoot;
   
   // Post: Constructs the initial Huffman Tree by computing character frequencies
   // in a text file using an inputted list of integers representing characters.
   public HuffmanTree(int[] count) {
      Queue<HuffmanNode> q = new PriorityQueue<>();
      for (int i = 0; i < count.length; i++) {
         if (count[i] > 0) {
            q.add(new HuffmanNode(count[i], i));
         }
      }
      q.add(new HuffmanNode(1, count.length));
      overallRoot = HuffTree(q);
   }
   
   // Post: Helper method which has a queue of Huffman Nodes as an input
   // which are sorted based on frequency to create the Huffman tree.
   private HuffmanNode HuffTree(Queue<HuffmanNode> q) {
      while (q.size() != 1) {
         HuffmanNode h1 = q.remove();
         HuffmanNode h2 = q.remove();
         int totalData = h1.data + h2.data;
         q.add(new HuffmanNode(totalData, h1, h2));
      }
      return q.remove();
   }
   
   // Writes out the Huffman Tree in standard format to the output stream.
   // Standard format:
   // Sequence of line pairs.
   // First line of pair has integer value of stored character.
   // Second line of pair has binary code for character.
   public void write(PrintStream output) {
      writeHelper(output,"", overallRoot);
   }
   
   // Post: Private helper method to write out Huffman Tree in standard format.
   // Takes PrintStream output, Huffman node and string to write out 
   // each line of the Huffman tree in a text file. 
   private void writeHelper(PrintStream output, String s, HuffmanNode root) {
      if (leafNode(root)) {
         output.println(root.charVal);
         output.println(s);
      } else {
         writeHelper(output, s + "0", root.left);
         writeHelper(output,s + "1", root.right);
      }
   }

   // Post: Recreating Huffman Tree from a file using 
   // Scanner input which contains a tree in standard format.
   public HuffmanTree(Scanner input) {
      overallRoot = new HuffmanNode(-1, -1);
      while (input.hasNextLine()) {
         int n = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = HuffmanTreeHelper(n, code, overallRoot);
      }
   }
   
   // Post: Helper method to Huffman Tree constructor. Takes int n, String code, and 
   // Huffman Node in order to read/write through sequences of bits. Builds the tree
   // using file information and returns HuffmanNode. 
   private HuffmanNode HuffmanTreeHelper(int n, String code, HuffmanNode root) {
      if (code.length() == 0) {
         if (leafNode(root)) {
            return (new HuffmanNode(0, n));
         }
      } else {
         root = nullNode(root);
         if (code.startsWith("1")) {
            root.right = HuffmanTreeHelper(n, code.substring(1), root.right);
         } else {
            root.left = HuffmanTreeHelper(n, code.substring(1), root.left);
         }
      }
      return root;
   }
   
   // Post: Creates a new Huffman Node if the input Huffman Node's 
   // left or right values are null.
   private HuffmanNode nullNode (HuffmanNode n) {
      if (n.left == null) {
         n.left = new HuffmanNode(0, 0);
      } else if (n.right == null) {
         n.right = new HuffmanNode(0, 0);
      }
      return n;
   }
   
   // Post: This method reads individal bits in input and prints the corresponding character value
   // to the bit to an output file. Reverses earlier encoding process. 
   // Takes parameters input (BitInputStream), output PrintStream and int eof. When a character 
   // equal to the eof parameter is encountered, the method stops reading. 
   // Assume that input stream is of legally encoded characters.
   public void decode(BitInputStream input, PrintStream output, int eof) {
      int oVal = overallRoot.charVal;
      HuffmanNode temp = overallRoot;
      while (oVal != eof) {
         if (leafNode(temp)) {
            oVal = temp.charVal;
            if (temp.charVal != eof) {
               output.write(temp.charVal);
               temp = overallRoot;
            }
         } else {
            int bit = input.readBit();
            if (bit == 0) {
               temp = temp.left;
            } else {
               temp = temp.right;
            }
         }
      }
   }
   
   // Post: Detects if a Huffman Node is a leaf node.
   private boolean leafNode(HuffmanNode n) {
      return (n.left == null && n.right == null);
   }
   
   // Class that represents a single node in the Huffman tree
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public int data;
      public int charVal;
      public HuffmanNode left;
      public HuffmanNode right;
      
      // Constructs a leaf node with given data
      public HuffmanNode(int data, int charVal) {
         this.data = data;
         this.charVal = charVal;
      }
      
      // Constructs a branch node with given data, left subtree, right subtree
      public HuffmanNode(int data, HuffmanNode left, HuffmanNode right) {
         this.data = data;
         this.left = left;
         this.right = right;
      }
      
      // Post: takes in Huffman node h. Compares the values of two Huffman Nodes and 
      // determines if one is greater than, less than, or equal to the other depending 
      // on the returned value.
      public int compareTo(HuffmanNode h) {
         int n = this.data - h.data;
         return n;
      }
   }
}
