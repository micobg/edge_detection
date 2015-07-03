import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        FileInputStream file = new FileInputStream(args[0]);

        SobelFilterGrayscale sobelFilterGrayscale = new SobelFilterGrayscale(file);
        sobelFilterGrayscale.enablePrintChanges();
        sobelFilterGrayscale.applyFilter();

        if (args.length > 1) {
            sobelFilterGrayscale.save(args[1]);
        }

        sobelFilterGrayscale.show();
    }
}