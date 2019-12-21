package Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Parser;

class TestAtypeParse {
	Parser parser;
	
	@BeforeEach
	void setUp() throws Exception {
		parser = new Parser();
	}

	@Test
	void testAddNoPointer() {
		String instruction = "add 4 0";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseAtype(inst);
		assertTrue(mcode.equals("0000000000001000"));
	}
	
	@Test
	void testAddPointer() {
		String instruction = "add 4 1";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseAtype(inst);
		assertTrue(mcode.equals("0000000000001001"));
	}
	
	@Test
	void testAtypeInstructionWithSP0() {
		String instruction = "add sp 0";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseAtype(inst);
		assertTrue(mcode.equals("0000000000000010"));
	}
	
	@Test
	void testAtypeInstructionWithSP1() {
		String instruction = "add sp 1";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseAtype(inst);
		assertTrue(mcode.equals("0000000000000011"));
	}
	
	@Test
	void testAtypeInstructionWithBank0() {
		String instruction = "add BankSel 0";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseAtype(inst);
		assertTrue(mcode.equals("0000000000000000"));
	}
}
