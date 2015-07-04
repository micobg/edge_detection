import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Missing parameters.");
            return;
        }

        FileInputStream file = new FileInputStream(args[0]);
        SobelFilterGrayscale sobelFilterGrayscale = new SobelFilterGrayscale(file);

        sobelFilterGrayscale.applyFilter();

        if (args.length > 1) {
            sobelFilterGrayscale.save(args[1]);
        }

        sobelFilterGrayscale.show();
    }
}