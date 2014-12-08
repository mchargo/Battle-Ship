package battleShip;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class WindowApplet extends JApplet
{
	@Override
	public void init()
	{
		int columns = 50;
		int rows = 31;
		
		latch = new CountDownLatch(1);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		field = new JTextField(columns);
		field.setFont( new Font("monospaced", Font.PLAIN, 14) );
		// enable auto scrolling
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
		DefaultCaret caret = (DefaultCaret)area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane = new JScrollPane(area);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel southLayout = new JPanel();
		southLayout.add(field);
		panel.add(southLayout, BorderLayout.SOUTH);

		JPanel centerLayout = new JPanel();
		centerLayout.add(pane);
		panel.add(centerLayout, BorderLayout.CENTER);

		getContentPane().add(panel);
		setSize(WIDTH, HEIGHT);
		setVisible(true);

		field.requestFocus();
		
		bootstrap();
	}
	
	public void bootstrap()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				BattleShip.window = WindowApplet.this;
				BattleShip.main(new String[0]);
			}
		}).start();
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

	private JPanel panel;
	private JTextArea area;
	private JScrollPane pane;
	private JTextField field;
	private String entered;
	private CountDownLatch latch;
	
	private static final int WIDTH = 414;
	private static final int HEIGHT = 565;
	
	private static final long serialVersionUID = -8071775499154147429L;
}
