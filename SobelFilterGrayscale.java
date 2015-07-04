import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SobelFilterGrayscale {

    /**
     * Parameters of the incoming image.
     */
    private int width;
    private int height;

    private int[] pixels;
    /**
     * Keep last iterated column (left) and upper cell to be able to write result in the same array.
     */
    private int upperCell = 0;

    private int[][] leftColumn;
    /**
     * We should keep left column for calculations and build new left column (for next iteration) at the same time.
     */
    private int leftColumnRead = 0;

    private int leftColumnWrite = 1;
    private BufferedImage outputImage = null;

    /**
     * Open given image.
     *
     * @param file path to file
     *             
     * @throws IOException on reading file exception
     */
    SobelFilterGrayscale(FileInputStream file) throws IOException {
        BufferedImage inImg = ImageIO.read(file);

        width = inImg.getWidth();
        height = inImg.getHeight();
        pixels = inImg.getRaster().getPixels(0, 0, width, height, (int[])null);

        leftColumn = new int[2][height];
    }

    /**
     * Apply Sobel filter to the image.
     */
    public void applyFilter() {
        int i, j, G;
        for(i = 0 ; i < width ; i++ ) {
            for(j = 0 ; j < height ; j++ ) {
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
                    // border case
                    G = 0;
                } else {
                    G = sobelOperator(i, j);
                }

                // keep left column and upper cell
                upperCell = pixels[j * width + i];
                leftColumn[leftColumnWrite][j] = pixels[j * width + i];

                pixels[j * width + i] = G;
            }

            // switch reader and writer
            leftColumnRead = (leftColumnRead + 1) % 2;
            leftColumnWrite = (leftColumnWrite + 1) % 2;
        }
    }

    /**
     * Save the image to given path.
     *
     * @param outputFileName path t save
     *
     * @throws IOException if can not save the file to given path
     */
    public void save(String outputFileName) throws IOException {
        buildOutputImage();

        FileOutputStream outFile = new FileOutputStream(outputFileName);
        ImageIO.write(outputImage, "JPG", outFile);
    }

    /**
     * Show the image.
     */
    public void show() {
        JFrame TheFrame = new JFrame("Result");

        if (outputImage == null) {
            buildOutputImage();
        }

        JLabel TheLabel = new JLabel(new ImageIcon(outputImage));
        TheFrame.getContentPane().add(TheLabel);

        TheFrame.setSize(width, height);
        TheFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        TheFrame.setVisible(true);
    }

    /**
     * Apply Sobel operator for one pixel of picture.
     *
     * @param i width coordinates of the pixel
     * @param j height coordinates of the pixel
     *
     * @return new value of the pixel
     */
    private int sobelOperator(int i, int j) {
        double Gx =
            pixels[(j - 1) * width + i + 1] + 2 * pixels[j * width + i + 1] + pixels[(j + 1) * width + i + 1] -
            leftColumn[leftColumnRead][j - 1] - 2 * leftColumn[leftColumnRead][j] - leftColumn[leftColumnRead][j + 1];
        double Gy =
            leftColumn[leftColumnRead][j + 1] + 2 * pixels[(j + 1) * width + i] + pixels[(j + 1) * width + i + 1] -
            leftColumn[leftColumnRead][j-1] - 2 * upperCell - pixels[(j - 1) * width + i + 1];

        return (int)(Math.hypot(Gx, Gy)/8);
    }

    /**
     * Build output image to save or show it.
     */
    private void buildOutputImage() {
        outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        outputImage.getRaster().setPixels(0, 0, width, height, pixels);
    }

}
