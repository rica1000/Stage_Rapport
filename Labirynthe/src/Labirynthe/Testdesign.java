/*UPDATED 12/08/2019*/
package Labirynthe;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
public class Testdesign extends JPanel implements MouseListener,
MouseMotionListener,ActionListener, Runnable{
	private JFrame frame;
	private JButton animation,clear,newmaze;
	private JComboBox <String> algo;
	private JComboBox <Integer> grid;
	private JPanel pan,pan2;
	private JLabel ltime,ltemps,lobs,lorg,ltar,lpath,lgrid,lalgo;
	private ArrayList<Integer> obsx,obsy;
	private int action;
	private Point origin,target;
	private boolean clickedorg,clickedtar,deplaceorg,deplacetar;
	
	private Noeud [] [] espace;
	private Noeud source,destination;
	private ArrayList<Noeud> toExplore,visited,obstacle;
	private HashMap<Noeud,Noeud> cameFrom;
	private HashMap<Noeud,Integer> gScore,fscore;
	private boolean activated;
	private String typeheuristic;
	private ArrayList<Noeud> bestpath;
	private int spacelength;
	private Accueil accueil;
	public Testdesign() { 
		setBackground(new Color(94,127,24));
		activated = false;
		spacelength = 5;
		/*Components*/
		lgrid = new JLabel("GRILLE");
		lalgo = new JLabel("TYPE ALGO");
		ltemps = new JLabel("Temps estimé :");
		ltime = new JLabel();
		ltime.setForeground(Color.red);
		ltime.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		lobs = new JLabel("OBSTACLE");
		lorg = new JLabel("ORIGIN");
		ltar = new JLabel("TARGET");
		lpath = new JLabel("BEST PATH");
		lobs.setForeground(Color.white);
		lorg.setForeground(Color.green);
		ltar.setForeground(Color.red);
		lpath.setForeground(Color.yellow);
		algo = new JComboBox <String> ();
		grid = new JComboBox <Integer> ();
		grid.setSize(15,15);
		for(int i=3;i<50;i++) grid.addItem(i);
		algo.addItem("MANHATTAN");
		algo.addItem("PYTHAGORE");
		algo.addItem("CHEBICHEF");
		animation = new JButton("ANIMATION");
		animation.addActionListener(this);
		animation.setToolTipText("Start A* with all parameters set");
		clear = new JButton("CLEAR");
		clear.addActionListener(this);
		clear.setToolTipText("Remove all nodes and clear the space");
		newmaze = new JButton("NEW MAZE");
		newmaze.addActionListener(this);
		newmaze.setToolTipText("Create a new squared space with dimensions given at the grid setting");
		
		/*Containers : frame is the principal container, the current Testdesign contains the grid,
		 * pan contains commands to change parameters, and pan2 contains result time and labels
		 */
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		pan = new JPanel(new GridBagLayout());
		GridBagConstraints gc =new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.gridwidth = 2;
		pan.setBackground(new Color(94,127,24));
		pos(0,0,gc);
		gc.insets = new Insets(0,0,0,0);
		pan.add(lgrid,gc);
		pos(0,1,gc);
		gc.insets = new Insets(0,0,20,0);
		pan.add(grid,gc);
		pos(0,2,gc);
		gc.insets = new Insets(0,0,0,0);
		pan.add(lalgo,gc);
		pos(0,3,gc);
		gc.insets = new Insets(0,0,20,0);
		pan.add(algo,gc);
		pos(0,4,gc);
		pan.add(newmaze,gc);
		pos(0,5,gc);
		pan.add(clear,gc);
		pos(0,6,gc);
		pan.add(animation,gc);
		pan2 = new JPanel(new GridLayout(3,3));
		pan2.add(lorg);
		pan2.add(ltar);
		pan2.add(lobs);
		pan2.add(lpath);
		pan2.add(ltemps);
		pan2.add(ltime);
		pan2.setBackground(new Color(94,127,24));
		accueil = new Accueil("iconi.png");
		frame.add(accueil,BorderLayout.CENTER);
		clickedorg = false;
		clickedtar = false;
		deplaceorg = false;
		deplacetar = false;
		obsx = new ArrayList<Integer> ();
		obsy = new ArrayList<Integer> ();
		action = 0;
		origin = new Point();
		target = new Point();
		accueil.getBegin().addActionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);	
		frame.setVisible(true);
	}

	
	
	private void pos(int i, int j,GridBagConstraints gc) {
		gc.gridx = i;
		gc.gridy = j;
	}



	private void initiation(String string) {
		 /*Type of distance*/
		 typeheuristic = string;
		 /*The squared space*/
		 espace = new Noeud [spacelength][spacelength];
		 /*the list of node available from the current node*/
		 toExplore = new ArrayList<Noeud>();
		 /*visited node*/
		 visited = new ArrayList<Noeud>();
		 obstacle = new ArrayList<Noeud>();
		 spaceinit();
		 source = espace[(int) origin.getX()][(int) origin.getY()];
		 destination = espace[(int) target.getX()][(int) target.getY()];
		 initObs();
		 toExplore.add(source);
		 initXY();
		 initCameFrom();
		 initGScore();
		 initFScore();
		 bestpath = search();
		 activated = true;
		 repaint();
	}
	 private void initObs() {
		 /*Put random obstacles*/
		for(int i=0;i<obsx.size();i++) {
			int x = obsx.get(i);
			int y=  obsy.get(i);
			obstacle.add(espace[x][y]);
		}
		
	}
	private void spaceinit() {
		/*Initialize the space*/
		 int k=0;
		 for(int i=0;i<espace.length;i++) {
			 for(int j=0;j<espace.length;j++) {
				 espace [i] [j] = new Noeud(""+k);
				 k++;
			 }
		 }
		
	}
	private void initFScore() {
		 /*fscore represents the sum of the heuristic and the distance 
		  * between the source and the current node*/
		 fscore = new HashMap<Noeud,Integer>();
		 for(int i=0;i<espace.length;i++) {
			 for(int j=0;j<espace.length;j++) {
				 fscore.put(espace[i][j],heuristic(espace[i][j],destination)+
						 gScore.get(espace[i][j]));
			 }
		 }
		 System.out.println(fscore);
	}
	private void initGScore() {
			/*gScore represents the heuristic distance between the source and the current node
			 */
		 gScore = new HashMap<Noeud,Integer>();
		 for(int i=0;i<espace.length;i++) {
			 for(int j=0;j<espace.length;j++) {
				 gScore.put(espace[i][j], 10);
			 }
		 }
		 gScore.replace(source, 0);
		 System.out.println(gScore);
		
	}
	private void initCameFrom() {
		/*Here in cameFrom */
		 cameFrom = new HashMap<Noeud,Noeud>();
		 for(int i=0;i<espace.length;i++) {
			 for(int j=0;j<espace.length;j++) {
				 cameFrom.put(espace[i][j],null);
			 }
		 }
	}
	private void initXY() {
		 for(int i=0;i<espace.length;i++) 
				for(int j=0;j<espace.length;j++)
				{
					espace[i][j].setX(i);
					espace[i][j].setY(j);
				}
	}
	public ArrayList<Noeud> search()
	 {
		if(source.equals(destination)) {System.out.println("SOURCE = DESTINATION"); return null;}
		else {
		 while(!toExplore.isEmpty()) {
			 /*Add the minimnum cost to visited or construct 
			  * the entire path if current node is the destination one*/
			 for(int compt=0;compt<toExplore.size();compt++) {
			 //System.out.println("HERE"+toExplore.get(compt)+toExplore.get(compt).getCost());
			 }
			 Collections.sort(toExplore);
			 //System.out.println("SORTED : "+toExplore);
			 if(toExplore.get(0).equals(destination))
				 return reconstruct_path(cameFrom,toExplore.get(0));
			 Noeud current = toExplore.get(0);
			 visited.add(toExplore.get(0));
			 toExplore.remove(0);
			 //System.out.println(current);
			 /*Explore neighbors*/
			 addsuccessors(current);
			 
		 }
		}
		 return visited;
	 }
	 private ArrayList<Noeud> reconstruct_path(HashMap<Noeud, Noeud> cameFrom2, Noeud noeud) {
		 ArrayList<Noeud> array = new ArrayList<Noeud>();
		 array.add(noeud);
		 while(cameFrom2.containsKey(noeud) && cameFrom.get(noeud) != null) {
			 noeud = cameFrom.get(noeud);
			 array.add(noeud);
		 }
		 System.out.println(array);
		 return array;
	}
	private void addsuccessors(Noeud noeud) {
		 int i=0,j=0;
		boolean trouve = false;
		for(i=0;i<espace.length && !trouve ;i++) 
			for(j=0;j<espace.length && !trouve;j++) {
				//System.out.print(espace[i][j]+ "VS" + noeud+"\t");
				
				if(espace[i][j].equals(noeud)) trouve=true;
			}
		//System.out.println("");
		i--;
		j--;
		//System.out.println(i+""+j);
		/*Explore neighbors ( max numbers = 4)*/
		Noeud [] neighbors = new Noeud[8];
		int k=0;
		if(j<espace.length-1) {
			neighbors[k] = espace[i][j+1]; 
			k++;
			if(!typeheuristic.equals("MANHATTAN")) {
				if(i<espace.length-1) {
					neighbors[k] = espace[i+1][j+1]; 
					k++;
				}if(i>0) {
					neighbors[k] = espace[i-1][j+1]; 
					k++;
				}
					}
		}if(j>0) {
			neighbors[k] = espace[i][j-1]; 
			k++;
			if(!typeheuristic.equals("MANHATTAN")) {
			if(i<espace.length-1) {
				neighbors[k] = espace[i+1][j-1]; 
				k++;
			}if(i>0) {
				neighbors[k] = espace[i-1][j-1]; 
				k++;
			}
			}
		}if(i<espace.length-1) {
			neighbors[k] = espace[i+1][j]; 
			k++;
		}if(i>0) {
			neighbors[k] = espace[i-1][j]; 
			k++;
		}
		
			
				for(int compt=0;compt<k;compt++) {
					if(!obstacle.contains(neighbors[compt])) {
					//System.out.print(neighbors[compt]+"\t");
					gScore.replace(neighbors[compt], gScore.get(neighbors[compt])+gScore.get(noeud));
					fscore.replace(neighbors[compt], gScore.get(noeud)+heuristic(neighbors[compt],destination));
					neighbors[compt].setCost(fscore.get(neighbors[compt]));
					if(visited.contains(neighbors[compt])) {
						//System.out.println("IN VISITED : "+neighbors[compt].getCost());
						continue;
					}
					int tentative = gScore.get(neighbors[compt]) + 1;
					if(!toExplore.contains(neighbors[compt])) { 
						//New Node
						cameFrom.replace(neighbors[compt],noeud);
						toExplore.add(neighbors[compt]);
						//System.out.println("NOT EXPLORED : "+neighbors[compt].getCost());
						
					}
					else if(tentative >= gScore.get(neighbors[compt])) {
						//System.out.println("EXPLORED BUT NOT VISITED 1 : "+neighbors[compt].getCost());
						continue;
					}
					else {
						//System.out.println("EXPLORED BUT NOT VISITED 2 : "+neighbors[compt].getCost());
						cameFrom.replace(neighbors[compt],noeud);
						gScore.replace(neighbors[compt], tentative);
						neighbors[compt].setCost(tentative);
						fscore.replace(neighbors[compt],gScore.get(neighbors[compt]) 
								+ heuristic(neighbors[compt],destination));
					}
					}
					repaint(); 
				}
				//System.out.println("");
		
	}

	public int heuristic(Noeud src,Noeud dest) {
		 int h = 0;
		 if(typeheuristic.equals("MANHATTAN"))
			 h = Math.abs(dest.getX()-src.getX())+Math.abs(dest.getY()-src.getY());
		 else if(typeheuristic.equals("PYTHAGORE"))
			 h = (int)Math.sqrt(Math.pow(dest.getX()-src.getX(), 2)+Math.pow(dest.getY()-src.getY(), 2));
		 else
			 h = Math.max(Math.abs(dest.getX()-src.getX()),Math.abs(dest.getY()-src.getY()));
		 return h;
	 }
	public Noeud getSource() {
		return source;
	}
	public void setSource(Noeud source) {
		this.source = source;
	}
	public Noeud getDestination() {
		return destination;
	}
	public void setDestination(Noeud destination) {
		this.destination = destination;
	}
	public Noeud [] [] getEspace() {
		return espace;
	}
	public ArrayList<Noeud> getBestpath() {
		return bestpath;
	}
	public void setBestpath(ArrayList<Noeud> bestpath) {
		this.bestpath = bestpath;
	}
	public ArrayList<Noeud> getObstacle() {
		return obstacle;
	}
	public void setObstacle(ArrayList<Noeud> obstacle) {
		this.obstacle = obstacle;
	}
	
	public void paintComponent(Graphics g) {
		/*The rectangles dimensions had been calculated depending of the app rendering*/
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		for(int i=0;i<spacelength;i++)
			for(int j=0;j<spacelength;j++) {
				g2d.setColor(Color.black);
				if(i==origin.getX() && j==origin.getY() &&clickedorg)
					g2d.setColor(Color.green);
				if(i==target.getX() && j==target.getY() &&clickedtar)
					g2d.setColor(Color.red);
				g2d.fillRect((this.getWidth()*(1-3/284))*j/(spacelength),(this.getHeight()*(1-28/313))*i/(spacelength),(this.getWidth()-14)/(spacelength),(this.getHeight()-33)/(spacelength));
			}
		/*Colorate obstacles with -coordonates in obsx and obsy- in black*/
		g2d.setColor(Color.white);
		for(int i=0;i<obsx.size();i++) {
			if(obsx.get(i)==origin.getX() && obsy.get(i)==origin.getY() || 
			obsx.get(i)==target.getX() && obsy.get(i)==target.getY())
				continue;
			g2d.fillRect((this.getWidth()*(1-3/284))*obsy.get(i)/spacelength, (this.getHeight()*(1-28/313))*obsx.get(i)/spacelength,
					(this.getWidth()-14)/(spacelength),(this.getHeight()-33)/(spacelength));
			
		}
		/*Colorate best path in  yellow if acivated = true*/
		if(activated) {
		g2d.setColor(Color.yellow);
		for(int i=0;i<bestpath.size();i++) {
			if(bestpath.get(i).getX()==origin.getX() && bestpath.get(i).getY()==origin.getY() || 
			   bestpath.get(i).getX()==target.getX() && bestpath.get(i).getY()==target.getY())
						continue;
			g2d.fillRect((this.getWidth()*(1-3/284))*bestpath.get(i).getY()/spacelength,  (this.getHeight()*(1-28/313))*bestpath.get(i).getX()/spacelength, 
					(this.getWidth()-14)/(spacelength),(this.getHeight()-33)/(spacelength));
		}
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub	
		int x = e.getX()*spacelength/(this.getWidth()*(1+6/284));
		int y = e.getY()*spacelength/(this.getHeight()*(1-23/313));
		if(x<spacelength && y<spacelength && (x!=origin.getX() || y!=origin.getY())&&!deplacetar
		&&!deplaceorg && (x!=target.getX() || y!=target.getY()) && action>1) {
		obsx.add(y);
		obsy.add(x);
		repaint();
		action++;
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX()*spacelength/(this.getWidth()*(1+6/284));
		int y = e.getY()*spacelength/(this.getHeight()*(1-23/313));
		switch(action) {
		case 0 : //put origin point 
			origin.setLocation(y, x);
			clickedorg = true;
			break;
		case 1 :
			if(y != origin.getX() || x!=origin.getY()) {
			target.setLocation(y, x);
			clickedtar = true;
			}else action--;
			break;
		default :
			if((y != origin.getX() || x!=origin.getY()) && (y != target.getX() || x!=target.getY())) {
			obsx.add(y);
			obsy.add(x);
			}
			break;
		}
		repaint();
		action++;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX()*spacelength/(this.getWidth()*(1+6/284));
		int y = e.getY()*spacelength/(this.getHeight()*(1-23/313));
		if(y == origin.getX() && x == origin.getY()&& action>1) {
			deplaceorg = true;
		}
		if(y == target.getX() && x == target.getY()&& action>1) {
			deplacetar = true;
		}
	}

	
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()*spacelength/(this.getWidth()*(1+6/284));
		int y = e.getY()*spacelength/(this.getHeight()*(1-23/313));
		if(deplaceorg == true && x<spacelength && y<spacelength)
		{
			origin.setLocation(y, x);
			repaint();
		}
		if(deplacetar == true && x<spacelength && y<spacelength)
		{
			target.setLocation(y, x);
			repaint();
		}
		deplaceorg = false;
		deplacetar = false;
	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(accueil.getBegin())) {
			accueil.setVisible(false);
			frame.add(this,BorderLayout.CENTER);
			frame.add(pan,BorderLayout.EAST);	
			frame.add(pan2,BorderLayout.SOUTH);
		}
		if(e.getSource().equals(animation) && clickedtar) {
			initiation((String)algo.getSelectedItem());
			if(!bestpath.get(0).equals(destination)) {
				ltime.setForeground(Color.red);
			ltime.setText("BLOCKED");
			go();
			}
			else {
				ltime.setForeground(Color.blue);
				ltime.setText(""+bestpath.size()+" s");
				
				/*Giving orders to the robot based on the best pasth
				 * L : Left
				 * R : Right
				 * T : Forward
				 * B : Backward
				 */
				ArrayList <String> steerings = new ArrayList <String> ();
				for(int i=bestpath.size()-1;i>0;i--) {
					Noeud currentnode = bestpath.get(i);
					Noeud nextnode = bestpath.get(i-1);
					if(currentnode.getX() == nextnode.getX() ) {
						if(currentnode.getY() < nextnode.getY()) steerings.add("R");
						else steerings.add("L");
							
					}
					else if (currentnode.getX() < nextnode.getX()) steerings.add("B");
					else steerings.add("T");
				}
				System.out.println("Command Lists :"+steerings);
				SendToTheRobot(steerings);
			}
		}
		if(e.getSource().equals(clear))
			clear();
		if(e.getSource().equals(newmaze))
			newmaze((int)grid.getSelectedItem());
	}
	private void SendToTheRobot(ArrayList<String> steerings) {
		 	PrintWriter out;
	        Socket socket;
	        try {
				socket = new Socket("10.3.22.82",8000);
				if(!socket.equals(null)) {
				out = new PrintWriter(socket.getOutputStream());
				for(int i=0;i<steerings.size();i++)
				{
					out.write(steerings.get(i));
					out.flush();
				}
				out.close();
				socket.close();
				}
			} catch (Exception e) {
			
			}
		
	}



	private Thread go() {
			Thread thread = new Thread(this);
			thread.start();
			return thread;
	}
	public void run() {
		int i = 0;
		while(i<99){
					if(i%2 == 0) ltime.setVisible(true);
					else ltime.setVisible(false);
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
				}
	}



	private void newmaze(int selectedItem) {
		clear();
		spacelength = selectedItem;
		repaint();
	}

	private void clear() {
		clickedorg = false;
		clickedtar = false;
		activated = false;
		action = 0;
		clearing(obsx);
		clearing(obsy);
		repaint();
	}



	private void clearing(ArrayList<Integer> list) {
			list.removeAll(list);
	}


	
}
