package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Parser;

class TestJtypeParse {
	Parser parser;

	@BeforeEach
	void setUp() throws Exception {
		parser = new Parser();
	}
	
	@Test
	void testJtypeInstruction() {
		parser.labelMap.put("GCD", 62);
		String instruction = "jp GCD";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseJtype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("0101100000011111"));
	}
}
