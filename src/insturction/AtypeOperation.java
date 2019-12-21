package insturction;

import java.util.Stack;

import hardware.Memory;
import hardware.RegisterFile;

public class AtypeOperation {
	public Stack<InstructionState> backUps;
	
	private Memory memory = Memory.getInstance();
	private RegisterFile regFile = RegisterFile.getInstance();
	
	public void add(int addr, int ptr) {
		int rs = 0;
		int b = regFile.rw;
		int a = (int) memory.dataMem.get(addr);
		rs = a + b;
		
		if (ptr == 0) {
			regFile.rw = rs;
		} else {
			int addr_ptr = (int) memory.dataMem.get(addr);
			regFile.rw = rs;
			
			backUps.peek().addr = addr_ptr;
			backUps.peek().bank = regFile.BankSel;
			backUps.peek().nextBank = 1;
			backUps.peek().memWrite = 1;
			backUps.peek().memData = memory.dataMem.get(addr_ptr);
			
			regFile.BankSel = 1;
			memory.dataMem.set(addr_ptr, rs);
			memory.index = addr_ptr;
			memory.memWrite = 1;
		}
		regFile.PC += 1;
	}
	
	public void sub(int addr, int ptr) {
		int rs = 0;
		int b = regFile.rw;
		int a = (int) memory.dataMem.get(addr);
		rs = a - b;
		
		if (ptr == 0) {
			regFile.rw = rs;
		} else {
			int addr_ptr = (int) memory.dataMem.get(addr);
			regFile.rw = rs;
			
			backUps.peek().addr = addr_ptr;
			backUps.peek().bank = regFile.BankSel;
			backUps.peek().nextBank = 1;
			backUps.peek().memWrite = 1;
			backUps.peek().memData = memory.dataMem.get(addr_ptr);
			
			regFile.BankSel = 1;
			memory.dataMem.set(addr_ptr, rs);
			memory.index = addr_ptr;
			memory.memWrite = 1;
		}
		regFile.PC += 1;
	}
	
	public void and(int addr, int ptr) {
		int rs = 0;
		int b = regFile.rw;
		int a = (int) memory.dataMem.get(addr);
		rs = a & b;
		
		if (ptr == 0) {
			regFile.rw = rs;
		} else {
			int addr_ptr = (int) memory.dataMem.get(addr);
			regFile.rw = rs;
			
			backUps.peek().addr = addr_ptr;
			backUps.peek().bank = regFile.BankSel;
			backUps.peek().nextBank = 1;
			backUps.peek().memWrite = 1;
			backUps.peek().memData = memory.dataMem.get(addr_ptr);
			
			regFile.BankSel = 1;
			memory.dataMem.set(addr_ptr, rs);
			memory.index = addr_ptr;
			memory.memWrite = 1;
		}
		regFile.PC += 1;
	}
	
	public void or(int addr, int ptr) {
		int rs = 0;
		int b = regFile.rw;
		int a = (int) memory.dataMem.get(addr);
		rs = a | b;
		
		if (ptr == 0) {
			regFile.rw = rs;
		} else {
			int addr_ptr = (int) memory.dataMem.get(addr);
			regFile.rw = rs;
			
			backUps.peek().addr = addr_ptr;
			backUps.peek().bank = regFile.BankSel;
			backUps.peek().nextBank = 1;
			backUps.peek().memWrite = 1;
			backUps.peek().memData = memory.dataMem.get(addr_ptr);
			
			regFile.BankSel = 1;
			memory.dataMem.set(addr_ptr, rs);
			memory.index = addr_ptr;
			memory.memWrite = 1;
		}
		regFile.PC += 1;
	}
	
	public void slt(int addr, int ptr) {
		int rs = 0;
		int b = regFile.rw;
		int a = (int) memory.dataMem.get(addr);
		if (b < a) rs = 1;
		regFile.rw = rs;
		regFile.PC += 1;
	}
}
