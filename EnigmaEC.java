///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Enigma
// Files:            RotorConstants.java
// Semester:         CS302 Spring 2013
//
// Author:           Jianxing Chen (jchen245@wisc.edu)
// CS Login:         jianxing
// Lecturer's Name:  Melissa Tress
// Lab Section:      341
//
//                   PAIR PROGRAMMERS COMPLETE THIS SECTION
// Pair Partner:     Zheng Gao
// CS Login:         Zhengg

// Lecturer's Name:  Deb Deppeler
// Lab Section:      328
//
//                   STUDENTS WHO GET HELP FROM ANYONE OTHER THAN THEIR PARTNER
// Credits:          (list anyone who helped you write your program)
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.*;

/**
 * Simplified implementation of the World War II-era German Engima cipher
 * machine. (http://en.wikipedia.org/wiki/Enigma_machine)
 * This version includes Extra Credit methods.
 */
public class EnigmaEC {

    /**
     * Application entry point. Prompts the user for configuration information
     * and then permits them to enter lines of text to be encoded. After the
     * user enters a line to be encoded, its encoded representation is printed
     * out.
     * 
     * <p>This is where you should put your welcome messages, the configuration 
     * prompt, and most importantly, your main program loop. This is also the 
     * place where you should declare and use the "Suggested Data Structures" 
     * from the P2 Specifications Page.
     * 
     * <p>When calling other methods, main() must respect the requirements of 
     * those methods. For example, main() should never call encode() with a 
     * non-upper-case character.
     * 
     * @param args Ignored.
     */
    public static void main(String[] args) {
        // TODO left to the student
    	//Declare variables.
    			char letter;
    			char codedLetter;
    			int [] reflector=convertRotor(RotorConstants.REFLECTOR);
    			Scanner stdr = new Scanner(System.in);
    			//Welcome Message
    			System.out.println("Willkommen auf der Enigma-Maschine!");
    			System.out.println("Please enter a Rotor Configuration.");
    			System.out.println("This must be a list of numbers in the range from" +
    					" 0 to 8, separated by spaces.");
    			System.out.println("Note that rotor 0 is the identity rotor.");//Prompt.

    			String userChoice = stdr.nextLine();//get user input of rotor choices.
    			
    			//put user choices in an array.
    			int [] rotorIndices = parseRotorIndices(userChoice);
    			
    			//set up rotorOffsets.
    			int [] rotorOffsets = new int [rotorIndices.length];
    			
    			//set up rotors according to user choices.
    			int [][] rotors = setUpRotors(rotorIndices);
    			
    			//get notches position of rotors user selected
    			int[][] rotorNotches = getRotorNotches(rotorIndices);
    			
    			System.out.println("Enter pairs of letters to connect" +
    					" on the plugboard. Separate pairs by spaces:");
    			String plugs = stdr.nextLine();
    			int [][] parsePlugs = parsePlugs(plugs);
    			


    			//Prompt to tell user to enter text to be encoded.
    			System.out.println("Enter lines of text to be encoded: ");
    			System.out.println();
    			
    			//Main program loop.
    			while(stdr.hasNextLine()){
    				String userInput = stdr.nextLine().toUpperCase();
    				System.out.print("Encoded Result: ");

    				for (int i = 0; i< userInput.length(); i++){

    					letter = userInput.charAt(i);
    					if(letter >='A' && letter<='Z'){
    						codedLetter = encode(rotors,reflector,letter);
    						System.out.print(codedLetter);
    						advance(rotors,rotorOffsets,rotorNotches);
    					}
    					else{
    						System.out.print(letter);
    					}
    				}
    			}
		
		
		
    	
    	
    }

