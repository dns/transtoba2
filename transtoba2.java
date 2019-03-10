/******************************************************************************

    transtoba2 - pure java edition

    linguistics by dr. uli kozok
    written by leander seige

    released under the gnu general public license version 3
    
    Copyright (C) 2008 leander Seige and dr. uli kozok

    http://transtoba2.seige.net
    transtoba2@seige.net (programming)
    uli@bahasa.net (linguistics)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
    
 ******************************************************************************/	

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.*;
import javax.swing.border.*;


public class transtoba2 extends JApplet implements ActionListener, KeyListener
{
	public static final long serialVersionUID = 23L;

	Map<String,String> map_prefilter_de = new HashMap<String,String>();
	Map<String,String> map_prefilter_id = new HashMap<String,String>();
	Map<String,String> map_transtoba = new HashMap<String,String>();
	LinkedList<String> cache_keys = new LinkedList<String>();
	LinkedList<String> cache_vals = new LinkedList<String>();	
	
	String[] pf_in,pf_out;
	ArrayList <Integer> tt_range=new ArrayList<Integer>();
	ArrayList <String> tt_in=new ArrayList<String>();
	ArrayList <String> tt_out=new ArrayList<String>();		
	ArrayList <Integer> tt_os=new ArrayList<Integer>();		
	int ttc, pfc;

	Font font;
	Border bd_space;
	TitledBorder bd_eingabe, bd_ausgabe;
	JPanel panel1,panel11,panel12;
	JTextArea ausgabe,eingabe;
	JScrollPane s_ausgabe,s_eingabe;
	JLabel l_toggle_font;
	JComboBox toggle_font;
	JLabel l_toggle_prefilter;
	JComboBox toggle_prefilter;    
	JToggleButton toggle_whitespaces;    
	JComboBox toggle_lang;   
	JProgressBar progress;
	ImageIcon[] flag_icon = new ImageIcon[3];
	ButtonGroup flag_buttongroup;
	JRadioButton[] flag_button = new JRadioButton[3];
	JSeparator sep1,sep2,sep3,sep4;
	JComboBox toggle_zoom;
	JComboBox toggle_laf;
	String[][] toggle_font_opts = {
		{ "Batak Toba", "Latin" },
		{ "Batak Toba", "Lateinisch" },
		{ "Batak Toba", "Latin" }
	};
	String[][] toggle_zoom_opts = {
		{ "Zoom Off", "Zoom On" },
		{ "Zoom Aus", "Zoom An" },
		{ "Zoom Off", "Zoom On" }
	};
	String[][] toggle_prefilter_opts = {
		{ "Batak or Indonesian","German" },
		{ "Batak oder Indonesisch","Deutsch" },
		{ "Batak atau Indonesia","Jerman" }
	};
	String[] toggle_lang_opts={
	    "English","Deutsch","Bahasa Indonesia"
	};
	String str_in = "";
	String str_out = "";
	Font toba_font,roman_font;
	Font toba_font_big,roman_font_big;
	
	int glid;
	String gui_lang,gui_lang_switch;
	String[] gui_lang_eingabe={"Input","Eingabe","Masukan"};
	String[] gui_lang_ausgabe={"Output","Ausgabe","Keluaran"};
	String[] gui_lang_output_as={"Output as:","Ausgabe:","Keluaran beraksara:"};
	String[] gui_lang_preprocess_as={"Input:","Eingabe:","Masukan:"};
	String[] gui_lang_spaces={"Spaces","Leerzeichen","Spasi"};
	UIManager.LookAndFeelInfo lafs[];

/******************************************************************************
  standalone wrapper
 ******************************************************************************/	

	public static void main(String[] args) {
		JApplet applet = new transtoba2();
		final JFrame frame = new JFrame("transtoba2");

		WindowAdapter wa = new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				System.exit(0);
			}
		};
		
		applet.init();
		frame.setContentPane(applet.getContentPane());
		frame.addWindowListener(wa);
		frame.setSize(600,400);
		frame.setLocation(0,0);
		frame.setVisible(true);
		applet.start();
	}

