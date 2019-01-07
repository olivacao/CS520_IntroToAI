import javax.swing.*;
import java.awt.*;

public class MazeDrawer {

    public void drawMaze(int[][] maze,String title) {
            JFrame frame = new JFrame(title);
            frame.setSize(maze.length*50, maze.length*50);

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(true);

            JPanel panel = new JPanel() {
                private static final long serialVersionUID = 1L;
                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze.length; j++) {
                            if (maze[i][j] == 0) {
                                g.setColor(Color.WHITE);
                            } else if (maze[i][j] == 1) {
                                g.setColor(Color.BLACK);

                            } else if (maze[i][j] == 8) {
                                g.setColor(Color.RED);
                            }
                            g.fillRect(j*50,i*50,50,50);
                        }
                    }
                }
            };
            frame.setContentPane(panel);

    }
}
