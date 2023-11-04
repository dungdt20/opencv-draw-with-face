package opencv;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class ObjectDetection {

    private Mat getFrameGray(Mat frame) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(frameGray, frameGray);

        return frameGray;
    }

    private List<Rect> detectAndDisplayFace(
            Mat frame,
            Mat frameGray,
            List<Rect> listOfOldFaces,
            CascadeClassifier faceCascade
    ) {
        // -- Detect faces
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(frameGray, faces);
        List<Rect> listOfNewFaces = faces.toList();

        for (Rect face : listOfNewFaces) {
            Imgproc.rectangle(frame, face, new Scalar(0, 0, 255), 2);
        }

        // -- Draw with middle of faces
        listOfOldFaces.addAll(listOfNewFaces);

        for (Rect face : listOfOldFaces) {
            Point faceCenter = new Point(face.x + (double) face.width / 2, face.y + (double) face.height / 2);
            Imgproc.circle(frame, faceCenter, 4, new Scalar(212, 245, 66), 5);
        }

        return listOfNewFaces;
    }

    private void detectAndDisplayEyes(
            Mat frame,
            Mat frameGray,
            Rect face,
            CascadeClassifier eyesCascade
    ) {
        Mat faceROI = frameGray.submat(face);
        // -- In each face, detect eyes
        MatOfRect eyes = new MatOfRect();
        eyesCascade.detectMultiScale(faceROI, eyes);
        List<Rect> listOfEyes = eyes.toList();

        for (Rect eye : listOfEyes) {
            Point eyeCenter = new Point(face.x + eye.x + (double) eye.width / 2, face.y + eye.y + (double) eye.height / 2);
            int radius = (int) Math.round((eye.width + eye.height) * 0.25);
            Imgproc.circle(frame, eyeCenter, radius, new Scalar(255, 0, 0), 4);
        }
    }

    public void run() {
        String filenameFaceCascade = "data/haarcascade_frontalface_alt2.xml";
        String filenameEyesCascade = "data/haarcascade_eye_tree_eyeglasses.xml";
        CascadeClassifier faceCascade = new CascadeClassifier();
        CascadeClassifier eyesCascade = new CascadeClassifier();

        if (!faceCascade.load(filenameFaceCascade)) {
            System.err.println("--(!)Error loading face cascade: " + filenameFaceCascade);
            System.exit(0);
        }
        if (!eyesCascade.load(filenameEyesCascade)) {
            System.err.println("--(!)Error loading eyes cascade: " + filenameEyesCascade);
            System.exit(0);
        }
        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.err.println("--(!)Error opening video capture");
            System.exit(0);
        }

        Mat frame = new Mat();
        List<Rect> listOfFaces = new ArrayList<>();

        while (capture.read(frame)) {
            if (frame.empty()) {
                System.err.println("--(!) No captured frame -- Break!");
                break;
            }
            //-- Apply the classifier to the frame
            Mat frameGray = getFrameGray(frame);
            List<Rect> listOfNewFaces = detectAndDisplayFace(frame, frameGray, listOfFaces, faceCascade);
            listOfFaces.addAll(listOfNewFaces);
            listOfNewFaces.forEach(face -> detectAndDisplayEyes(frame, frameGray, face, eyesCascade));

            //-- Show what you got
            Core.flip(frame, frame, 1);
            HighGui.imshow("Capture - Face detection",  frame);
            if (HighGui.waitKey(10) == 27) {
                break;
            }
        }
        System.exit(0);
    }
}