/******************************************************************************
  applet initialization
 ******************************************************************************/	

	public void build_window_layout() {
		int i;
				
		if(gui_lang.equals("de")) {
		    glid=1;
		} else if(gui_lang.equals("id")) {
		    glid=2;
		} else {
		    glid=0;
		}

		flag_icon[0]=new ImageIcon(transtoba2.class.getClassLoader().getResource("en.png"));
		flag_icon[1]=new ImageIcon(transtoba2.class.getClassLoader().getResource("de.png"));
		flag_icon[2]=new ImageIcon(transtoba2.class.getClassLoader().getResource("id.png"));
		flag_buttongroup = new ButtonGroup();
		flag_button[0]=new JRadioButton(flag_icon[0]);
		flag_button[1]=new JRadioButton(flag_icon[1]);
		flag_button[2]=new JRadioButton(flag_icon[2]);
		flag_button[0].addActionListener(this);
		flag_button[1].addActionListener(this);
		flag_button[2].addActionListener(this);
		flag_button[0].setBackground(Color.WHITE);
		flag_button[1].setBackground(Color.WHITE);
		flag_button[2].setBackground(Color.WHITE);
		flag_buttongroup.add(flag_button[0]);
		flag_buttongroup.add(flag_button[1]);
		flag_buttongroup.add(flag_button[2]);

		panel1 = new JPanel();
		panel11 = new JPanel();
		panel12 = new JPanel();

		getContentPane().setBackground(Color.WHITE);

		bd_eingabe=BorderFactory.createTitledBorder(gui_lang_eingabe[glid]);
		bd_ausgabe=BorderFactory.createTitledBorder(gui_lang_ausgabe[glid]);
		bd_space=BorderFactory.createEmptyBorder(4,4,4,4);

		sep1=new JSeparator(JSeparator.VERTICAL);
		sep1.setMaximumSize(new Dimension(6,20));
		sep1.setMinimumSize(new Dimension(4,20));
		sep2=new JSeparator(JSeparator.VERTICAL);
		sep2.setMaximumSize(new Dimension(6,20));
		sep2.setMinimumSize(new Dimension(4,20));
		sep3=new JSeparator(JSeparator.VERTICAL);
		sep3.setMaximumSize(new Dimension(6,20));
		sep3.setMinimumSize(new Dimension(4,20));
		sep4=new JSeparator(JSeparator.VERTICAL);
		sep4.setMaximumSize(new Dimension(6,20));
		sep4.setMinimumSize(new Dimension(4,20));

		panel1.setLayout(new BorderLayout());
		panel11.setLayout(new GridLayout(2,1));
		panel12.setLayout(new BoxLayout(panel12,BoxLayout.X_AXIS));
		panel1.add(panel11,BorderLayout.CENTER);
		panel1.add(panel12,BorderLayout.SOUTH);
		panel1.setBackground(Color.WHITE);
		panel11.setBackground(Color.WHITE);
		panel12.setBackground(Color.WHITE);

		eingabe=new JTextArea(str_in);
		eingabe.setFont(roman_font);
		eingabe.setWrapStyleWord(true);
		eingabe.setLineWrap(true);
		eingabe.addKeyListener(this);
		eingabe.setBackground(Color.WHITE);

		ausgabe=new JTextArea(str_out);
		ausgabe.setWrapStyleWord(true);
		ausgabe.setLineWrap(true);
		ausgabe.setFont(toba_font);
		ausgabe.setBackground(Color.WHITE);

		s_eingabe=new JScrollPane(eingabe);
		s_eingabe.setBackground(Color.WHITE);
		s_ausgabe=new JScrollPane(ausgabe);
		s_ausgabe.setBackground(Color.WHITE);
		s_eingabe.setBorder(BorderFactory.createCompoundBorder(
			bd_eingabe,bd_space)
		);
		s_ausgabe.setBorder(BorderFactory.createCompoundBorder(
			bd_ausgabe,bd_space)
		);
		s_eingabe.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s_ausgabe.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s_eingabe.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		s_ausgabe.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		l_toggle_font=new JLabel(gui_lang_output_as[glid]);
		toggle_font=new JComboBox(toggle_font_opts[glid]);
		toggle_font.addActionListener(this);

		l_toggle_prefilter=new JLabel(gui_lang_preprocess_as[glid]);
		toggle_prefilter=new JComboBox(toggle_prefilter_opts[glid]);
		toggle_prefilter.addActionListener(this);

		toggle_zoom=new JComboBox(toggle_zoom_opts[glid]);
		toggle_zoom.addActionListener(this);

		toggle_whitespaces=new JToggleButton(gui_lang_spaces[glid]);
		toggle_whitespaces.addActionListener(this);

		toggle_lang=new JComboBox(toggle_lang_opts);
		toggle_lang.addActionListener(this);
		toggle_lang.setSelectedIndex(glid);
/*
		toggle_laf=new JComboBox();
	        lafs = UIManager.getInstalledLookAndFeels();
	        for(i=0; i<lafs.length; i++) {
		    toggle_laf.addItem(lafs[i].getName());
		}
		toggle_laf.addActionListener(this);

		toggle_laf.setMaximumSize(toggle_whitespaces.getMaximumSize());
		toggle_laf.setBackground(Color.WHITE);
*/
		toggle_prefilter.setMaximumSize(toggle_whitespaces.getMaximumSize());
		toggle_prefilter.setBackground(Color.WHITE);
		
		toggle_zoom.setMaximumSize(toggle_whitespaces.getMaximumSize());
		toggle_zoom.setBackground(Color.WHITE);		
		
		progress = new JProgressBar(JProgressBar.VERTICAL,0,100);
		progress.setMaximumSize(new Dimension(20,20));
		progress.setPreferredSize(new Dimension(20,20));
		progress.setBackground(Color.WHITE);
		progress.setForeground(Color.RED);		
		
		panel11.add(s_eingabe);
		panel11.add(s_ausgabe);
		panel12.add(flag_button[0]);
		panel12.add(flag_button[1]);
		panel12.add(flag_button[2]);
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(sep1);
   		panel12.add(Box.createHorizontalGlue());
//		panel12.add(toggle_laf);
		panel12.add(toggle_zoom);
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(sep4);
   		panel12.add(Box.createHorizontalGlue());
//		panel12.add(toggle_lang);
//		panel12.add(l_toggle_font);
//		panel12.add(toggle_font);
		panel12.add(l_toggle_prefilter);
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(toggle_prefilter);	
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(sep2);
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(toggle_whitespaces);	
   		panel12.add(Box.createHorizontalGlue());
		panel12.add(sep3);
   		panel12.add(Box.createHorizontalGlue());
    	panel12.add(progress);
		getContentPane().add(panel1);

		action_text_in_main();
	}

	public void load_ttf_fonts() {
		InputStream fontStream = null;
		URL fontURL=
			transtoba2.class.getClassLoader().getResource("batak-toba-1.2.2.ttf");
		try { fontStream = fontURL.openStream(); }
		catch (Exception e) {
			e.printStackTrace(); 
		}	
		try { font = Font.createFont(Font.TRUETYPE_FONT, fontStream); }
		catch (Exception e) {
			str_in=str_in+"Error 2\n";
			e.printStackTrace();
		}
		try { fontStream.close();}
		catch (Exception e) {
			str_in=str_in+"Error 3\n";
			e.printStackTrace();
		}
		toba_font=font.deriveFont(24f);
		toba_font_big=font.deriveFont(48f);		
		fontURL=
			transtoba2.class.getClassLoader().getResource("liberationsans-regular.ttf");
		try { fontStream = fontURL.openStream(); }
		catch (Exception e) {
			e.printStackTrace(); 
		}	
		try { font = Font.createFont(Font.TRUETYPE_FONT, fontStream); }
		catch (Exception e) {
			str_in=str_in+"Error 2\n";
			e.printStackTrace();
		}
		try { fontStream.close();}
		catch (Exception e) {
			str_in=str_in+"Error 3\n";
			e.printStackTrace();
		}
		roman_font_big=font.deriveFont(36f);
		roman_font=font.deriveFont(18f);
	}

	public void init() {
		System.out.println("transtoba2, version "+serialVersionUID);
		System.out.println("(c) 2008 leander seige, dr. uli kozok");
		System.out.println("released under the gnu gpl version 3");

		try { gui_lang = getParameter("gui_lang"); }
		catch (Exception e) { gui_lang="en"; glid=0; }
		if(gui_lang==null) {
			gui_lang="en";
		}

    	try { gui_lang_switch = getParameter("gui_lang_switch"); }
		catch (Exception e) { gui_lang_switch="yes"; }
		if(gui_lang_switch==null) {
			gui_lang_switch="no";
		}
		
		read_prefilter("de",map_prefilter_de);
		read_prefilter("id",map_prefilter_id);
		read_transtoba_code();
		load_ttf_fonts();
		build_window_layout();
	}

	
	public void start() {
	}

	public void stop() {
	}

	public void destroy() {
	}

