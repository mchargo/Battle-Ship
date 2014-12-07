package battleShip;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Window implements WindowListener
{
	public Window(){this(30, 50);}
	public Window(int rows, int columns){this("Java24Hours", rows, columns, true);}
	public Window(String title, int rows, int columns, boolean exitOnClose)
	{
		this.exitOnClose = exitOnClose;
		latch = new CountDownLatch(1);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		field = new JTextField(columns);
		field.setFont( new Font("monospaced", Font.PLAIN, 14) );
		// enable auto scrolling
		DefaultCaret caret = (DefaultCaret)field.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		field.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				entered = field.getText();
				field.setText("");
				latch.countDown();
			}
		});

		area = new JTextArea(rows, columns);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setEditable(false);
		area.setFont( new Font("monospaced", Font.PLAIN, 14) );
		pane = new JScrollPane(area);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel southLayout = new JPanel();
		southLayout.add(field);
		panel.add(southLayout, BorderLayout.SOUTH);

		JPanel centerLayout = new JPanel();
		centerLayout.add(pane);
		panel.add(centerLayout, BorderLayout.CENTER);

		frame = new JFrame("");

		frame.addWindowListener(this);

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		field.requestFocus();
	}

	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowClosed(WindowEvent e){}
	@Override public void windowDeactivated(WindowEvent e){}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e){}
	@Override public void windowOpened(WindowEvent e){}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(exitOnClose) System.exit(0);
	}

	public void print(final String s)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				area.append(s);
				area.repaint();
			}
		};

		SwingUtilities.invokeLater(run);
	}

	public void println(){println("");}
	public void println(final String s)
	{
		Runnable run = new Runnable()
		{
			public void run()
			{
				area.append(s + "\n");
				area.repaint();
			}
		};

		SwingUtilities.invokeLater(run);
	}

	public String nextLine()
	{
		try 
		{
			latch.await();
			resetLatch();
			return entered.trim();
		} catch (InterruptedException e) 
		{
			System.out.println("Error occurred: " + e.toString());
			e.printStackTrace();
		}

		return null;
	}

	public int nextInt()
	{
		while(true)
		{
			try
			{
				return Integer.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public long nextLong()
	{
		while(true)
		{
			try
			{
				return Long.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public float nextFloat()
	{
		while(true)
		{
			try
			{
				return Float.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public double nextDouble()
	{
		while(true)
		{
			try
			{
				return Double.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public short nextShort()
	{
		while(true)
		{
			try
			{
				return Short.valueOf(nextLine());
			}catch(Exception e){}
		}
	}

	public void clear()
	{
		if(Thread.currentThread().getId() == 1) // main thread
		{
			area.setText("");
		}else{
			// not main thread
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					area.setText("");
				}
			});
		}
	}

	public boolean prompt()
	{
		return nextLine().toLowerCase().startsWith("y");
	}

	public void resetLatch()
	{
		latch = new CountDownLatch(1);
	}

	private boolean exitOnClose;
	private JFrame frame;
	private JPanel panel;
	private JTextArea area;
	private JScrollPane pane;
	private JTextField field;
	private String entered;
	private CountDownLatch latch;
}
