package hardware;

import java.util.ArrayList;
import java.util.List;

public class Memory {
	public List<Object> instMem = new ArrayList<>(256);
	public List<Object> dataMem = new ArrayList<>(256);
	public List<Object> spMem = new ArrayList<>(256);
	public List<Object> pspMem = new ArrayList<>(256);
	
	public int index = 0;
	public int memWrite = 0;
	
	private static Memory memory;
	public static Memory getInstance() { 
        if (memory == null) 
        	memory = new Memory(); 
        return memory; 
    } 
	
	private Memory() {
		clear();
	}
	
	public List<Object> getMemory(int bank) {
		if (bank == 0) {
			return instMem;
		} else if (bank == 1) {
			return dataMem;
		} else if (bank == 2) {
			return spMem;
		} else if (bank == 3) {
			return pspMem;
		}
		return null;
	}
	
	public void clear() {
		memWrite = 0;
		index = 0;
		for(int i = 0; i < 256; i++) {
			dataMem.add(0);
			instMem.add(0);
			spMem.add(0);
			pspMem.add(0);
		}
		
		pspMem.set(224, 0);
	}
}


