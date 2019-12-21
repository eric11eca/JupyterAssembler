package insturction;

import hardware.RegisterFile;

public class ItypeOperation {
	private RegisterFile regFile = RegisterFile.getInstance(); 
	
	public void loadi(int a) {
		regFile.rw = a;
		regFile.PC += 1;
	}
	
	public void ori(int a) {
		regFile.rw = a | regFile.rw;
		regFile.PC += 1;
	}
	
	public void andi(int a) {
		regFile.rw = a & regFile.rw;
		regFile.PC += 1;
	}
	
	public void addi(int a) {
		regFile.rw += a;
		regFile.PC += 1;
	}

	public void lui(int a) {
		String bits = Integer.toBinaryString(a);
		int zeros = 11 - bits.length();
		if (zeros < 0) {
			bits = bits.substring(Math.abs(zeros)); 
		} else {
			for (int i = 0; i < zeros; i++) {
				bits = "0" + bits;
			}
		}

		String msb = bits.substring(6,11);
		msb += "000000";

		regFile.rw = Integer.parseInt(msb, 2);
		regFile.PC += 1;
	}
}
