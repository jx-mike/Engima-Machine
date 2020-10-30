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
 */
public class Enigma {

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
	 * Parses the rotor configuration from a line containing rotor indices.
	 * The line should look something like this:
	 * <pre>
	 *     3 7 2</pre>
	 * The method returns an array containing the indices specified on the line.
	 * 
	 * <p>If any of the specified indices is not a valid index into the
	 * {@code RotorConstants.ROTORS} array, the method should print the 
	 * specified error message to {@code System.out} and then quit the 
	 * program by calling {@code System.exit(-1)}.
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
		//store the string in to Scanner
		Scanner in=new Scanner(rotorIndicesLine);

		//remove leading and tailing spaces of the string.
		String trimedLine = rotorIndicesLine.trim();
		
		//to check if user entered only space instead of rotor numbers.
		if (trimedLine.equals("")) {
			System.out.println("You must specify at least one rotor");
			System.exit(-1);//error
		}
		
		//put string of rotor choices into a string array without any spaces.
		String[] rotorStrs = trimedLine.split(" +");

		int numOfIndices = rotorStrs.length;//get number of choices.
		
		//set up corresponding rotor array.
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
					System.exit(-1);
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
	 *        {@code RotorConstants.ROTORS}array.
	 * @return The array of rotors, each of which is itself an array of ints
	 *         copied from {@code RotorConstants.ROTORS}.
	 */
	public static int[][] setUpRotors(int[] rotorIndices) {
		// TODO left to the student
		
		//Initialize setUpRotors array.
		int [][] setUpRotors= new int [rotorIndices.length][26];
		
		//Main method loop
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
		
		//Set up a 2D array that stores notches with initialization.
		int [][] getRotorNotches= new int [rotorIndices.length][];
		
		//Main method loop
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
		
		//Initialize array of convertRotor.
		int [] convertRotor = new int [26];
		
		//Initialize a char array to store character of string.
		char[] text = new char [rotorText.length()];
		
		//Main method loop
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
		
		//Convert userInput into integer and store them into array.
		int [] convertToIndex = convertRotor(userInput);
		
		//set the start position of encoding.
		int start = convertToIndex[0];
		int forwardRun = 0;
		
		//Main method loop
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
	public static void advance(int[][] rotors, int[] rotorOffsets,
			int[][] rotorNotches) {
		// TODO left to the student
		
		//Main method loop
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
	 * Advances a single rotor by one position to the left. This is done by
	 *  moving the first value in the array to the end and shifting all the
	 *  other values down towards the beginning of the array.
	 * 
	 * 
	 * @param rotor Contents of the rotor to be advanced.
	 */
	public static void rotate(int[] rotor) {
		// TODO left to the student
		int tmp=rotor[0];
		
		
		//Main method loop
		for(int i = 0; i < rotor.length-1;i++)
		{
			rotor[i]=rotor[i+1];       
		}
		rotor[rotor.length-1]=tmp;
	}


}

