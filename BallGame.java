import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BallGame extends JFrame implements Runnable, KeyListener, MouseListener {
	
	private static final long serialVersionUID = -4561387616954048688L;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 625;
	private int direccion;
	private int vidas;
	private int velX; //
	private int velY; //
	private int difX = 5; //
	private int difY = 6; //
	private int min; //
	private int seg; //
	private int posX; //
	private int posY; //
	private Image dbImage;
	private Image fondo;
	private Graphics dbg;
	private Pelota ball;
	private Canasta basket;
	private animacion animBall;
	private long tiempoActual;
	private boolean empieza;
	
	
	public BallGame() {
		
		empieza = false;
		vidas = 5;
		direccion = 0;
		fondo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/fondo.jpg")); // fondo del JFrame
		posX = 0; // posX de la pelota
		posY = HEIGHT - 70; // posY de la pelota
		int posCanX = (WIDTH / 4) * 3;
		int posCanY = (HEIGHT / 4) * 3;
		
		velX = (int) (Math.random() * difX) + 10; // 
		velY = -( (int)(Math.random() * difY) + 30); //
		
		
		Image bola1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/basketball.gif"));
		Image bola2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/basketball2.gif"));
		Image bola3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/basketball3.gif"));
		Image bola4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/basketball4.gif"));
		
		animBall = new animacion();
		animBall.sumaCuadro(bola1, 100);
		animBall.sumaCuadro(bola2, 100);
		animBall.sumaCuadro(bola3, 100);
		animBall.sumaCuadro(bola4, 100);
		
		ball = new Pelota(posX, posY, bola1);
		
		Image  canasta = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/canasta.gif"));
		basket = new Canasta(posCanX, posCanY, canasta);
		
		addKeyListener(this);
		addMouseListener(this);
		
		Thread t = new Thread(this);
		t.start();
		
	}
	
	public void run() {
		
		tiempoActual = System.currentTimeMillis();
		while(vidas > 0) {
		
			actualiza();
			repaint();
			checaColision();
		
			try	{
				// El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
	
	public void actualiza() {

		long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
		tiempoActual += tiempoTranscurrido;
		animBall.actualiza(tiempoTranscurrido);
	
		switch (direccion) { // Para actualizar la direccion del cofre
			
			case 1: { // izquierda
				
				basket.setPosX(basket.getPosX() - 5);
				break;
			}
			
			case 2: { // derecha
				
				basket.setPosX(basket.getPosX() + 5);
				break;
			}
		}
		
		if (empieza) {
			velY++; //
			ball.setPosX(ball.getPosX() + velX); //
			ball.setPosY(ball.getPosY() + velY); //
		}
		
		if(ball.getPosY() > HEIGHT){ // start
			ball.setPosX(posX);
			ball.setPosY(posY);
			velX = (int) (Math.random() * difX) + 10;
			velY = - ((int) (Math.random() * difY) + 30);
		} // termina

	}
	
	public void checaColision() {
				
		switch (direccion) { // Para que no se salga de la pantalla
			
			case 1: { // Se mueve a la izquierda
				
				if (basket.getPosX() < getWidth() / 2) { // Que no pase de la mitad de la pantalla
					
					basket.setPosX(basket.getPosX() + 5);
				}
				break;
			}
			
			case 2: { // Se mueve a la derecha
				
				if (basket.getPosX() + basket.getAncho() > getWidth()) { // Que no se pase del Frame

					basket.setPosX(basket.getPosX() - 5);
				}
				break;
			}
		}
		
		if (ball.intersecta(basket)) {
			
			ball.setPosX(posX);
			ball.setPosY(posY);
			empieza = false;
		}
		
	}
	
	public void paint(Graphics g) {
		
		if(dbImage == null) {
			
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor(getForeground());
		paint1(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paint1(Graphics g) {
		
		g.drawImage(fondo, 0, 0, getSize().width, getSize().height, this);
				
		if(ball != null) {
			
			g.drawImage(animBall.getImagen(), ball.getPosX(), ball.getPosY(), this);
			g.drawImage(basket.getImagenI(), basket.getPosX(), basket.getPosY(), this);
		}
		
		else {
			
			g.drawString("No se cargo la imagen...", 20, 20);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si se presiona la flecha izquierda
			
			direccion = 1;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si se presiona la flecha derecha
		
			direccion = 2;
		}
		
	}
	
	public void keyTyped(KeyEvent e) {
	
	}
	
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Si se deja de presionar la flecha derecha
			
			direccion = 0;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Si se deja de presionar la flecha izquierda
		
			direccion = 0;
		}
	}

	public boolean estaDentro(MouseEvent e) {
		
		if ((e.getX() > ball.getPosX()) && (e.getX() < ball.getPosX() + ball.getAncho()) 
			&& (e.getY() > ball.getPosY()) && (e.getY() < ball.getPosY() + ball.getAlto())) {
			
			return true;	
		}
		
		else {
			
			return false;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
 
		if (e.getButton() == MouseEvent.BUTTON1 && estaDentro(e)) {

			empieza = true;
		}
	}

	/**
	 * Metodo <I>mouseClicked</I> sobrescrito de la interface <code>MouseMotionListener</code>.<P>
	*/

	public void mouseEntered(MouseEvent e) {
		
	}

	/**
	 * Metodo <I>mouseExited</I> sobrescrito de la interface <code>MouseMotionListener</code>.<P>
	*/

	public void mouseExited(MouseEvent e) {
		
	}

	/**
	 * Metodo <I>mousePressed</I> sobrescrito de la interface <code>MouseMotionListener</code>.<P>
	*/

	public void mousePressed(MouseEvent e) {
		
	}

	/**
	 * Metodo <I>mouseReleased</I> sobrescrito de la interface <code>MouseMotionListener</code>.<P>
	*/

	public void mouseReleased(MouseEvent e) {

	}
	
	public static void main (String args[]) {
		
		BallGame juego = new BallGame();
		juego.setSize(WIDTH, HEIGHT);
		juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego.setVisible(true);
	}
}
