package main;
import java.util.HashMap;
import java.util.Map;

public class Parser {
	public HashMap<String, String> opcodeMap;
	public Map<String, Integer> labelMap;
	
	private static final String SP_ADDR = "0000000001";
	private static final String BANKSELADDR = "0000000000";
	private static final String CAUSEADDR = "1000000000";
	
	public Parser() {
		opcodeMap = new HashMap<>();
		labelMap = new HashMap<>();
		initOpcodeMap();
	}
	
	private void initOpcodeMap() {
		opcodeMap.put("add",  "00000");
		opcodeMap.put("sub",  "00001");
		opcodeMap.put("or",   "00100");
		opcodeMap.put("and",  "00010");
		opcodeMap.put("slt",  "00011");
		opcodeMap.put("loadi", "01010");
		opcodeMap.put("addi", "10000");
		opcodeMap.put("ori",  "10100");
		opcodeMap.put("andi", "10010");
		opcodeMap.put("lui",  "10011");
		opcodeMap.put("load", "01110");
		opcodeMap.put("store", "01000");
		opcodeMap.put("bz",   "01100");
		opcodeMap.put("bnz",  "01101");
		opcodeMap.put("j",    "01111");
		opcodeMap.put("jp",   "01011");
		opcodeMap.put("sl",   "00101");
		opcodeMap.put("sr",   "00110");
		opcodeMap.put("return", "10001");
	}
	
	public String parseAtype(String[] inst) {
		String opcode = opcodeMap.get(inst[0]);
		String memAddr = inst[1];
		if (memAddr.equals("sp")) {
			memAddr = Parser.SP_ADDR;
		} else if (memAddr.equals("banksel")) {
			memAddr = Parser.BANKSELADDR;
		} else if (memAddr.equals("cause")) {
			memAddr = Parser.CAUSEADDR;
		} else {
			memAddr = Integer.toBinaryString(Integer.valueOf(memAddr));
			int zeros = 10 - memAddr.length();
			for (int i = 0; i < zeros; i++) {
				memAddr = "0" + memAddr;
			}
		}
		
		String machineCode = opcode + memAddr + inst[2];
		
		if(inst[0] == "slt") {
			return opcode + memAddr + "0";
		}
		return machineCode;
	}
	
	public String parseItype(String[] inst) {
		String opcode = opcodeMap.get(inst[0]);
		String immediate = Integer.toBinaryString(Integer.valueOf(inst[1]));
		
		int zeros = 11 - immediate.length();
		if (zeros < 0) {
			immediate = immediate.substring(Math.abs(zeros)); 
		} else {
			for (int i = 0; i < zeros; i++) {
				immediate = "0" + immediate;
			}
		}
		
		String machineCode = opcode + immediate;
		return machineCode;
	}
	
	public String parseJtype(String[] inst) {
		String opcode = opcodeMap.get(inst[0]);
		Integer addr = labelMap.containsKey(inst[1]) ? labelMap.get(inst[1]) : Integer.valueOf(inst[1]);
		String address = Integer.toBinaryString(Integer.valueOf(addr));
		int zeros = 11 - address.length();
		
		for (int i = 0; i < zeros; i++) {
			address = "0" + address;
		}
		
		String machineCode = opcode + address;
		return machineCode;
	}
	
	public String parseOtype(String[] inst) {
		String opcode = opcodeMap.get(inst[0]);
		return opcode + "00000000000";
	}
	
}