    /**
     * Parses the plugboard configuration from a line of letter pairs. The line
     * of pairs should look something like this:
     * <pre>
     *    TY BG UI</pre>
     * That is, it will consist of a sequence of upper-case letter pairs,
     * separated by spaces. This method should return an array of pairs of
     * integers. Each pair is itself represented as an array. Each of these
     * pairs should contain the indices of the two letters that were specified
     * for that pair. For example, if the input to this method is the string
     * shown above, then the return value should be the following 2D array:
     * <pre>
     *     { { 19, 24 },
     *       {  1,  6 },
     *       { 20,  8 } }</pre>
     * If the line is invalid (which could happen if one of the pairs has more
     * than 2 or less than 2 letters, or if the letters aren't upper-case),
     * this method should print an appropriate error message to
     * {@code System.out} and then quit the program by calling
     * {@code System.exit(-1)}.
     * 
     * <p>This method should also check if any letters are used twice in the
     * list of plug pairs. If any letter is used more than once (either in the
     * same pair or different pairs), the method should print an appropriate
     * error message to {@code System.out} and then quit the program by calling
     * {@code System.exit(-1)}.
     * 
     * @param plugLine The line of text containing the letter pairs.
     * @return An array of pairs to be swapped at the plugboard.
     *         Each row should represent a pair of letters. Hence, each row 
     *         must always be exactly of length two.
     */
    public static int[][] parsePlugs(String plugLine) {
        // TODO left to the student
    	Scanner in=new Scanner (plugLine);
    	int [][] parsePlugs= 
    			new int [plugLine.replaceAll("\\s","").length()/2][2];
    	int []check = new int [plugLine.replaceAll("\\s","").length()];
    	
    	
    	//to store the first letter string before the first space
    	String a = in.next();
    	
    	
    	//check if userInput is upperCase and are pairs of letters. 
    	for (int i = 0; i<plugLine.length();i++){
    		String upperInput = plugLine.toUpperCase();
    		if (a.toUpperCase().equals(a)&&a.length()==2  ){			    			
    			parsePlugs[0]=convertRotor(a);
    			parsePlugs[i+1]=convertRotor(in.next());
    		}
    		else{
    			System.out.println("Invalid plug pair. You must enter" +
    					" a pair of upper-case letters with no characters" +
    					" in between.");
    			System.exit(-1);
    		}
    	}
    	
    	//convert 2D array parsePlugs to check if there are same letters in
    	//userInput.
    	int m=0;
		int n=1;
    	for(int i=0;i<parsePlugs.length;i++){
    		check[i]=parsePlugs[m][n];
    	}
    	
    	//check if there are repetitive letters in array check.
    	for(int i =0;i<check.length;i++){
    		for(int j=1;j<check.length;j++){
    			if(check[i]==check[j]&&i!=j){
    				System.out.println("You cannot use the same letters" +
    						" on the plugboard.");
					System.exit(-1);
    			} 			
    		}		
    	}
        return parsePlugs;
    }

    /**
     * Parses the rotor configuration from a line containing rotor indices.
     * The line should look something like this:
     * <pre>
     *     3 7 2</pre>
     * The method returns an array containing the indices specified on the line.
     * 
     * <p>If any of the specified indices is not a valid index into the
     * {@code RotorConstants.ROTORS} array, the method should print the 
     * specified error message to {@code System.out} and then quit the program 
     * by calling {@code System.exit(-1)}.
     * 
     * <p>The user is not permitted to use the same rotor twice. If the user
     * tries to specify the same rotor multiple times, this method should print
     * the specified error message to {@code System.out} and then quit the
     * program by calling {@code System.exit(-1)}.
     * 
     * @param rotorIndicesLine Line of text containing rotor indices, separated
     *        by spaces.
     * @return An array of rotor indices to use.
     */
    public static int[] parseRotorIndices(String rotorIndicesLine) {
        // TODO left to the student
Scanner in=new Scanner(rotorIndicesLine);
		
		String trimedLine = rotorIndicesLine.trim();
		if (trimedLine.equals("")) {
			System.out.println("You must specify at least one rotor");
			System.exit(-1);// error
		}
		String[] rotorStrs = trimedLine.split(" +");
		
		int numOfIndices = rotorStrs.length;
		int[] parseRotorIndices=new int[numOfIndices];
		for (int i = 0;i<rotorIndicesLine.length();i++){
			if(in.hasNextInt()){
				int a = in.nextInt();
				if(a < 0||a>8){
					System.out.println("Invalid rotor. You must enter an" +
							" integer between 0 and 8.");
					System.exit(-1);//error
				}
				parseRotorIndices[i]=a;
			}
		}
		for (int i=0;i<parseRotorIndices.length-1;i++){
			for(int j=1;j<parseRotorIndices.length;j++){
				if(parseRotorIndices[i]==parseRotorIndices[j]&&i!=j){
					System.out.println("You cannot use the same rotor twice.");
					System.exit(-1);//error
				}
			}
		}
		return parseRotorIndices;
    }

