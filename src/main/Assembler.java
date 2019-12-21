package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import hardware.Memory;
import hardware.RegisterFile;
import insturction.InstructionState; 

@SuppressWarnings("serial")
public class Assembler extends JFrame implements ActionListener {  
	private JSplitPane mainSplitPane;
	private JSplitPane splitPaneText;
	private JSplitPane splitCrud;
	
	private JToolBar toolBar;
    
	private RSyntaxTextArea instArea;
	private RSyntaxTextArea mcodeArea;
    
	private JTable codeTable;
	private JTable labelTable;
	private DefaultTableModel codeModel;
	private DefaultTableModel labelModel;
	
	private JTable memTable;
	private JTable regTable;
	private DefaultTableModel memModel;
	private DefaultTableModel regModel;
	
	private JTextArea console;
	private JTabbedPane executePane;
	private JTabbedPane regLabelPane;
    
    private Assemble assemble;
    private Simulation simulation;
    
    private String[] codeColumns;
    private String[] labelColumns;
    private String[] memColumns;
    private String[] regColumns;
    
    private Object[][] codeData;
    private Object[][] labelData;
    private Object[][] memData;
    private Object[][] regData;
    
    private Class<?>[] codeColumnClass;
    private Class<?>[] labelColumnClass;
    private Class<?>[] regColumnClass;
    private Class<?>[] memColumnClass;
    
    private JButton AssembleCode;
    private JButton Run;
    private JButton Step;
    private JButton Pause;
    private JButton Stop;
    private JButton Reset;
    private JButton Backstep;
    private JButton Save;
    private JButton SaveAs;
    private JButton SaveCoe;
    private JButton SaveCoeAs;
    private JButton Open;
    private JButton New;
    
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Memory memory = Memory.getInstance();
	private RegisterFile regFile = RegisterFile.getInstance();
    
    private final String lineBreak = "-----------------------------";
    private final String radix = "MEMORY_INITIALIZATION_RADIX=2;\n";
    private final String  headr = "MEMORY_INITIALIZATION_VECTOR=\n";
    
    private boolean color = false;
    private String currentFilePath = "";
    private String currentCoePath = "";
    private Rectangle cellReact;
    
