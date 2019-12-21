package hardware;

public class RegisterFile {
	private static RegisterFile file;
	
	private RegisterFile() {
		rw = 0;
		PC = 0;
		sp = 0;
		PSP = 0;
		BankSel = 0;
		Cause = 0;
	}
	
	public static RegisterFile getInstance() { 
        if (file == null) 
        	file = new RegisterFile(); 
        file.reset();
        return file; 
    } 
	
	public Integer rw;
	public Integer PC;
	public Integer sp;
	public Integer PSP;
	public Integer BankSel;
	public Integer Cause;
	
	public void reset() {
		rw = 0;
		PC = 0;
		sp = 0;
		PSP = 0;
		BankSel = 0;
		Cause = 0;
	}
}