    /**
     * Builds an array of rotor ciphers to use, based on the user's selection.
     * The array of rotor ciphers will be a 2D array, where each "row" of the 
     * array would represent a given rotor's rotor cipher in integral form.
     * The number of rows will be equal to the number of rotors in use, as 
     * specified by the length of rotorIndices.
     * 
     * <p> Hint: You will need to access the rotor ciphers contained in 
     * RotorConstants, and convert them into integral form by using 
     * convertRotor().
     * 
     * @param rotorIndices The indices of the rotors to use. Each value in this
     *        array should be a valid index into the 
     *        {@code RotorConstants.ROTORS} array.
     * @return The array of rotors, each of which is itself an array of ints
     *         copied from {@code RotorConstants.ROTORS}.
     */
    public static int[][] setUpRotors(int[] rotorIndices) {
        // TODO left to the student
    	int [][] setUpRotors= new int [rotorIndices.length][26];
		for(int r=0;r<rotorIndices.length;r++){
			String rotorsText = RotorConstants.ROTORS[rotorIndices[r]];
			setUpRotors[r]=convertRotor(rotorsText);
		}
		return setUpRotors;
    }

    /**
     * Gets a 2D array containing the notch positions for each rotor being used.
     * Each "row" of the array will represent the notch positions of a single 
     * rotor. A rotor may have more than one notch position, so thus, each "row"
     * will contain one or more integers. There may be multiple rows, if 
     * multiple rotors are in use. Note that this array may be jagged, since 
     * different rotors have different numbers of notch positions.
     * 
     * <p> <pre>
     *     Example:
     *     Input: [2, 1, 3]
     *     Output: A 2D Array that would look something like this:
     *            [[Array of notch positions of Rotor 2],
     *             [Array of notch positions of Rotor 1],
     *             [Array of notch positions of Rotor 3]]</pre>
     * 
     * @param rotorIndices The indices of the rotors to use. Each value in this
     *        array should be a valid index into the 
     *        {@code RotorConstants.ROTORS} array.
     * @return An array containing the notch positions for each selected rotor.
     */
    public static int[][] getRotorNotches(int[] rotorIndices) {
        // TODO left to the student
    	int [][] getRotorNotches= new int [rotorIndices.length][];
		for (int r =0; r<rotorIndices.length;r++){
			getRotorNotches[r]=RotorConstants.NOTCHES[rotorIndices[r]];
		}

		return getRotorNotches;
    }

