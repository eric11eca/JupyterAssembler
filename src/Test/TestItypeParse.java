package Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Parser;

class TestItypeParse {
	Parser parser;

	@BeforeEach
	void setUp() throws Exception {
		parser = new Parser();
	}
	
	@Test
	void testAddiPositive1() {
		String instruction = "addi 1";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1000000000000001"));
	}
	
	@Test
	void testAddiPositive10() {
		String instruction = "addi 10";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1000000000001010"));
	}

	@Test
	void testAddiNegtive1() {
		String instruction = "addi -1";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1000011111111111"));
	}
	
	@Test
	void testAddiNegtive10() {
		String instruction = "addi -10";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1000011111110110"));
	}
	
	@Test
	void testOriPositive10() {
		String instruction = "ori 10";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1010000000001010"));
	}
	
	@Test
	void testOriNegtive10() {
		String instruction = "ori -10";
		String[] inst = instruction.split("\\s+");
		String mcode = parser.parseItype(inst);
		//System.out.println(mcode);
		assertTrue(mcode.equals("1010011111110110"));
	}
}
