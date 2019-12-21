package insturction;

import java.util.Map;
import java.util.Stack;

import hardware.Memory;
import hardware.RegisterFile;

public class JtypeOperation {
	private Memory memory = Memory.getInstance();
	private RegisterFile regFile = RegisterFile.getInstance(); 
	public Stack<InstructionState> backUps;
	
	public void bz(String label, Map<String, Integer> labels) {
		if (regFile.rw == 0) {
			int newPC = labels.get(label);
			regFile.PC = newPC;
		} else {
			regFile.PC += 1;
		}
	}
	
	public void bnz(String label, Map<String, Integer> labels) {
		if (regFile.rw != 0) {
			int newPC = labels.get(label);
			regFile.PC = newPC;
		} else {
			regFile.PC += 1;
		}
	}
	
	public void j(String label, Map<String, Integer> labels) {
		int newPC = labels.get(label);
		regFile.PC = newPC;
	}
	
	public void jp(String label, Map<String, Integer> labels) {
		memory.pspMem.set(regFile.PSP, regFile.PC);
		regFile.PC = labels.get(label);
		regFile.PSP += 1;
	}
}
