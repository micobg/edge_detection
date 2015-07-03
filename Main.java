import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Missing parameters.");
            return;
        }

        FileInputStream file = new FileInputStream(args[1]);
        SobelFilterGrayscale sobelFilterGrayscale = new SobelFilterGrayscale(file);

        if(args[0].equals("--print")) {
            sobelFilterGrayscale.enablePrintChanges();
        }

        sobelFilterGrayscale.applyFilter();

        if (args.length > 2) {
            sobelFilterGrayscale.save(args[2]);
        }

        sobelFilterGrayscale.show();
    }
}