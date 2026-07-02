package com.example.looklost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import FaceMatch.IMREAD_GRAYSCALE;

public class FaceMatch extends AppCompatActivity {
    private static final int IMREAD_GRAYSCALE = 0;
    private ImageView imageView;
    private Button matchButton;
    private RecyclerView recyclerView;
    private AdapterToShowPersons personAdapter;
    private List<PersonModel> personList;

    private DatabaseReference databaseReference;

    private static final int FACE_WIDTH = 128;
    private static final int FACE_HEIGHT = 128;

//    static {
//        if (!OpenCVLoader.initDebug()) {
//            // Handle initialization error
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match);

        imageView = findViewById(R.id.image_view);
        matchButton = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recycler_view);

        personList = new ArrayList<>();
        personAdapter = new AdapterToShowPersons((Context) personList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(personAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("persons");

        matchButton.setOnClickListener(new View.OnClickListener() {
            class Mat {
            }

            @Override
            public void onClick(View v) {
                // Get the input image as a Bitmap
                Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_image);

                // Convert the Bitmap to OpenCV Mat format
                Mat inputMat = new Mat();
                Object Utils = null;
//                Objects.requireNonNull(Utils).wait(inputBitmap, inputMat);

                // Preprocess the input image for face matching
                Mat processedMat = preprocessImage(inputMat);

                // Perform face matching
                List<PersonModel> matchedPersons = performFaceMatching(processedMat);

                // Display the matched persons in the RecyclerView
                displayMatchedPersons(matchedPersons);
            }

            private List<PersonModel> performFaceMatching(Mat processedMat) {
                return null;
            }

            private Mat preprocessImage(Mat inputMat) {
                return null;
            }
        });
    }

    private Mat preprocessImage(Mat inputMat) {
        // Convert the input image to grayscale
        Mat grayMat = new Mat();
        Imgproc.cvtColor(inputMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        // Resize the image to a fixed size for face matching
        Mat resizedMat = new Mat();
        Imgproc.resize(grayMat, resizedMat, new Size(FACE_WIDTH, FACE_HEIGHT));

        // Normalize the pixel values between 0 and 1
        Mat normalizedMat = new Mat();
        Object CvType = null;
        Object CV_32F = null;
        resizedMat.clone(normalizedMat, CV_32F, 1.0 / 255);

        // Apply histogram equalization to improve contrast
        Mat equalizedMat = new Mat();
        Imgproc.equalizeHist(normalizedMat, equalizedMat);

        // Return the preprocessed image
        return equalizedMat;
    }

    private List<PersonModel> performFaceMatching(Mat inputMat) {
        List<PersonModel> matchedPersons = new ArrayList<>();

        // Iterate over the Firebase person table
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PersonModel person = snapshot.getValue(PersonModel.class);

                    // Convert the person image from Base64 to OpenCV Mat format
                    Mat personMat = (Mat) convertBase64ToMat(person.getPersonImage());

                    // Perform face matching between input image and person image
                    double similarity = computeSimilarity(inputMat, personMat);

                    // Check if the similarity score is above a threshold (e.g., 0.8)
                    if (similarity >= 0.8) {
                        // Add the matched person to the list
                        matchedPersons.add(person);
                    }
                }

                // Update the RecyclerView with the matched persons
                displayMatchedPersons(matchedPersons);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        return matchedPersons;
    }

    private CharSequence convertBase64ToMat(String base64Image) {
        try {
            byte[] imageBytes = readImageFile(base64Image);
            String base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return base64String;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readImageFile(String imagePath) throws IOException {
        // Read the image file and convert it to byte array
        File file = new File(imagePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            byteStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        return byteStream.toByteArray();
    }

    private double computeSimilarity(Mat mat1, Mat mat2) {
        // Compute the similarity between two images (e.g., using feature extraction and comparison)
        // This can be done using various methods such as histogram comparison, deep learning-based models, etc.
        // Here, we compute the similarity as the absolute difference between the pixel intensities
        Mat diffMat = new Mat();
        Core.absdiff(mat1, mat2, diffMat);

        Scalar diffScalar = Core.sumElems(diffMat);
        double diffSum;

//        double similarity = 1 - (diffSum / (mat1.rows() * mat1.cols() * 255));

        double similarity = 0;
        return similarity;
    }


    private void displayMatchedPersons(List<PersonModel> matchedPersons) {
        // Clear the existing list
        personList.clear();

        // Add the matched persons to the list
        personList.addAll(matchedPersons);

        // Notify the adapter about the data change
        personAdapter.notifyDataSetChanged();
    }

    private class MatOfByte {
        public MatOfByte(byte[] decodedBytes) {
        }
    }

    private class Scalar {
        public double val;
    }

    private static class Core {
        public static void absdiff(Mat mat1, Mat mat2, Mat diffMat) {

        }

        public static Scalar sumElems(Mat diffMat) {
            return null;
        }
    }

    private class IMREAD_GRAYSCALE {
    }
    private static class Imgproc {
        public static final Object COLOR_BGR2GRAY = 0;

        public static void cvtColor(Mat inputMat, Mat grayMat, Object colorBgr2gray) {

        }

        public static void resize(Mat grayMat, Mat resizedMat, Size size) {

        }

        public static void equalizeHist(Mat normalizedMat, Mat equalizedMat) {

        }
    }

    private class CV_32F {
    }
}