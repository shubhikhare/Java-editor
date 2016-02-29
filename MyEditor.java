import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class MyEditor implements ActionListener
{
	JFrame jf;
	JLabel jl;
	JTextField jtf;
	JTextArea jta,jta1;
	JButton jbcompile,jbrun;
	JScrollPane jsp,jsp1;
	Runtime r;
	String str="";
	String fname="";
	String result="";
	MyEditor()
	{
		jf=new JFrame("My Editor");
		jf.setLayout(null);
		jl=new JLabel("			Enter java class name");
		jl.setBounds(180,20,230,25);
		jtf=new JTextField();
		jtf.setBounds(130,20,50,25);
		jta=new JTextArea(50,50);
		jta.addFocusListener(new MyFocusListener(this));
		jta1=new JTextArea(50,50);
		jta.setFont(new Font("varinda",Font.PLAIN,15));
		jta1.setFont(new Font("varinda",Font.PLAIN,15));
		jsp=new JScrollPane(jta);
		jsp1=new JScrollPane(jta1);
		jsp.setBounds(50,60,320,150);	
		jsp1.setBounds(50,270,320,150);
		jf.add(jsp);
		jf.add(jsp1);
		jbcompile=new JButton("Compile");
		jbrun=new JButton("Run");
		jbcompile.setBounds(100,230,80,25);	
		jbrun.setBounds(280,230,80,25);
		jf.add(jl);
		jf.add(jtf);
		r=Runtime.getRuntime();
		jf.add(jbrun);
		jf.add(jbcompile);
		jbcompile.addActionListener(this);	
		jbrun.addActionListener(this);	
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(550,550);
		jf.setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbcompile)
		{
			str="";
			if(!jtf.getText().equals(""))
			{
				try
				{
					fname=jtf.getText().trim()+".java";
					FileWriter fw=new FileWriter(fname);
					String s1=jta.getText();
					PrintWriter pw=new PrintWriter(fw);
					pw.println(s1);
					pw.flush();
					Process error=r.exec("C:\\Program Files (x86)\\Java\\jdk1.8.0\\bin\\javac.exe -d . "+fname);
					BufferedReader err=new BufferedReader(new InputStreamReader(error.getErrorStream()));
					while(true)
					{
						String temp=err.readLine();
						if(temp!=null)
						{
							result+=temp;
							result+="\n";
						}
						else break;		
					}
					if(result.equals(""))
					{
						jta1.setText("Compilation Successful!"+fname);
						err.close();
					}
					else jta1.setText(result);
				}
				catch(Exception e1)
				{
					System.out.println(e1);
				}
			}
			else jta1.setText("Please Enter the java program name");
		}
		else if(e.getSource()==jbrun)
		{
			int start=0;
			try
			{
				String fn=jtf.getText().trim();
				Process p=r.exec("C:\\Program Files (x86)\\Java\\jdk1.8.0\\bin\\java       "+fn);
				BufferedReader output=new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader error=new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while(true)
				{
					String temp=output.readLine();
					if(temp!=null)
					{
						result+=temp;
						result+="\n";	
					}
					else break;
				}
				if(result.equals(""))
				{
					while(true)
					{
						String temp=error.readLine();
						if(temp!=null)
						{
							result+=temp;
							result+="\n";
						}
						else break;
					}
				}
				output.close();
				error.close();
				jta1.setText(result);
			}
			catch(Exception e2)
			{
				System.out.println(e2);
			}
		}
	}
	public static void main(String s[])
	{
		new MyEditor();
	}		 
}
class MyFocusListener extends FocusAdapter
{
	MyEditor e;
	MyFocusListener(MyEditor e)
	{
		this.e=e;
	}
	public void focusGained(FocusEvent fe)
	{
		String str=e.jtf.getText().trim();
		e.jta.setText("public class " +str+"\n"
			     +"{"+"\n"
					+"public static void main(String s[])"+"\n"
					+"{"+"\n"
					+"			"+"\n"
					+"}"+"\n"
			     +"}");
	}
}