    /**
     * Converts a rotor cipher from its textual representation into an integer-
     * based rotor cipher. Each int would be in the range [0, 25], representing  
     * the alphabetical index of the corresponding character.
     * 
     * <p>The mapping of letters to integers works as follows: <br />
     * Each letter should be converted into its alphabetical index. 
     * That is, 'A' would map to 0, 'B' should map to 1, etc. until we reach 
     * 'Z', which would map to 25.
     * 
     * <p><pre>
     * Example:
     * Input String: EKMFLGDQVZNTOWYHXUSPAIBRCJ
     * Output Array: [4 10 12 5 11 6 3 16 21 25 13 19 14 22 24 7 23 20 18 15 0 
     *                8 1 17 2 9]</pre>
     * 
     * @param rotorText Textual representation of the rotor. This will be like
     *        the Strings in {@code RotorConstants.ROTORS}; that is, it will be 
     *        a String containing all 26 upper-case letters.
     * @return Representation of the rotor as an array of values between 0 and
     *         25, inclusive.
     */
    public static int[] convertRotor(String rotorText) {
        // TODO left to the student
    	int [] convertRotor = new int [26];
		char[] text = new char [rotorText.length()];
		for (int i=0;i<text.length;i++){
			char letter = rotorText.charAt(i);
			if(letter>='A'&&letter<='Z'){
				convertRotor[i]= (int)letter - 'A';
			}
		}
		return convertRotor;
    	
    	
    	
    	
    	
    	
    	
        
    }

    /**
     * Runs one character through the Enigma encoding rotors.
     * 
     * <p>To do this, you will first need to convert input into its 
     * alphabetical index, e.g. 'A' would be 0, 'B' would be 1, etc.
     * Next, you would run the letter through the rotors in forward order.
     * Do this by using your converted-letter to index into a given row of 
     * the rotors array. Then, apply the reflector, and run the letter through 
     * the rotors again, but in reverse. Encoding in reverse not only implies 
     * that the rotor-order is to be reversed. It also means that each cipher
     * is applied in reverse. An example:
     * 
     * <pre>
     *      Cipher (input):     EKMFLGDQVZNTOWYHXUSPAIBRCJ
     *      Alphabet (output):  ABCDEFGHIJKLMNOPQRSTUVWXYZ
     * </pre>
     * While encoding in reverse, 'E' would get encoded as 'A', 'K' as 'B', 
     * etc. (In the forward direction, 'E' would get encoded as 'L') 
     * 
     * Finally, convert your letter back to 
     * a traditional character value.
     * 
     * @param rotors The rotor ciphers in their current configuration, each in
     *         integral rotor cipher form. Each "row" of this array represents 
     *         a rotor cipher.
     * @param reflector The special reflector rotor in integral rotor cipher 
     *         form.
     * @param input The character to be encoded. Must be an upper-case letter.
     * @return The result of encoding the input character. This must also be an
     *         upper-case letter.
     */
    public static char encode(int[][] rotors, int[] reflector, char input) {
        // TODO left to the student
           	
    	String userInput = Character.toString(input);
		int [] convertToIndex = convertRotor(userInput);
		int start = convertToIndex[0];
		int forwardRun = 0;
		for(int r=0; r<rotors.length;r++){
			forwardRun = rotors[r][start];
			start = forwardRun;
		}
		char reflectorCheck = RotorConstants.REFLECTOR.charAt(forwardRun);
		String afterReflector = Character.toString(reflectorCheck);
		int [] reflectorConvert = convertRotor(afterReflector);
		int reverseRun = reflectorConvert[0];
		for(int r=rotors.length-1;r>=0;r--){
			int c=0;
			while(reverseRun!=rotors[r][c]&&c<rotors[r].length){
				c++;
			}
			reverseRun=c;

		}

		char outPut=RotorConstants.ROTORS[0].charAt(reverseRun);

		return outPut;
    }

    /**
     * Runs one character through both the Enigma plugboard and the 
     * encoding rotors.
     * 
     * <p>First check to see if the letter is one of the plugboard pairs (it is 
     * if it can be found in the plugs array). If so, swap it with the other 
     * letter in its pair. Then encode using the rotors as before. Then take 
     * the resultant letter and apply the plugboard yet again.
     * 
     * <p>Hint: You will need to use swapPluggedPairs() somewhere within this 
     * method.
     * 
     * @param rotors The rotor ciphers in their current configuration, each in
     *         integral rotor cipher form. Each "row" of this array represents 
     *         a rotor cipher.
     * @param reflector The special reflector rotor in integral rotor cipher 
     *         form.
     * @param plugs The plugboard configuration, as an array of pairs to swap.
     *        Each row represents a pair of letters. Hence, each row 
     *        must always be exactly of length two.
     *        A value of null indicates that the plugboard should not be used.
     * @param input The input character. Must be an upper-case letter.
     * @return The encoded output character. This must also be an
     *         upper-case letter.
     */
    public static char encodeWithPlugs(int[][] rotors, int[] reflector, int[][] plugs, char input) {
        // TODO left to the student
        return 0;
    }

