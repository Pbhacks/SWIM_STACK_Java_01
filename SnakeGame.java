import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;

    private LinkedList<Point> snake;
    private Point food;
    private char direction;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 'R'; // Initially, the snake moves to the right

        spawnFood();

        Timer timer = new Timer(150, this); // Adjust the delay for snake speed
        timer.start();

        addKeyListener(this);
        setFocusable(true);

        // Set the background color of the content pane to black
        getContentPane().setBackground(Color.BLACK);
    }

    @Override
    public void paint(Graphics g) {
        // Override the paint method to prevent flickering
        super.paint(g);
        drawGame(g);
    }

    @Override
    public void update(Graphics g) {
        // Override the update method to implement double-buffering
        Image offScreenBuffer = createImage(getWidth(), getHeight());
        Graphics offScreenGraphics = offScreenBuffer.getGraphics();
        offScreenGraphics.setColor(getBackground());
        offScreenGraphics.fillRect(0, 0, getWidth(), getHeight());
        offScreenGraphics.setColor(getForeground());
        paint(offScreenGraphics);
        g.drawImage(offScreenBuffer, 0, 0, this);
    }

    private void drawGame(Graphics g) {
        // Draw the snake
        for (Point point : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public void actionPerformed(ActionEvent e) {
        move();
        checkCollision();
        checkFood();
        repaint();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, head.y - 1);
                break;
            case 'D':
                newHead = new Point(head.x, head.y + 1);
                break;
            case 'L':
                newHead = new Point(head.x - 1, head.y);
                break;
            default:
                newHead = new Point(head.x + 1, head.y);
        }

        snake.addFirst(newHead);

        // Check if the snake ate itself
        if (snake.size() > 1 && snake.subList(1, snake.size()).contains(newHead)) {
            gameOver();
        }

        // Check if the snake collided with the walls
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE) {
            gameOver();
        }

        // Check if the snake ate the food
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollision() {
        // Check if the head collides with the body or the walls
        Point head = snake.getFirst();

        if (snake.size() > 1 && snake.subList(1, snake.size()).contains(head)) {
            gameOver();
        }

        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            gameOver();
        }
    }

    private void checkFood() {
        // Check if the snake ate the food
        if (snake.getFirst().equals(food)) {
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;

        // Generate a random position for the food
        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over!");
        System.exit(0);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame().setVisible(true));
    }
}