/******************************************************************************
  action
 ******************************************************************************/	
 
	public void set_lang_to_gui() {
	    bd_eingabe.setTitle(gui_lang_eingabe[glid]);
	    bd_ausgabe.setTitle(gui_lang_ausgabe[glid]);
	    l_toggle_font.setText(gui_lang_output_as[glid]);
	    l_toggle_prefilter.setText(gui_lang_preprocess_as[glid]);
	    toggle_font.removeAllItems();
	    toggle_prefilter.removeAllItems();
	    toggle_zoom.removeAllItems();
	    toggle_font.addItem(toggle_font_opts[glid][0]);
	    toggle_font.addItem(toggle_font_opts[glid][1]);
	    toggle_prefilter.addItem(toggle_prefilter_opts[glid][0]);
	    toggle_prefilter.addItem(toggle_prefilter_opts[glid][1]);
	    toggle_zoom.addItem(toggle_zoom_opts[glid][0]);
	    toggle_zoom.addItem(toggle_zoom_opts[glid][1]);
	    toggle_whitespaces.setText(gui_lang_spaces[glid]);
	    panel1.repaint();
	}
 
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==toggle_lang ) {
		    glid=toggle_lang.getSelectedIndex();
		    set_lang_to_gui();
		} else if (e.getSource()==flag_button[0]) {
		    glid=0;
		    set_lang_to_gui();
		} else if (e.getSource()==flag_button[1]) {
		    glid=1;
		    set_lang_to_gui();
		} else if (e.getSource()==flag_button[2]) {
		    glid=2;
		    set_lang_to_gui();
		} else if(e.getSource()==toggle_font) {
			if(toggle_font.getSelectedItem()==toggle_font_opts[glid][0]) {
				ausgabe.setFont(toba_font); 
			} else {
				ausgabe.setFont(roman_font); 
			}
    	    action_text_in_main();
    	} else if (e.getSource()==toggle_zoom) {
    		if(toggle_zoom.getSelectedItem()==toggle_zoom_opts[glid][1]) {
    			eingabe.setFont(roman_font_big);
    			ausgabe.setFont(toba_font_big);
    		} else {
    			eingabe.setFont(roman_font);
    			ausgabe.setFont(toba_font);
    		}
		} else if (e.getSource()==toggle_whitespaces) {
    	    	    action_text_in_main();
		} /* else if (e.getSource()==toggle_laf) {
		    try {
			UIManager.setLookAndFeel(lafs[toggle_laf.getSelectedIndex()].getClassName()); 
			SwingUtilities.updateComponentTreeUI(panel1); 
		    }
	            catch (Exception xe) { }
		} */ else if(e.getSource()==toggle_prefilter) {
    	    	    action_text_in_main();		
		}
	}

	public void action_text_in_main() {
		str_in=eingabe.getText();
		apply_prefilter();
		apply_transtoba();
		ausgabe.setText(str_out);
	}

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
	if(e.isActionKey()==false) {
	    action_text_in_main();
	} 
    }

