package itag;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author s0831408 and s0789266
 */
public class Panel extends JPanel implements MouseListener, Serializable{

	private static final long serialVersionUID = 1L;

	public BufferedImage image;
	
	public ArrayList<Point> currentTag = null;
	
	public ArrayList<ArrayList<Point>> tagsList;
        public ArrayList<String> tagColorList = new ArrayList<String>();
        public ArrayList<Color> colorArray = new ArrayList<Color>();
        
        public Color defaultColor = Color.GREEN;
        
        @Override
	public void paint(Graphics g) {
                super.paint(g);
		
		//display iamge
		showImage(g);
		
		//display all the completed tags
		for(ArrayList<Point> tag : tagsList) {
			drawTag(tag);
			finishTag(tag);
		}
		
		//display current tag
		drawTag(currentTag);
	}
	
	public Panel() {
		currentTag = new ArrayList<Point>();
		tagsList = new ArrayList<ArrayList<Point>>();

		this.setVisible(true);

		Dimension panelSize = new Dimension(640, 480);
		this.setSize(panelSize);
		this.setMinimumSize(panelSize);
		this.setPreferredSize(panelSize);
		this.setMaximumSize(panelSize);
		addMouseListener(this);
	}

	public Panel(File fileName) throws Exception{
		this();
                image = ImageIO.read(new File(fileName.getPath()));
                if (image.getWidth() > 640 || image.getHeight() > 480) {
                    int newWidth = image.getWidth() > 640 ? 640 : (image.getWidth() * 480)/image.getHeight();
                    int newHeight = image.getHeight() > 480 ? 480 : (image.getHeight() * 640)/image.getWidth();
                    System.out.println("SCALING TO " + newWidth + "x" + newHeight );
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
                    image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    image.getGraphics().drawImage(scaledImage, 0, 0, this);
                }
	}

	public void showImage(Graphics g) {
            g.drawImage(image, 0, 0, null);
	}
               
        /**
	 * displays a tag without last stroke
	 * @param tag to be displayed
	 */
	public void drawTag(ArrayList<Point> tag) {
                Graphics2D g = (Graphics2D)this.getGraphics();
                g.setStroke(new BasicStroke(1));
		g.setColor(defaultColor);
		for(int i = 0; i < tag.size(); i++) {
			Point currentVertex = tag.get(i);
			if (i != 0) {
				Point prevVertex = tag.get(i - 1);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			g.fillOval(currentVertex.getX() - 5, currentVertex.getY() - 5, 10, 10);
		}
	}
        
        public void drawThickTag(ArrayList<Point> tag) {
                Graphics2D g = (Graphics2D)this.getGraphics();
                g.setStroke(new BasicStroke(8));
		g.setColor(defaultColor);
		for(int i = 0; i < tag.size(); i++) {
			Point currentVertex = tag.get(i);
			if (i != 0) {
				Point prevVertex = tag.get(i - 1);
				g.drawLine(prevVertex.getX(), prevVertex.getY(), currentVertex.getX(), currentVertex.getY());
			}
			g.fillOval(currentVertex.getX() - 5, currentVertex.getY() - 5, 10, 10);
		}
	}

        
	public void finishTag(ArrayList<Point> tag) {
		//if there are less than 3 vertices than nothing to be completed
		if (tag.size() >= 3) {
			Point firstVertex = tag.get(0);
			Point lastVertex = tag.get(tag.size() - 1);
		
			Graphics2D g = (Graphics2D)this.getGraphics();
                        g.setStroke(new BasicStroke(1));
			g.setColor(defaultColor);
			g.drawLine(firstVertex.getX(), firstVertex.getY(), lastVertex.getX(), lastVertex.getY());
		}
	}
        
        public void finishThickTag(ArrayList<Point> tag) {
		//if there are less than 3 vertices than nothing to be completed
		if (tag.size() >= 3) {
			Point firstVertex = tag.get(0);
			Point lastVertex = tag.get(tag.size() - 1);
		
			Graphics2D g = (Graphics2D)this.getGraphics();
                        g.setStroke(new BasicStroke(8));
			g.setColor(defaultColor);
			g.drawLine(firstVertex.getX(), firstVertex.getY(), lastVertex.getX(), lastVertex.getY());
		}
	}
	
	public void addNewTag() {
		//finish the current tag if any
		if (currentTag != null ) {
			finishTag(currentTag);
			tagsList.add(currentTag);
		}
		
		currentTag = new ArrayList<Point>();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		//check if the cursos withing image area
		if (x > image.getWidth() || y > image.getHeight()) {
			//if not do nothing
			return;
		}
		
		Graphics2D g = (Graphics2D)this.getGraphics();
		
		//if the left button than we will add a vertex to poly
		if (e.getButton() == MouseEvent.BUTTON1) {
			g.setColor(defaultColor);
			if (currentTag.size() != 0) {
				Point lastVertex = currentTag.get(currentTag.size() - 1);
				g.drawLine(lastVertex.getX(), lastVertex.getY(), x, y);
			}
			g.fillOval(x-5,y-5,10,10);
			
			currentTag.add(new Point(x,y));
			System.out.println(x + " " + y);
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
