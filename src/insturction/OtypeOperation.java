package insturction;

import hardware.Memory;
import hardware.RegisterFile;

public class OtypeOperation {
	private Memory memory = Memory.getInstance();
	private RegisterFile regFile = RegisterFile.getInstance();
	
	public void sl() {
		regFile.rw *= 2;
		regFile.PC += 1;
	}
	
	public void sr() {
		regFile.rw /= 2;
		regFile.PC += 1;
	}
	
	public void returnp() {
		regFile.PSP -= 1;
		regFile.PC = (Integer) memory.pspMem.get(regFile.PSP) + 1;
	}
}