/******************************************************************************
  loading transtoba data files
 ******************************************************************************/	

	public String hex2asc(String in) {
		String temp=in;
		String out="";
		int pi=0;

		while(temp.length()!=0) {
			pi=Integer.parseInt(temp.substring(0,4),16);
			out=out+(char)pi;
			temp=temp.substring(4);
		}
		return out;
	}	

	public void read_transtoba_code() {
		String[] result;
		String s;
		InputStream ins = null;
		Integer ti=0;
		try {
			URL codeurl=
				transtoba2.class.getClassLoader().getResource("transtoba-code.dat");
			try { ins = codeurl.openStream(); }
			catch (Exception e) {
				e.printStackTrace();
			}	
			InputStreamReader reader = new InputStreamReader(ins);
			BufferedReader br = new BufferedReader(reader);
			ttc=0;
			while(null!=(s=br.readLine())) {
				result=s.trim().split("\\s+");
				if(result.length>2) {
					tt_os.add(ttc,ti.parseInt(result[0]));
					tt_range.add(ttc,ti.parseInt(result[1]));
					tt_in.add(ttc,result[2]);
					tt_out.add(ttc,hex2asc(result[3]));
					ttc++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read_prefilter(String lang, Map<String,String> map) {
		String[] result;
		String s;
		InputStream ins = null;
		try {
			URL codeurl=
				transtoba2.class.getClassLoader().getResource("transtoba-prefilter-"+lang+".dat");
			try { ins = codeurl.openStream(); }
			catch (Exception e) {
				e.printStackTrace();
			}	
			InputStreamReader reader = new InputStreamReader(ins);
			BufferedReader br = new BufferedReader(reader);
			while(null!=(s=br.readLine())) {
				result=s.trim().split("\\s+");
				if(result.length>1) {
					map.put(result[0],result[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

/******************************************************************************
  prefiltering - replacing characters we don't have replacements for ;)
 ******************************************************************************/	

	public void apply_prefilter() {
		str_out=str_in;
		if(toggle_prefilter.getSelectedItem()==toggle_prefilter_opts[glid][0]) {
			for(Map.Entry<String,String> e : map_prefilter_id.entrySet() ) {
				str_out=str_out.replaceAll(e.getKey().toLowerCase(),e.getValue().toLowerCase());
				str_out=str_out.replaceAll(e.getKey().toUpperCase(),e.getValue().toUpperCase());
			}
		} else {
			for(Map.Entry<String,String> e : map_prefilter_de.entrySet() ) {
				str_out=str_out.replaceAll(e.getKey().toLowerCase(),e.getValue().toLowerCase());
				str_out=str_out.replaceAll(e.getKey().toUpperCase(),e.getValue().toUpperCase());
			}
		}

	}

/******************************************************************************
  main translitaration functions
 ******************************************************************************/	

	public void proc_cache(String k, String v) {
		int i=cache_keys.indexOf(k);
		if(i!=-1) {
			cache_keys.remove(i);
			cache_vals.remove(i);			
		}
		cache_keys.addLast(k);
		cache_vals.addLast(v);
		if(cache_keys.size()>250) {
			cache_keys.removeFirst();
			cache_vals.removeFirst();			
		}
	}

	public void apply_transtoba() {
		String out="",temp="",workon="",cache;
		int pi=0,check=0,i=0,j=0;

//		System.out.println("--\n");
	
		// run through all input lines
		String[] tempb=str_out.toUpperCase().split("\n"); 
		for(j=0;j<tempb.length;j++) {
		if(progress!=null) {
			progress.setValue(100-(100/tempb.length)*j); 
			progress.paintImmediately(progress.getVisibleRect());
		}
		// run through all input words for each line
		String[] tempa=tempb[j].split("\\s+");
		for(i=0;i<tempa.length;i++) {
			workon=tempa[i];
			// run through every input word
			if(cache_keys.contains(workon)==false) {
			    cache="";
			    for(int x=0;x<workon.length();) {
				// run through the list of regex strings
				boolean ready=false;
				for(int z=0;z<ttc&&ready==false;z++) {
					//does this rule only match word beginnings?
					// yes, we don't have to add "^" because it's already there; 'coz we're checking a beginning-of-word-rule
					if(tt_in.get(z).charAt(0)=='^') {
						if(x==0) {
							if(workon.matches(tt_in.get(z)+".*")) {
								out=out+tt_out.get(z);
								cache=cache+tt_out.get(z);
								// System.out.println(tt_in.get(z)+"=>"+workon.substring(x)+"("+out+")");
								x=x+tt_range.get(z);
								ready=true;
						}
					}
					// no, we have to add "^" ('coz we always need to pass it to regex)
					} else if((workon.length()>x+tt_os.get(z))&&(x+tt_os.get(z)>=0)) {
						if(workon.substring(x+tt_os.get(z)).matches("^"+tt_in.get(z)+".*")) {
							out=out+tt_out.get(z);
							cache=cache+tt_out.get(z);
							// System.out.println(tt_in.get(z)+"=>"+workon.substring(x)+"("+out+")");
							x=x+tt_range.get(z);
							ready=true;								
						}
					} 
				}
				if(ready==false) {
					out=out+workon.charAt(x);
					cache=cache+workon.charAt(x);
					x=x+1;
				}
			} 
//			    System.out.println("to cache: "+workon);
			    proc_cache(workon,cache);
			} else {
//			    System.out.println("from cache: "+workon);
			    out=out+cache_vals.get(cache_keys.indexOf(workon));
			    proc_cache(workon,cache_vals.get(cache_keys.indexOf(workon)));
			}
			out=out+" ";
		}
			out=out+"\n";		
		}

		// warp diacritics around c: t=out.charAt(x-1); out.charAt(x-1)=out(x-2); out.charAt(x-2)=t;				
		for(int x=3;x<out.length();x++) {
			if(	toba_is_konsonant(out.charAt(x-3))			&&
					toba_is_konsonant(out.charAt(x-1))		&&
					toba_is_diacritic(out.charAt(x-2))		&&
					toba_is_diacritic(out.charAt(x))		&&
					out.charAt(x-2)!=0x5C					&&
					out.charAt(x)==0x5C
			) {
				out=out.substring(0,x-2)
				+out.substring(x-1,x-0)
				+out.substring(x-2,x-1)
				+out.substring(x);
			}	    
		}

		// c: out.charAt(x-2)+=0x20; out.charAt(x-1)-=0x20; 
		for(int x=2;x<out.length();x++) {
			if(	toba_is_konsonant_u(out.charAt(x-2))	&&
				toba_is_konsonant(out.charAt(x-1))	&&
				out.charAt(x)==0x5C
			) {	
				out=out.substring(0,x-2)
				+(char)((int)(out.substring(x-2,x-1).charAt(0))+0x20)
				+(char)((int)(out.substring(x-1,x-0).charAt(0))-0x20)
				+out.substring(x);	
			}	    
		}

		if(toggle_whitespaces.isSelected()==false) {
			out=out.replaceAll(" ","");
		}

		str_out=out;
		if(progress!=null) {
			progress.setValue(0);
			progress.paintImmediately(progress.getVisibleRect());
		}
	
//		System.out.println("cache: "+cache_keys.size());
	}
	
/******************************************************************************
  little helpers, probably not used currently
 ******************************************************************************/	

	public boolean toba_is_konsonant(char in) {
		char[] kons= {0x61,0x68,0x6B,0x62,0x70,0x6E,0x77,0x67,0x6A,0x64,0x72,0x6D,0x74,0x73,0x79,0x3C,0x6C,0x00};
		for(int x=0;in!=kons[x];x++) { if(kons[x]==0x00) return false; }
		return true; 
	}

	public boolean toba_is_konsonant_u(char in) {
		char[] k_u= {0x41,0x48,0x4B,0x42,0x50,0x4E,0x57,0x47,0x4A,0x44,0x52,0x4D,0x54,0x53,0x59,0x3E,0x4C,0x00}; // ,0x5D removed for new ny(u) rule, version 0.9
		for(int x=0;in!=k_u[x];x++)	{ if(k_u[x]==0x00) return false; }
		return true; 
	}

	public boolean roman_is_vokal(char in) {
		char[] dv= {'A','E','I','O','U',0x00};
		for(int x=0;dv[x]!=0x00;x++) { if(dv[x]==in) return true; }
		return false; 
	}

	public boolean toba_is_diacritic(char in) {
		char[] dia= {0x5C,0x65,0x69,0x6F,0x78,0x00};
		for(int x=0;in!=dia[x];x++)
		{ if(dia[x]==0x00) return false; }
		return true; 
	}

} 

