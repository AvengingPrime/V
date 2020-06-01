import java.awt.*; 
import javax.swing.*; 
import java.io.*; 
import java.awt.event.*; 
import javax.swing.plaf.metal.*; 
import javax.swing.text.*; 

class GUI extends JFrame implements ActionListener
{
    JTextArea text;
    JFrame frame;
    V v;
    
    
    boolean closed;
    boolean run;
    
    GUI(V v)
    {
    	run = false;
        frame = new JFrame("editor");
        
        try
        {
//        	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        	
//        	MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        catch (Exception e)
        {
        	
        }
        
        this.v = v;
        
        text = new JTextArea();
        
        JMenuBar menubar = new JMenuBar();
        
        JMenu file = new JMenu("File");

        JMenuItem $new = new JMenuItem("New");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem print = new JMenuItem("Print");

        $new.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        print.addActionListener(this);
        
        file.add($new);
        file.add(open);
        file.add(save);
        file.add(print);
        
        JMenu edit = new JMenu("Edit");
        
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        
        JMenu code = new JMenu("Code");
        
        JMenuItem run = new JMenuItem("Run");
        
        run.addActionListener(this);
        
        code.add(run);
        
        menubar.add(file);
        menubar.add(edit);
        menubar.add(code);
        
        frame.setJMenuBar(menubar);
        frame.add(text);
        frame.setSize(500, 500);
        frame.show();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        
        if(command.equals("Cut"))
        {
            text.cut();
        }
        else if(command.equals("Copy"))
        {
            text.copy();
        }
        else if(command.equals("Paste"))
        {
            text.paste();
        }
        else if(command.equals("Save"))
        {
            this.save();
        }
        else if(command.equals("Print"))
        {
            try
            {
                text.print();
            }
            catch(Exception evt)
            {
                JOptionPane.showMessageDialog(frame, evt.getMessage());
            }
        }
        else if(command.equals("Open"))
        {
            JFileChooser chooser = new JFileChooser("f:");

            int num = chooser.showOpenDialog(null);
  
            if(num == JFileChooser.APPROVE_OPTION)
            { 
                File file = new File(chooser.getSelectedFile().getAbsolutePath()); 
  
                try { 
                    String str = "", str1 = "";
                    FileReader fReader = new FileReader(file);
                    
                    BufferedReader bReader = new BufferedReader(fReader);
                    
                    str1 = bReader.readLine();
                    
                    while((str = bReader.readLine()) != null)
                    {
                        str1 = str1 + "\n" + str;
                    }
                    
                    text.setText(str1);
                }
                catch(Exception evt)
                {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(frame, "the user cancelled the operation");
            }
        }
        else if(command.equals("New"))
        {
            text.setText("");
        }
        else if(command.equals("Run"))
        {
        	System.out.println("RUN");
        	v.compile(save());
        }
    }
    
    public File save()
    {
    	JFileChooser chooser = new JFileChooser("f:");
        
        int num = chooser.showSaveDialog(null);
        
        if(num == JFileChooser.APPROVE_OPTION)
        {
            File file = new File(chooser.getSelectedFile().getAbsolutePath());
            
            try
            {
            	FileWriter fWriter = new FileWriter(file, false);
                
                BufferedWriter bWriter = new BufferedWriter(fWriter);
                
                bWriter.write(text.getText()); 

                bWriter.flush(); 
                bWriter.close(); 
            } 
            catch(Exception evt)
            {
                JOptionPane.showMessageDialog(frame, evt.getMessage()); 
            }
            
            return file;
        }
        else
        {
            JOptionPane.showMessageDialog(frame, "the user cancelled the operation");
        }
        
        return null;
    }
    
    public static void main(String args[])
    {
//        GUI gui = new GUI();
    }
}