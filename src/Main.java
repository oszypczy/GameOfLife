import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        System.out.println("Width: " + width + ", Height: " + height);
        SwingUtilities.invokeLater(() -> new Game(width, height));
    }
}
