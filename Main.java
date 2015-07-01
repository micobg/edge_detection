import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        int     i, j;
        FileInputStream inFile = new FileInputStream(args[0]);
        BufferedImage inImg = ImageIO.read(inFile);
        int width = inImg.getWidth();
        int height = inImg.getHeight();
        int[] output = new int[width*height];
        int[] pixels = inImg.getRaster().getPixels(0,0,width,height,(int[])null);

        double Gx;
        double Gy;
        double G;

        for(i = 0 ; i < width ; i++ )
        {
            for(j = 0 ; j < height ; j++ )
            {
                if (i==0 || i==width-1 || j==0 || j==height-1)
                    G = 0; // Image boundary cleared
                else{
                    Gx = pixels[(i+1)*height + j-1] + 2*pixels[(i+1)*height +j] + pixels[(i+1)*height +j+1] -
                            pixels[(i-1)*height +j-1] - 2*pixels[(i-1)*height+j] - pixels[(i-1)*height+j+1];
                    Gy = pixels[(i-1)*height+j+1] + 2*pixels[i*height +j+1] + pixels[(i+1)*height+j+1] -
                            pixels[(i-1)*height+j-1] - 2*pixels[i*height+j-1] - pixels[(i+1)*height+j-1];
                    G  = Math.hypot(Gx, Gy);
                }

                output[i*height+j] = (int)G;
            }
        }


        BufferedImage outImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        outImg.getRaster().setPixels(0,0,width,height,output);
        FileOutputStream outFile = new FileOutputStream("result.jpg");
        ImageIO.write(outImg,"JPG",outFile);

        JFrame TheFrame = new JFrame("Result");

        JLabel TheLabel = new JLabel(new ImageIcon(outImg));
        TheFrame.getContentPane().add(TheLabel);


        TheFrame.setSize(width, height);

        TheFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        TheFrame.setVisible(true);




        return;


//
//        File file = new File(args[0]);
//        try {
//            BufferedImage image = ImageIO.read(file);
////            int[][] result = imageToIntArray(image);
//            imageToByteArray(file);
//        } catch (IOException e) {
//            System.err.println("Cannot read the file.");
//        }
    }

    private static int[][] imageToIntArray(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;

                if (argb != -1) {
                    System.out.println(argb);
                }
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static byte[] imageToByteArray(File image) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            System.err.println("Cannot read the file.");
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }
}