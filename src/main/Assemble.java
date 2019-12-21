package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Assemble {
	public String finalMCode = "";
	public boolean useExceptionHandler = false;
	
	public Map<String, Integer> labels = new HashMap<>();
	public static Set<String> AtypeInstructions = new HashSet<>();
	public static Set<String> MtypeInstructions = new HashSet<>();
	public static Set<String> JtypeInstructions = new HashSet<>();
	public static Set<String> ItypeInstructions = new HashSet<>();
	public static Set<String> OtypeInstructions = new HashSet<>();
	
	public Map<String, Integer> linenumbers;
	public List<String> addresses;
	public List<String> instructions;
	public List<String> machineCodes;
	public List<String> comments;
	
	public List<Integer> breakPoints = new ArrayList<>();
	
	private Parser parser;
	
	public Assemble() {
		initAtypeInstructions();
		initMtypeInstructions();
		initJtypeInstructions();
		initItypeInstructions();
		initOtypeInstructions();
		
		linenumbers = new HashMap<>();
		addresses = new ArrayList<>();
		instructions = new ArrayList<>();
		machineCodes = new ArrayList<>();
		comments = new ArrayList<>();
		parser = new Parser();
	}
	
	public void assemble() {
		machineCodes = new ArrayList<>();
		
		for (int i = 0; i < instructions.size(); i++) {
			String instruction  = instructions.get(i);
			String[] inst = instruction.split("\\s+");
			String instructionType = getInsturctionType(inst[0]);

			String mcode = "0000000000000000";
			parser.labelMap = labels;
			
			if (instructionType == "A") {
				mcode = parser.parseAtype(inst);
			} else if (instructionType == "M") {
				mcode = parser.parseAtype(inst);
			} else if (instructionType == "I") {
				mcode = parser.parseItype(inst);
			} else if (instructionType == "J") {
				mcode = parser.parseJtype(inst);
			} else if (instructionType == "O") {
				mcode = parser.parseOtype(inst);
			} else if (instruction.contains("syscall")) {
				mcode = "termination code";
			}
			machineCodes.add(mcode);
		}
		
		finalMCode = "";
		for (int i = 0; i < machineCodes.size(); i++) {
			if (!machineCodes.get(i).equals("termination code")) {
				finalMCode = finalMCode + machineCodes.get(i) + ",\n";
			}
		}
		
		if (useExceptionHandler) {
			for (int i = machineCodes.size(); i < 251; i++) {
				finalMCode += "0101000000000000,\n";
			}
			
			finalMCode += "0111010000000000,\n"; 
			finalMCode += "0110000000000011,\n";
			finalMCode += "0111100011111101,\n";
			finalMCode += "0111100011111101,\n";
			finalMCode += "0111100000000000,\n";
		}
		
		finalMCode = finalMCode.substring(0,finalMCode.length() - 2);
		finalMCode += ";";
	}
	
	private static final String SPECIALMEMADDRUSED = "ERROR: The memory address in this instruction is reserved for special objects.";
	private static final String OPREARIONNOTFOUND = "ERROR: Operation %s is not defined";
	
	public List<String> syntaxPolicy(String[] inst) {
		List<String> errors = new ArrayList<String>();
	
		if (inst.length > 2) {
			if(AtypeInstructions.contains(inst[0]) || MtypeInstructions.contains(inst[0])) {
				if (inst[1].equals("1") || inst[1].equals("0")) {
					errors.add(SPECIALMEMADDRUSED);
				}
			}
		}
		
		if (!(AtypeInstructions.contains(inst[0]) || MtypeInstructions.contains(inst[0]) ||
				ItypeInstructions.contains(inst[0]) || JtypeInstructions.contains(inst[0]) ||
					OtypeInstructions.contains(inst[0])))
			errors.add(String.format(OPREARIONNOTFOUND, inst[0]));
		
		return errors;
	}
	
	public Set<String[]> checkSyntax() {
		Set<String[]> messageSet = new HashSet<String[]>();
		for(int i = 0; i < instructions.size(); i++) {
			String inst = instructions.get(i);
			List<String> errors = syntaxPolicy(inst.split("\\s+"));
			if(!errors.isEmpty()) {
				String error = "";
				for (String e : errors) {
					error += e;
					error += "\n";
				}
				String[] message = new String[] {
					String.valueOf(linenumbers.get(inst)), error
				};
				messageSet.add(message);
			}
		}
		return messageSet;
	}
	
	public void processFile(String codeFile) {
		if (codeFile == "")
			return;
		
		labels = new HashMap<>();
		addresses = new ArrayList<>();
		instructions = new ArrayList<String>();
		comments = new ArrayList<>();
		
		String[] codes = codeFile.split("\\r?\\n");
		int size = codes.length;
		
		int count = 0;
		int linenum = 1;
		boolean isLabel = false;
		boolean afterSyscall = false;
		
		for (int i = 0; i < size; i++) {
			String instruction = codes[i].trim();
			
			boolean empty = instruction.isEmpty();
			boolean nowords = instruction.equals("");
			boolean lineBreak = instruction.equals("\n");
			boolean commentOnly = instruction.startsWith("#");
			boolean skip = (empty || nowords || lineBreak || commentOnly);
			
			if (!skip) {
				int commentIdx = instruction.indexOf('#');
				
				if(instruction.contains(":")) {
					labels.put(instruction.substring(0, instruction.length()-1), count);
					linenumbers.put(instruction, linenum);
					isLabel = true;
				}
				if (!isLabel) {
					if (commentIdx > 0) {
						String comment = instruction.substring(commentIdx);
						instruction = instruction.substring(0, commentIdx-1);
						instructions.add(instruction);
						linenumbers.put(instruction, linenum);
						comments.add(comment);
					} else {
						instructions.add(instruction);
						comments.add("");
						linenumbers.put(instruction, linenum);
					}

					if(instruction.contains("syscall")) {
						addresses.add("Programe End");
						afterSyscall = true;
					} else {
						int addr = afterSyscall ? count-1 : count;
						addresses.add(String.format("%05X", addr));
					}
					
					count++;
				} else {
					isLabel = false;
				}
				linenum++;
			}
		}
	}
	
	public static String getInsturctionType(String inst) {
		if (AtypeInstructions.contains(inst)) {
			return "A";
		} else if (MtypeInstructions.contains(inst)) {
			return "M";
		} else if (JtypeInstructions.contains(inst)) {
			return "J";
		} else if (ItypeInstructions.contains(inst)) {
			return "I";
		} else if (OtypeInstructions.contains(inst)) {
			return "O";
		}
		return "";	
	}
	
	public static void initAtypeInstructions() {
		AtypeInstructions.add("add");
		AtypeInstructions.add("sub");
		AtypeInstructions.add("or");
		AtypeInstructions.add("and");
		AtypeInstructions.add("slt");
	}
	
	public static void initMtypeInstructions() {
		MtypeInstructions.add("load");
		MtypeInstructions.add("store");
	}
	
	public static void initItypeInstructions() {
		ItypeInstructions.add("addi");
		ItypeInstructions.add("ori");
		ItypeInstructions.add("andi");
		ItypeInstructions.add("loadi");
		ItypeInstructions.add("lui");
	}
	
	public static void initJtypeInstructions() {
		JtypeInstructions.add("j");
		JtypeInstructions.add("jp");
		JtypeInstructions.add("bz");
		JtypeInstructions.add("bnz");
	}
	
	public static void initOtypeInstructions() {
		OtypeInstructions.add("sl");
		OtypeInstructions.add("sr");
		OtypeInstructions.add("return");
	}
}
