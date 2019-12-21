package insturction;

import java.util.Stack;

import hardware.Memory;
import hardware.RegisterFile;

public class MtypeOperation {
	private Memory memory = Memory.getInstance();
	private RegisterFile regFile = RegisterFile.getInstance();
	public Stack<InstructionState> backUps;
	
	public void store(int addr, int ptr) {
		if (addr == 0) {
			regFile.BankSel = regFile.rw;
		} else if (addr == 1) {
			if (ptr == 0) {
				regFile.sp = regFile.rw;	
			} else {
				backUps.peek().addr = regFile.sp;
				backUps.peek().bank = regFile.BankSel;
				backUps.peek().memData = memory.spMem.get(addr);
				backUps.peek().nextBank = 2;
				backUps.peek().memWrite = 1;

				regFile.BankSel = 2;
				memory.memWrite = 1;
				memory.index = regFile.sp;
				memory.spMem.set(regFile.sp, regFile.rw);
			}
		} else {
			if (ptr == 0) {
				backUps.peek().addr = addr;
				backUps.peek().bank = regFile.BankSel;
				backUps.peek().nextBank = 1;
				backUps.peek().memWrite = 1;
				backUps.peek().memData = memory.dataMem.get(addr);
				
				regFile.BankSel = 1;
				memory.dataMem.set(addr, regFile.rw);
				memory.index = addr;
				memory.memWrite = 1;
			} else {
				int addr_ptr = (Integer) memory.dataMem.get(addr);
				backUps.peek().addr = addr;
				backUps.peek().bank = regFile.BankSel;
				backUps.peek().nextBank = 1;
				backUps.peek().memWrite = 1;
				backUps.peek().memData = memory.dataMem.get(addr_ptr);
				
				regFile.BankSel = 1;
				memory.dataMem.set(addr_ptr, regFile.rw);
				memory.index = addr_ptr;
				memory.memWrite = 1;
			}
		}
		regFile.PC += 1;
	}
	
	public void load(int addr, int ptr) {
		if (addr == 0) {
			regFile.rw = regFile.BankSel;
		} else if (addr == 1) { 
			regFile.rw = regFile.Cause;
		} else if (addr == 1) {
			if (ptr == 0) {
				regFile.rw = regFile.sp;
			} else {
				regFile.rw = (Integer) memory.dataMem.get(regFile.sp);
			}
		} else {
			if (ptr == 0) {
				regFile.rw = (Integer) memory.dataMem.get(addr);
			} else {
				int addr_ptr = (Integer) memory.dataMem.get(addr);
				regFile.rw = (Integer) memory.dataMem.get(addr_ptr);
			}
		}	
		regFile.PC += 1;
	}
}