    /**
     * Uses the plugboard configuration to swap pairs of letters.
     * The letter should be an alphabetical index
     * 
     * @param plugs The plugboard configuration, as an array of pairs to swap.
     *              Each row represents a pair of letters. Hence, each row 
     *              must always be exactly of length two.
     * @param letter Letter to be run through the plugboard. This value must be 
     *              in the range 0-25 (inclusive), as it should represent the 
     *              alphabetical index of a letter. e.g. 'A' would be 0, 
     *              'B' would be 1, etc.
     *             
     * @return The swapped value. The return value may be unchanged from the
     *         value parameter if the value did not correspond to any swap
     *         pairs in the plugboard configuration.
     */
    public static int swapPluggedPairs(int[][] plugs, int letter) {
        // TODO left to the student
        return 0;
    }

    /**
     * Advances the rotors by one position. Note that the reflector never
     * advances; it stays stationary.
     * 
     * <p> This advancement should take place, starting at rotor at index 0
     * of rotors. The 0th rotor will always be rotated. After rotating it, you 
     * would then update its corresponding offset in rotorOffsets. Then, check 
     * to see if the current offset matches a notch (meaning that a notch 
     * has been reached). If so, the advancement should continue on to the next 
     * rotor, following the same process. Otherwise, advancement should halt.
     * 
     * <p>Hint: You will probably want to call rotate() within this method.
     * 
     * @param rotors The array of rotor ciphers in their current configuration. 
     *         The rotor at index 0 is the first rotor to be considered for 
     *         advancement. It will always rotate exactly once. The remaining 
     *         rotors may advance, but not as often. 
     *         The data in this array will be updated to show the rotors' new 
     *         positions.
     * @param rotorOffsets The current offsets by which the rotors have been
     *        rotated. These keep track of how far each rotor has rotated since 
     *        the beginning of the program. The offset at index i will 
     *        correspond to rotor at index 0 of rotors. e.g. offset 0 pertains 
     *        to the 0th rotor cipher in rotors.
     *        Will be updated in-place to reflect the new offsets after
     *        advancing.
     * @param rotorNotches The positions of the notches on each of the rotors.
     *        Each row of this array represents the notches of a certain rotor.
     *        The ith row will correspond to the notches of the ith rotor  
     *        cipher in rotors.
     *        Only when a rotor advances to its notched position does the next
     *        rotor in the chain advance.
     */
    public static void advance(int[][] rotors, int[] rotorOffsets, int[][] rotorNotches) {
        // TODO left to the student
    	for(int i=0; i<rotors.length; i++){
			rotate(rotors[i]);
			rotorOffsets[i]++;
			if(rotorOffsets[i] == 26){
				rotorOffsets[i]=0;
			}


			boolean matched = false;


			for(int j = 0; j< rotorNotches[i].length; j++ ){
				if(rotorOffsets[i] == rotorNotches[i][j]){


					matched = true;
				}
			}

			if (!matched) {
				break;
			}
		}
    }

    /**
     * Advances a single rotor by one position. This is done by moving the
     * first value in the array to the end and shifting all the other values
     * down towards the beginning of the array.
     * 
     * @param rotor Contents of the rotor to be advanced.
     */
    public static void rotate(int[] rotor) {
        // TODO left to the student
    	int tmp=rotor[0];
		for(int i = 0; i < rotor.length-1;i++)
		{
			rotor[i]=rotor[i+1];       
		}
		rotor[rotor.length-1]=tmp;
    	
    }
}
