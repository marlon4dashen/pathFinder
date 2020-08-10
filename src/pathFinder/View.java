package pathFinder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View extends JFrame {
	
	private static Graph graph;
	private static JPanel choiceBar;
	private final static int DEMOSIZE = 700;
	private static JLabel alg = new JLabel("Algorithm: ");
	private static String [] algorithms = {"Dijkstra", "A*", "DFS", "BFS"};
	private static JComboBox<String> algos = new JComboBox<String>(algorithms); 
	private static JLabel framesize = new JLabel("Size: ");
	private static JSlider size = new JSlider(20, 40);
	private static JButton clear = new JButton("Clear");
	private static JButton start = new JButton("Start");
	private static JButton random = new JButton("Random Generate");
	private static JLabel no_path = new JLabel("No path is available.");
	private static int space = 3;
	private static int sq_size;
	private static int mx = -100;
	private static int my = -100;
	private static int dx = -100;
	private static int dy = -100;
	private static int cx = -100;
	private static int cy = -100;
	private static boolean first = false;
	private static boolean both = false;
	private boolean inDrag = false;
	private static Squares[][] map;
	private static LinkedList<Squares> seq;
	private static ArrayList<ArrayList<Integer>> paths;
	private static Timer timer;
	private static ArrayList<Point> obstacle;
	private static Stack<Squares> found_path;
	private boolean randomGen = false;
	private final double OBSTACLE_PERCENTAGE = 0.3;
	
	public View() {
		paths = new ArrayList<ArrayList<Integer>>();
		obstacle = new ArrayList<Point>();
		found_path = new Stack<Squares>();
		this.setTitle("Pathfinding Visualization");
		this.setSize(DEMOSIZE, DEMOSIZE);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		Click click = new Click();
		this.addMouseListener(click);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		//Setting up choice bar
		
		choiceBar = new JPanel();
		choiceBar.setSize(200, 800);
		choiceBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1),"Controls"));
		choiceBar.setBounds(10,10,200,550);
		choiceBar.setLayout(null);

		int space = 20;
		alg.setBounds(20, space, 100, 40);
		choiceBar.add(alg);
		space += 30;
		
		algos.setBounds(20, space, 100, 40);
		choiceBar.add(algos);
		space += 40;
		
		framesize.setBounds(20, space, 100, 40);
		choiceBar.add(framesize);
		space += 30;
		
		size.setBounds(12, space, 180, 40);
		size.setMajorTickSpacing(4);
		size.setMinorTickSpacing(1);
		size.setPaintTicks(true);
		size.setPaintLabels(true);
		choiceBar.add(size);
		space += 100;
		
		no_path.setBounds(20, space, 150, 40);
		no_path.setForeground(Color.RED);
		no_path.setVisible(false);
		choiceBar.add(no_path);
		space += 60;
		
		start.setBounds(20,space,100,40);
		choiceBar.add(start);
		space += 60;
		
		clear.setBounds(20,space,100,40);
		choiceBar.add(clear);
		space += 60;
		
		random.setBounds(20,space,150,40);
		choiceBar.add(random);
		space += 30;
		
		this.add(choiceBar);
		

		//Add board
		Board board = new Board();
		this.add(board);
		
		
		//Event Listener for slider
		sq_size = size.getValue() - space;
		size.addChangeListener(new ChangeListener() {
			final int space = 3;
			@Override
			public void stateChanged(ChangeEvent e) {
				sq_size = size.getValue() - space;
				repaint();
			}
			
		});
		
		
		//Event Listener for clear
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mx = -100;
				my = -100;
				dx = -100;
				dy = -100;
				first = false;
				both = false;
				found_path.clear();
				paths.clear();
				no_path.setVisible(false);
				obstacle.clear();
				randomGen =false;
				repaint();
				
			}
			
		});
		
		//Event Listener for start
		start.addActionListener(new ActionListener() {
			
			final int space = 3;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mx != -100 && my != -100 && dx != -100 && dy != -100) {
					graph = createGraph();
					
					int start_x = mx / (space + sq_size);
					int start_y = my / (space + sq_size);
					
					Squares start_sq = map[start_y][start_x];
					
					int end_x = dx / (space + sq_size);
					int end_y = dy / (space + sq_size);
					
					Squares end_sq = map[end_y][end_x];
					
					if((String)algos.getSelectedItem() == "DFS")
						graph.DFS(start_sq, end_sq);
					else if((String)algos.getSelectedItem() == "BFS")
						graph.BFS(start_sq, end_sq);
					else if((String)algos.getSelectedItem() == "Dijkstra")
						graph.Dijkstra(start_sq, end_sq);
					else
						graph.A_Star(start_sq, end_sq);
					
					seq = graph.getSeq();
					
					timer = new Timer(50, new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							
							if(seq.size() == 0) {
								found_path = graph.getPath();
								repaint();
								if(found_path.empty())
									no_path.setVisible(true);
								System.out.println(paths);
								((Timer)e.getSource()).stop();
							}else {
								ArrayList<Integer> tmp = new ArrayList<Integer>();
								
								Squares sq = seq.removeFirst();
								tmp.add(sq.getX());
								tmp.add(sq.getY());
								paths.add(tmp);
								repaint();
							}
						}
						
						

					});
					
					timer.setRepeats(true);
					timer.start();
				}
				
				
				
			}
			
			
		});
		
		random.addActionListener(new ActionListener() {

			private boolean flag = false;
			private int space = 3;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(both) {
					
					randomGen = true;
					int x_step = 400 / (space + sq_size);
					int y_step = 600 / (space + sq_size);
					Random random = new Random();

					while(obstacle.size() < x_step * y_step * OBSTACLE_PERCENTAGE) {
						flag = false;
						int x = random.nextInt(x_step);
						int y = random.nextInt(y_step);
						Point p = new Point(x, y);
						for(int j = 0; j < obstacle.size(); j++) {
								if(obstacle.get(j).equals(p)) {
									flag = true;
							}
						}
						
						int start_x = mx / (space + sq_size);
						int start_y = my / (space + sq_size);
						int end_x = dx / (space + sq_size);
						int end_y = dy / (space + sq_size);
						
						if(!flag && p.getX() != start_x && p.getX() != end_x && p.getY() != start_y && p.getY() != end_y) {
							obstacle.add(p);
						}
						
					}
					repaint();
				}
				
			}
			
		});
		
		this.setVisible(true);
		
	}
	
	private Graph createGraph() {
		
		int x_step = 400 / (sq_size + space);
		int y_step = 600 / (sq_size + space);
		
		Graph g = new Graph(x_step * y_step);
		map = new Squares[y_step][x_step];
		
		for(int i = 0; i < y_step; i++) {
			for(int j = 0; j < x_step; j++) {
				map[i][j] = new Squares(i, j, x_step);
			}
		}
		
		Squares [] v = new Squares[x_step * y_step];
		for(int i = 0; i < y_step; i++) {
			for(int j = 0; j < x_step; j++) {
				v[i * x_step + j] = map[i][j];
			}
		}
		g.setVertices(v);
		
		for(int i = 0; i < y_step; i++) {
			for(int j = 0; j < x_step; j++) {
				if(j == x_step - 1 && i == y_step - 1) {
					continue;
				}else if(j == x_step - 1) {
					g.addEdge(map[i][j], map[i + 1][j]);
				}else if(i == y_step - 1) {
					g.addEdge(map[i][j], map[i][j + 1]);
				}else {
					g.addEdge(map[i][j], map[i + 1][j]);
					g.addEdge(map[i][j], map[i][j + 1]);
				}
			}
		}
		
		for(int i = 0; i < obstacle.size(); i++) {
			
			Point p = obstacle.get(i);
			Squares tmp = new Squares(p.getY(), p.getX(), x_step);
			g.removeVertices(tmp);
		}

		return g;
	}
	
	
	public class Board extends JPanel{
		
		public Board(){
			this.setSize(400, 600);
			this.setBounds(250, 20, 403, 603);
		}

		public void paintComponent(Graphics g) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 403, 603);
			
			int x_step = 400 / (sq_size + space);
			int y_step = 600 / (sq_size + space);
			
			for(int i = 0; i < x_step; i++) {
				for(int j = 0; j < y_step; j++) {
					g.setColor(Color.WHITE);
					if(mx >= i * sq_size + space * (i + 1) && mx  < i * sq_size + space * (i + 1) + sq_size && my  >= j * sq_size + space * (j + 1) && my <= j * sq_size + space * (j + 1) + sq_size) {
						g.setColor(Color.RED);
					}
					if(dx >= i * sq_size + space * (i + 1) && dx  < i * sq_size + space * (i + 1) + sq_size && dy  >= j * sq_size + space * (j + 1) && dy <= j * sq_size + space * (j + 1) + sq_size) {
						g.setColor(Color.GREEN);
					}
					if(cx >= i * sq_size + space * (i + 1) && cx  < i * sq_size + space * (i + 1) + sq_size && cy  >= j * sq_size + space * (j + 1) && cy <= j * sq_size + space * (j + 1) + sq_size) {
						g.setColor(Color.BLUE);
					}
					
					
					for(int a = 0; a < obstacle.size(); a++) {
						if(obstacle.get(a).getX() == i && obstacle.get(a).getY() == j) {
							g.setColor(Color.PINK);
							break;
						}
					}

						
					g.fillRect(i * sq_size + space * (i + 1), j * sq_size + space * (j + 1), sq_size, sq_size);
					
				}
			}
			
			for(int i = 0; i < paths.size(); i++) {
				g.setColor(Color.YELLOW);
				int x = paths.get(i).get(0);
				int y = paths.get(i).get(1);
				
				g.fillRect(x * sq_size + space * (x + 1), y * sq_size + space * (y + 1), sq_size, sq_size);
			}
			
			if(found_path != null) {
				for(Squares s: found_path) {
					g.setColor(Color.BLUE);
					
					int x = s.getX();
					int y = s.getY();
					g.fillRect(x * sq_size + space * (x + 1), y * sq_size + space * (y + 1), sq_size, sq_size);
				}
				
				
			}

		}
		
	
	}
	
	public class Click implements MouseListener{
		
		

		@Override
		public void mouseClicked(MouseEvent e) {
			if(first) {
				dx = e.getX() - 258;
				dy = e.getY() - 50;
				first = false;
				both = true;
				repaint();
 			}else {
				mx = e.getX() - 258;
				my = e.getY() - 50;
				first = true;
				repaint();
			}

			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(both && !randomGen) {
				inDrag = true;
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(both && !randomGen) {
				inDrag = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class Move implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			
			boolean flag = false;
			
			int x = e.getX() - 258;
			int y = e.getY() - 50;
			
			

			if(inDrag) {
				
				int px = x / (space + sq_size);
				int py = y / (space + sq_size); 
				int x_step = 400 / (sq_size + space);
				int y_step = 600 / (sq_size + space);
		
				Point p = new Point(px, py);
				for(int i = 0; i < obstacle.size(); i++) {
					if(obstacle.get(i).equals(p)) {
						flag = true;
					}
				}
				if(!flag && px < x_step && py < y_step) {
					obstacle.add(p);
				}
				repaint();
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			cx = e.getX() - 258;
			cy = e.getY() - 50;
			repaint();
		}
		
	}
		

	
	public static void main(String[] args) {
		
		View v1 = new View();
		
	}
	
	
}
