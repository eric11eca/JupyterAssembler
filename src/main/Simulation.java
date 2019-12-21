package main;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import hardware.Memory;
import hardware.RegisterFile;
import insturction.AtypeOperation;
import insturction.InstructionState;
import insturction.ItypeOperation;
import insturction.JtypeOperation;
import insturction.MtypeOperation;
import insturction.OtypeOperation;

public class Simulation {	
	public List<String> addresses;
	public List<String> instructions;
	public Map<String, Integer> labels;
	public  Stack<InstructionState> backUps;
	public boolean finished = false;

	private AtypeOperation atypeOperation;
	private MtypeOperation mtypeOperation;
	private ItypeOperation itypeOperation;
	private JtypeOperation jtypeOperation;
	private OtypeOperation otypeOperation;
	
	private RegisterFile regFile = RegisterFile.getInstance();
	private Memory memory = Memory.getInstance();
	
	public Simulation() {
		atypeOperation = new AtypeOperation();
		mtypeOperation = new MtypeOperation();
		itypeOperation = new ItypeOperation();
		jtypeOperation = new JtypeOperation();
		otypeOperation = new OtypeOperation();
		backUps = new Stack<>();
	}
	
	public void injectBackUps() {
		atypeOperation.backUps = backUps;
		mtypeOperation.backUps = backUps;
	}
	
	public void simulateForward() {
		injectBackUps();
		regFile.BankSel = 0;
		String instruction = instructions.get(regFile.PC);
		if (instruction.equals("syscall")) {
			finished = true;
			return;
		}
		
		memory.memWrite = 0;
		
		InstructionState state = new InstructionState();
		state.rw = regFile.rw;
		state.pc = regFile.PC;
		state.psp = regFile.PSP;
		state.sp = regFile.sp;
		state.memWrite = 0;
		backUps.push(state);
		
		String[] inst = instruction.split("\\s+");
		String instructionType = Assemble.getInsturctionType(inst[0]);

		if (instructionType == "A") {
			if (inst[0].equals("add")) {
				atypeOperation.add(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
			} else if (inst[0].equals("sub")) {
				atypeOperation.sub(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
			} else if (inst[0].equals("and")) {
				atypeOperation.and(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
			} else if (inst[0].equals("or")) {
				atypeOperation.or(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
			} else if (inst[0].equals("slt")) {
				atypeOperation.slt(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
			}
		} else if (instructionType == "M") {
			if (inst[0].equals("load")) {
				if(inst[1].equals("sp")) {
					mtypeOperation.load(1, Integer.valueOf(inst[2]));
				} else if(inst[1].equals("banksel")) {
					mtypeOperation.load(0, Integer.valueOf(inst[2]));
				} else if (inst[1].equals("cause")) {
					mtypeOperation.load(1024, Integer.valueOf(inst[2]));
				} else {
					mtypeOperation.load(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
				}
			} else if (inst[0].equals("store")) {
				if(inst[1].equals("sp")) {
					mtypeOperation.store(1, Integer.valueOf(inst[2]));
				} else if(inst[1].equals("banksel")) {
					mtypeOperation.store(0, Integer.valueOf(inst[2]));
				} else {
					mtypeOperation.store(Integer.valueOf(inst[1]), Integer.valueOf(inst[2]));
				}
			}
		} else if (instructionType == "I") {
			if (inst[0].equals("loadi")) {
				itypeOperation.loadi(Integer.valueOf(inst[1]));
			} else if (inst[0].equals("addi")) {
				itypeOperation.addi(Integer.valueOf(inst[1]));
			} else if (inst[0].equals("ori")) {
				itypeOperation.ori(Integer.valueOf(inst[1]));
			} else if (inst[0].equals("andi")) {
				itypeOperation.andi(Integer.valueOf(inst[1]));
			} else if (inst[0].equals("lui")) {
				itypeOperation.lui(Integer.valueOf(inst[1]));
			}
		} else if (instructionType == "J") {
			if (inst[0].equals("bz")) {
				jtypeOperation.bz(inst[1], labels);
			} else if (inst[0].equals("bnz")) {
				jtypeOperation.bnz(inst[1], labels);
			} else if (inst[0].equals("j")) {
				jtypeOperation.j(inst[1], labels);
			} else if (inst[0].equals("jp")) {
				jtypeOperation.jp(inst[1], labels);
			}
		} else if (instructionType == "O") {
			if (inst[0].equals("sl")) {
				otypeOperation.sl();
			} else if (inst[0].equals("sr")) {
				otypeOperation.sr();
			} else if (inst[0].equals("return")) {
				otypeOperation.returnp();
			}
		}
		backUps.peek().memWrite = memory.memWrite;
	}
}
