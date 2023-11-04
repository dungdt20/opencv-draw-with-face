package opencv;

import nu.pattern.OpenCV;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        OpenCV.loadLocally();

        EventQueue.invokeLater(() -> {
            new Thread(() -> new ObjectDetection().run()).start();
        });
    }
}