    public Assembler() { 
    	assemble = new Assemble();
    	simulation = new Simulation();
    	
        instArea = new RSyntaxTextArea(20, 60); 
        mcodeArea = new RSyntaxTextArea(20, 60);
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("images/my", "main.TokenMaker");
        instArea.setSyntaxEditingStyle("images/my");
        instArea.setCodeFoldingEnabled(true);
        
        initTableSegments();

        RTextScrollPane instTextPane = new RTextScrollPane(instArea);
        RTextScrollPane mcodeTextPane = new RTextScrollPane(mcodeArea);
        JScrollPane codeTablePane = new JScrollPane(codeTable);
        JScrollPane memTablePane = new JScrollPane(memTable);
        
        JSplitPane codememSplit = new JSplitPane(SwingConstants.HORIZONTAL, codeTablePane, memTablePane);
        codememSplit.setDividerLocation(screenSize.height/4-20);
        
        JScrollPane labelTablePane = new JScrollPane(labelTable);
        JScrollPane regTablePane = new JScrollPane(regTable);
        regLabelPane = new JTabbedPane();
        regLabelPane.addTab("Register", regTablePane);
        regLabelPane.addTab("Label", labelTablePane);
        
        splitCrud = new JSplitPane(SwingConstants.VERTICAL, codememSplit, regLabelPane);
        splitCrud.setOneTouchExpandable(true);
        splitCrud.setDividerLocation(1000);
        
        splitPaneText = new JSplitPane(SwingConstants.VERTICAL, instTextPane, mcodeTextPane);
        splitPaneText.setOneTouchExpandable(true);
		splitPaneText.setDividerLocation(1000);
		
		console = new JTextArea(200,200);
		Font font = new Font("Consolas", Font.BOLD, 12);
		console.setFont(font);
		console.setLineWrap(true);

		executePane = new JTabbedPane(JTabbedPane.TOP); 
        executePane.addTab("Execute", splitCrud);
        executePane.addTab("Console", console); 
        
        mainSplitPane = new JSplitPane(SwingConstants.HORIZONTAL, splitPaneText, executePane);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setDividerLocation(screenSize.height/2 - 132);
        
        try { 
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
        } 
        catch (Exception e) { 
        } 
       
        JMenuBar mb = new JMenuBar(); 
        
        JMenu m1 = new JMenu("File"); 
        JMenuItem mi1 = new JMenuItem("New"); 
        JMenuItem mi2 = new JMenuItem("Open"); 
        JMenuItem mi3 = new JMenuItem("Save"); 
        JMenuItem mi7 = new JMenuItem("Save COE");
        JMenuItem mi8 = new JMenuItem("Save As");
        JMenuItem mi9 = new JMenuItem("Save COE As");
        mi1.addActionListener(this); 
        mi2.addActionListener(this); 
        mi3.addActionListener(this); 
        mi7.addActionListener(this); 
        mi8.addActionListener(this); 
        mi9.addActionListener(this); 
        m1.add(mi1); 
        m1.add(mi2); 
        m1.add(mi3);
        m1.add(mi7);
        m1.add(mi8);
        m1.add(mi9);
  
        JMenu m2 = new JMenu("Edit"); 
        JMenuItem mi4 = new JMenuItem("cut"); 
        JMenuItem mi5 = new JMenuItem("copy"); 
        JMenuItem mi6 = new JMenuItem("paste"); 
        mi4.addActionListener(this); 
        mi5.addActionListener(this); 
        mi6.addActionListener(this); 
        m2.add(mi4); 
        m2.add(mi5); 
        m2.add(mi6); 
  
        JMenu m3 = new JMenu("Run"); 
        JMenuItem assemb = new JMenuItem("Assemble"); 
        JMenuItem simulate = new JMenuItem("Simulate");
        JMenuItem syntax = new JMenuItem("Check Syntax");
        JCheckBoxMenuItem exception = new JCheckBoxMenuItem("Attach Exception Handler", false);
        assemb.addActionListener(this);
        simulate.addActionListener(this);
        syntax.addActionListener(this);
        exception.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               if(exception.getState()){
            	   assemble.useExceptionHandler = true;
               } else {
            	   assemble.useExceptionHandler = false;
               }
            }
         });
        m3.add(assemb);
        m3.add(syntax);
        m3.add(simulate);
        m3.add(exception);
        
        mb.add(m1); 
        mb.add(m2); 
        mb.add(m3);
  
        setJMenuBar(mb); 
        setUpToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        add(mainSplitPane); 
        
        setSize(screenSize.width, screenSize.height);  
        setTitle("Jupyter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

	private void initTableSegments() {
		initColumnHeaders();
		initColumnClasses();
		
		regData = new Object[][] {
    		new Object[]{"$rw", "0"},
    		new Object[]{"$sp", "0"},
    		new Object[]{"$psp", "0"},
    		new Object[]{"$pc", "0"},
    		new Object[]{"$BankSel", "0"}
    	};
    	
        codeModel = tableModelFactory(codeColumns, codeColumnClass, codeData, true);
        labelModel = tableModelFactory(labelColumns, labelColumnClass, labelData, false);
        codeModel.addTableModelListener(new TableModelListener() {
        	@Override public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
			    int column = e.getColumn();
			    if (column == 0) {
			        TableModel model = (TableModel) e.getSource();
			        Boolean checked = (Boolean) model.getValueAt(row, column);
			        if (checked) {
			        	assemble.breakPoints.add(row);
			        } else {
			        	assemble.breakPoints.remove(Integer.valueOf(row));
			        }
			    }
			}
		});
        memModel = tableModelFactory(memColumns, memColumnClass, memData, false);
        regModel = tableModelFactory(regColumns, regColumnClass, regData, false);
             
        codeTable = new JTable(codeModel) {
        	@Override public boolean isCellEditable(int row, int col) {
        	    if (col < 1) {
        	        return true;
        	    } else {
        	        return false;
        	    }
        	}
        }; 
        labelTable = new JTable(labelModel);
        memTable = new JTable(memModel) {
        	@Override public boolean isCellSelected(int r, int c) {
        		int row = memory.index;
        		int col = regFile.BankSel*2+1;
                if ((r == row && row != 0) && (c == col && col != 0)) {
                	this.setSelectionBackground(Color.YELLOW);
                	return color;
                }
                return false;
            }
        };
        regTable = new JTable(regModel);
	}

	private void initColumnClasses() {
		codeColumnClass = new Class[] {
				Boolean.class, String.class, String.class, String.class, String.class};
		labelColumnClass = new Class[] {String.class, String.class};
		regColumnClass = new Class[] {String.class, String.class};
		memColumnClass = new Class[] {
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class};
	}

	private void initColumnHeaders() {
		codeColumns = new String[] {"","Address", "Machine Code", "Code", "Comments"};
        labelColumns = new String[] {"Label", "address"};
        memColumns = new String[] {"Inst Address", "Inst", "Data Address", 
        		"Data", "SP Address", "SP", "PSP Address", "PSP"};
        regColumns = new String[] {"Register", "Value"};
	}

	private DefaultTableModel tableModelFactory(String[] columns, Class<?>[] columnClass, Object[][] data, Boolean code) {
		DefaultTableModel model;
		if (code) {
			model = new DefaultTableModel(data, columns) {
				@Override
	            public boolean isCellEditable(int row, int column){
	        		return false;
	        	}
				
	            @Override
	            public Class<?> getColumnClass(int column) {
	                return column == 0 ? Boolean.class : String.class;
	            }
	        };
		} else {
			model = new DefaultTableModel(data, columns) {
				@Override
	            public boolean isCellEditable(int row, int column){
	        		return false;
	        	}
				
	            @Override
	            public Class<?> getColumnClass(int columnIndex){
	            	return columnClass[columnIndex];
	            }
	        };
		}
        
        return model;
	} 
    
    private void setUpToolBar() {
        toolBar = new JToolBar();
        
        initOpenButton();       
        initNewButton();
        initSaveButton();
        initSaveAsButton();
        initSaveCoeButton();
        initSaveCoeAsButton();
        initRunButton();
        initAssembleButton();
        initStepButton();    
        initBackStepButton();
        initResetButton();
        
        Stop = new JButton();
        Stop.setEnabled(false);
        try {
            Image img = ImageIO.read(getClass().getResource("/Stop22.png"));
            Stop.setIcon(new ImageIcon(img));
        } catch (Exception ex) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
            System.out.println("can't find image");
        }
        
        Pause = new JButton();
        Pause.setEnabled(true);
        try {
            Image img = ImageIO.read(getClass().getResource("/Pause22.png"));
            Pause.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        Pause.addActionListener(e-> {
        	System.out.print(assemble.breakPoints);
        	System.out.println();
        });

		toolBar.add(Open);
		toolBar.add(New);
		toolBar.add(new JToolBar.Separator());
		toolBar.add(Save);
		toolBar.add(SaveAs);
		toolBar.add(SaveCoe);
		toolBar.add(SaveCoeAs);
		toolBar.add(new JToolBar.Separator());
        toolBar.add(AssembleCode);
        toolBar.add(Run);
        toolBar.add(Step);
        toolBar.add(Backstep);
        toolBar.add(new JToolBar.Separator());
        //toolBar.add(Pause);
        //toolBar.add(Stop);
        toolBar.add(Reset);
        toolBar.add(new JToolBar.Separator());
    }

	private void initResetButton() {
		Reset = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Reset22.png"));
            Reset.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        Reset.setEnabled(false); 
		Reset.addActionListener(e->{
			resetSimulation();
		});
		Reset.createToolTip();
		Reset.setToolTipText("Reset Programe");
	}

	private void initBackStepButton() {
		Backstep = new JButton();
        Backstep.setEnabled(false);
        try {
            Image img = ImageIO.read(getClass().getResource("/StepBack22.png"));
            Backstep.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        Backstep.addActionListener(e->{
        	backStepAction();
        });
        Backstep.createToolTip();
        Backstep.setToolTipText("Step Backward");
	}

	private void initStepButton() {
		Step = new JButton();
        Step.setEnabled(false);
        try {
            Image img = ImageIO.read(getClass().getResource("/StepForward22.png"));
            Step.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        Step.addActionListener(e->{
        	stepForwardAction();
        });
        Step.createToolTip();
        Step.setToolTipText("Step Forward");
	}

	private void initAssembleButton() {
		AssembleCode = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Assemble22.png"));
            AssembleCode.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        AssembleCode.addActionListener(this);
        AssembleCode.setActionCommand("Assemble");
        AssembleCode.createToolTip();
        AssembleCode.setToolTipText("Assemble Programe");
	}

	private void initRunButton() {
		Run = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Play22.png"));
            Run.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println("can't find image");
        }
        Run.setEnabled(false);
        Run.addActionListener(e->{
        	runAction();
        });
        Run.createToolTip();
        Run.setToolTipText("Run Programe");
	}

	private void initSaveCoeAsButton() {
		SaveCoeAs = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/SaveAs16.png"));
            SaveCoeAs.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        SaveCoeAs.setEnabled(true);
        SaveCoeAs.createToolTip();
        SaveCoeAs.setToolTipText("Save as new COE");
        SaveCoeAs.addActionListener(this);
        SaveCoeAs.setActionCommand("Save COE As");
	}

	private void initSaveCoeButton() {
		SaveCoe = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Save16.png"));
            SaveCoe.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        SaveCoe.setEnabled(true);
        SaveCoe.createToolTip();
        SaveCoe.setToolTipText("Save current COE");
        SaveCoe.addActionListener(this);
        SaveCoe.setActionCommand("Save COE");
	}

	private void initSaveAsButton() {
		SaveAs = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/SaveAs22.png"));
            SaveAs.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        SaveAs.setEnabled(true);
        SaveAs.createToolTip();
        SaveAs.setToolTipText("Save as new");
        SaveAs.addActionListener(this);
        SaveAs.setActionCommand("Save As");
	}

	private void initSaveButton() {
		Save = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Save22.png"));
            Save.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        Save.setEnabled(true);
        Save.createToolTip();
        Save.setToolTipText("Save current");
        Save.addActionListener(this);
        Save.setActionCommand("Save");
	}

	private void initNewButton() {
		New = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/New22.png"));
            New.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        New.setEnabled(true);
        New.createToolTip();
        New.setToolTipText("New Edit");
        New.addActionListener(this);
        New.setActionCommand("New");
	}

	private void initOpenButton() {
		Open = new JButton();
        try {
            Image img = ImageIO.read(getClass().getResource("/Open22.png"));
            Open.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
        	System.out.println("can't find image");
        }
        Open.setEnabled(true);
        Open.createToolTip();
        Open.setToolTipText("Open File");
        Open.addActionListener(this);
        Open.setActionCommand("Open");
	}

	private void runAction() {
		while(!simulation.finished) {
			if(assemble.breakPoints.contains(regFile.PC)) {
				break;
			}
			simulation.simulateForward();
		}
		
		loadFullToMemTable();
		updateRegTable();
		
		codeTable.setRowSelectionInterval(regFile.PC, regFile.PC);
		cellReact = codeTable.getCellRect(regFile.PC, 0, true);
		codeTable.scrollRectToVisible(cellReact);
		
		if(assemble.breakPoints.contains(regFile.PC)) {
			return;
		}
		
		Step.setEnabled(false);
		Run.setEnabled(false);
		Backstep.setEnabled(true);
	}

	private void stepForwardAction() {
		if(assemble.breakPoints.contains(regFile.PC)) {
			return;
		}
		simulation.simulateForward();
		updateRegTable();
		
		if (memory.memWrite == 1) {
			color = true;
			updateMemTable(false, 0);
			memory.memWrite = 0;
		}
		
		codeTable.setRowSelectionInterval(regFile.PC, regFile.PC);
		cellReact = codeTable.getCellRect(regFile.PC, 0, true);
		codeTable.scrollRectToVisible(cellReact);
		
		if (simulation.finished) {
			Step.setEnabled(false);
			Run.setEnabled(false);
			color = false;
		}
		
		Backstep.setEnabled(true);
	}

	private void backStepAction() {
		Step.setEnabled(true);
		if (simulation.backUps.isEmpty()) {
			Backstep.setEnabled(false);
			return;
		}
		InstructionState prev = simulation.backUps.pop();
		regFile.rw = prev.rw;
		regFile.PC = prev.pc;
		regFile.PSP = prev.psp;
		regFile.sp = prev.sp;
		regFile.BankSel = prev.bank;
		int next_bank = prev.nextBank;
		
		if (prev.memWrite == 1) {
			memory.index = prev.addr;
			memory.getMemory(next_bank).set(prev.addr, prev.memData);
			updateMemTable(true, next_bank);
		}
		
		updateRegTable();
		color = false;	
		codeTable.setRowSelectionInterval(regFile.PC, regFile.PC);   
		cellReact = codeTable.getCellRect(regFile.PC, 0, true);
		codeTable.scrollRectToVisible(cellReact);
		
		if(simulation.finished) simulation.finished = false;
	}

	private void resetMemTable() {
		memData = new Object[256][8];
		int index = 0;
		for(int i = 0; i < 256; i++) {
			String inst = assemble.machineCodes.size() > i ? assemble.machineCodes.get(i) : "0000000000000000";
			memData[index] = new Object[] {
					String.format("%s(%s)",index,index), inst, 
					String.format("%s(%s)",index,index+256), 0, 
					String.format("%s(%s)",index,index+512), 0, 
					String.format("%s(%s)",index,index+768), 0};
			if (!inst.equals("termination code")) {
				index++;
			}
		}
		
		if (index < 256) {
			memData[255] = new Object[] {
					String.format("%s(%s)",255,255), "0000000000000000", 
					String.format("%s(%s)",255,255+256), 0, 
					String.format("%s(%s)",255,255+512), 0, 
					String.format("%s(%s)",255,255+768), 0};
		}
		
		DefaultTableModel memModel = this.tableModelFactory(memColumns, memColumnClass, memData, false);
		memTable.setModel(memModel);
		memTable.clearSelection();
	}
    
    private void resetRegTable(){
    	regData = new Object[][] {
    		new Object[]{"$rw", "0"},
    		new Object[]{"$sp", "0"},
    		new Object[]{"$psp", "0"},
    		new Object[]{"$pc", "0"},
    		new Object[]{"$BankSel", "0"}
    	};
    	DefaultTableModel regModel = this.tableModelFactory(regColumns, regColumnClass, regData, false);
    	regTable.setModel(regModel);
    }

	private void updateRegTable() {
		DefaultTableModel regModel = (DefaultTableModel) regTable.getModel();
		regModel.setValueAt(regFile.rw, 0, 1);
		regModel.setValueAt(regFile.sp, 1, 1);
		regModel.setValueAt(regFile.PSP, 2, 1);
		regModel.setValueAt(regFile.PC, 3, 1);
		regModel.setValueAt(regFile.BankSel, 4, 1);
	}
	
	private void updateMemTable(boolean back, int nextBank) {
		DefaultTableModel memModel = (DefaultTableModel) memTable.getModel();
		int row = memory.index;
		int col = regFile.BankSel*2+1;
		if(back) {
			regFile.BankSel = nextBank;
			memModel.setValueAt(memory.getMemory(nextBank).get(row), row, nextBank*2+1);
		} else {
			memModel.setValueAt(memory.getMemory(regFile.BankSel).get(row), row, col);
		}
		//System.out.print("row: " + row + " col: " + col);
	}
	
	private void loadFullToMemTable() {
		DefaultTableModel memModel = (DefaultTableModel) memTable.getModel();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 256; j++) {
				if(i == 0) {
					String inst = assemble.machineCodes.size() > j ? assemble.machineCodes.get(j) : "0000000000000000";
					memModel.setValueAt(inst, j, 1);
				} else {
					List<Object> l = memory.getMemory(i);
					Object o = l.get(j);
					memModel.setValueAt(o, j, i*2+1);
				}
			}
		}
	}
  
    public void actionPerformed(ActionEvent e) { 
        String s = e.getActionCommand(); 
        if (s.equals("Save")) { 
        	saveFileAction(true); 
        } else if (s.equals("Save COE")) { 
        	saveFileAction(false); 
        } else if (s.equals("Save As")) { 
        	saveAsFileAction(true); 
        } else if (s.equals("Save COE As")) { 
        	saveAsFileAction(false); 
        } else if (s.equals("Check Syntax")) {
        	syntaxCheckAction();
        } else if (s.equals("Assemble")) { 
			assembleAction();
        } else if (s.equals("Simulate")) { 
        	resetSimulation();
        } else if (s.equals("Open")) { 
            openFileAction();
        } else if (s.equals("New")) { 
            instArea.setText("");
            mcodeArea.setText("");
        }
    }
    
	private void assembleAction() {		
		assemble.processFile(instArea.getText());
		assemble.assemble();
		mcodeArea.setText("");
		mcodeArea.setText(assemble.finalMCode);
		executePane.setSelectedComponent(splitCrud);
		
		DefaultTableModel codeModel = (DefaultTableModel) codeTable.getModel();
		codeModel.setRowCount(0);

		for (int i = 0; i < assemble.addresses.size(); i++) {
			String inst = assemble.instructions.get(i);
			String addr = assemble.addresses.get(i);
			String mcode = assemble.machineCodes.get(i);
			String comment = assemble.comments.get(i);
			if(inst.equals("syscall")) {
				mcode = "termination code";
			}
			Object[] codeInfo = new Object[] {false ,addr, mcode, inst, comment};
			codeModel.addRow(codeInfo);
		}	
		
		DefaultTableModel labelModel = (DefaultTableModel) labelTable.getModel();
		labelModel.setRowCount(0);
		
		for(String label : assemble.labels.keySet()) {
			int addr = assemble.labels.get(label);
			String addrHax = String.format("%05X", addr);
			String[] labelInfo = new String[] {label, addrHax};
			labelModel.addRow(labelInfo);
		}
		
		resetSimulation();
		Reset.setEnabled(true);
	}

	private void resetSimulation() {
		simulation.addresses = assemble.addresses;
		simulation.instructions = assemble.instructions;
		simulation.labels = assemble.labels;
		simulation.finished = false;
		
		resetMemTable();
    	resetRegTable();
    	regFile.reset();
    	memory.clear();
    	regTable.setRowSelectionInterval(0,0);
    	codeTable.setRowSelectionInterval(0,0);
    	Step.setEnabled(true);
		Run.setEnabled(true);
	}

	private void saveFileAction(boolean inst) {
		String path = inst ? currentFilePath : currentCoePath;
		if (!path.equals("")) { 
		    File fi = new File(path); 
		    try { 
		        FileWriter wr = new FileWriter(fi, false); 
		        BufferedWriter w = new BufferedWriter(wr); 

		        if(inst) w.write(instArea.getText());
		        else {
		        	w.write(radix);
			        w.write(headr);
			        w.write(mcodeArea.getText());
		        }
		        w.flush(); 
		        w.close(); 
		    } 
		    catch (Exception evt) { 
		        JOptionPane.showMessageDialog(this, evt.getMessage()); 
		    } 
		} else {
			saveAsFileAction(inst);
		}
	}
	
	private void saveAsFileAction(boolean inst) {
		JFileChooser j = new JFileChooser("f:"); 
		
		int r = j.showSaveDialog(null); 
		if (r == JFileChooser.APPROVE_OPTION) { 
		    File fi = new File(j.getSelectedFile().getAbsolutePath()); 
  
		    try { 
		        FileWriter wr = new FileWriter(fi, false); 
		        BufferedWriter w = new BufferedWriter(wr); 
		        
		        if(inst) w.write(instArea.getText());
		        else {
		        	w.write(radix);
			        w.write(headr);
			        w.write(mcodeArea.getText());
		        }
		        w.flush(); 
		        w.close(); 
		    } 
		    catch (Exception evt) { 
		        JOptionPane.showMessageDialog(this, evt.getMessage()); 
		    } 
		}
	}

	private void syntaxCheckAction() {
		executePane.setSelectedComponent(console);
		console.setText("");
		assemble.processFile(instArea.getText());
		Set<String[]> messages = assemble.checkSyntax();
		String errorFormat = "Errors occured at line %s:\n %s \n";
		if (!messages.isEmpty()) {
			String content = "";
			for (String[] msg : messages) {
				content += String.format(errorFormat, msg[0], msg[1]);
			}
			content += "\n\nSyntax Checking Failed! \n";
			console.setText(content);
		} else {
			console.setText("Syntax Check is Sucessful\n");
		}
	}

	private void openFileAction() {
		JFileChooser j = new JFileChooser("f:"); 
		int r = j.showOpenDialog(null); 
  
		if (r == JFileChooser.APPROVE_OPTION) { 
		    File fi = new File(j.getSelectedFile().getAbsolutePath()); 
		    try { 
		        String s1 = "", sl = ""; 
		        FileReader fr = new FileReader(fi); 

		        @SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(fr); 
		        sl = br.readLine(); 
		        while ((s1 = br.readLine()) != null) { 
		        	if (s1.contains(lineBreak)) break;
		        	sl = sl + "\n" + s1;
		        } 
		        instArea.setText(sl); 
		        currentFilePath = j.getSelectedFile().getAbsolutePath();
		    } catch (Exception evt) { 
		        JOptionPane.showMessageDialog(this, evt.getMessage()); 
		    } 
		}
	} 

    public static void main(String[] args) {
    	int splashDuration = 2000;
    	new JupiterSplashScreen(splashDuration).showSplash();
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
        	   Assembler a = new Assembler();
        	   a.setVisible(true);
        	   a.setExtendedState(a.getExtendedState() | JFrame.MAXIMIZED_BOTH);
           }
        });
     }
} 