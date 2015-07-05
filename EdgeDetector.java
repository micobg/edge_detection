import java.io.IOException;

public interface EdgeDetector {

    /**
     * Apply Sobel filter to the image.
     */
    void applyFilter();

    /**
     * Save the image to given path.
     *
     * @param outputFileName path t save
     *
     * @throws IOException if can not save the file to given path
     */
    void save(String outputFileName) throws IOException;

    /**
     * Show the image.
     */
    void show();

}
