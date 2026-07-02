package com.example.looklost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImageSearch extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final String API_KEY = "AIzaSyBBMvZg170AX_lOmEUoG78QNDkJlHBqTEs";
    private static final String SEARCH_ENGINE_ID = "f3ab589b30fe54389";
    private RecyclerView recyclerView;
    private FaceImageSearchadapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new FaceImageSearchadapter();
        recyclerView.setAdapter(imageAdapter);
        // Request permission to access the user's gallery
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_IMAGE_PICK);
//        } else {
            openGallery();
//        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                String imagePath = getImagePath(selectedImageUri);
                if (imagePath != null) {
                    performImageSearch(imagePath);
                } else {
                    // Handle error in resolving the image path
                    Toast.makeText(ImageSearch.this, "image request error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getImagePath(Uri imageUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

//    private void performImageSearch(String imagePath) {
//        String searchQuery = "file://" + imagePath;
//        String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY +
//                "&cx=" + SEARCH_ENGINE_ID +
//                "&searchType=image" +
//                "&q=" + searchQuery;
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        List<String> imageUrls = parseSearchResults(response);
//                        imageAdapter.setImageUrls(imageUrls);
//                        imageAdapter.notifyDataSetChanged();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error in the image search request
//                    }
//                });
//
//        Volley.newRequestQueue(this).add(request);
//    }
private void performImageSearch(String imageUrl) {
    try {
        String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY +
                "&cx=" + SEARCH_ENGINE_ID +
                "&searchType=image" +
                "&q=" + URLEncoder.encode("image_url:" + imageUrl, "UTF-8");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<FaceImageSearch> searchResults = parseSearchResults(response);
                        imageAdapter.setSearchResults(searchResults);
                        imageAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error in the image search request
                    }
                });

        Volley.newRequestQueue(this).add(request);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}
    private List<FaceImageSearch> parseSearchResults(JSONObject response) {
        List<FaceImageSearch> searchResults = new ArrayList<>();

        try {
            JSONArray items = response.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String imageUrl = item.getString("link");
                String title = item.getString("title");
                String snippet = item.getString("snippet");

                FaceImageSearch searchResult = new FaceImageSearch(imageUrl, title, snippet);
                searchResults.add(searchResult);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchResults;
    }


//    private List<String> parseSearchResults(JSONObject response) {
//        List<String> imageUrls = new ArrayList<>();
//        try {
//            JSONArray items = response.getJSONArray("items");
//            for (int i = 0; i < items.length(); i++) {
//                JSONObject item = items.getJSONObject(i);
//                String imageUrl = item.getString("link");
//                imageUrls.add(imageUrl);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return imageUrls;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(ImageSearch.this, "permission request error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.performImageSearch();

//    private class SearchRequest {
//        private static final String API_KEY = "YOUR_API_KEY";
//        private static final String SEARCH_ENGINE_ID = "YOUR_SEARCH_ENGINE_ID";
//        private static final String UPLOAD_IMAGE_URL = "URL_OF_UPLOADED_IMAGE";
//
//        public void performImageSearch() {
//            String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + API_KEY +
//                    "&cx=" + SEARCH_ENGINE_ID +
//                    "&searchType=image" +
//                    "&q=" + UPLOAD_IMAGE_URL;
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONArray items = response.getJSONArray("items");
//                                for (int i = 0; i < items.length(); i++) {
//                                    JSONObject item = items.getJSONObject(i);
//                                    String imageUrl = item.getString("link");
//                                    // Process the image URL as per your app's requirements
//                                    System.out.println(imageUrl);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            error.printStackTrace();
//                        }
//                    });
//
//            Volley.newRequestQueue(getApplicationContext()).add(request);
//        }
//    }




//In OnCreate method
// // Get the search results from the previous activity or data source
//        searchResults = getSearchResults();
//
//        // Initialize the ListView
//        listView = findViewById(R.id.listView);
//
//        // Create an adapter to populate the ListView with the search results
//        FaceImageSearchadapter adapter = new FaceImageSearchadapter(this, searchResults);
//        listView.setAdapter(adapter);
//
//        // Set an item click listener for the ListView
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the clicked item
//                FaceImageSearch clickedResult = searchResults.get(position);
//
//                // Open the image in a new activity or in the browser
//                openImage(clickedResult.getImageUrl());
//            }
//        });
//    }
//
//    private void openImage(String imageUrl) {
//        // You can customize this method based on how you want to display the image
//        // For example, you can start a new activity to display the image or open it in the browser
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
//        startActivity(intent);
//    }
//
//    private ArrayList<FaceImageSearch> getSearchResults() {
//        // Implement this method to fetch or generate the image search results
//        // For this example, let's assume we have an ArrayList of ImageSearchResult objects
//        ArrayList<FaceImageSearch> results = new ArrayList<>();
//
//        // Add some dummy results for demonstration purposes
//        results.add(new FaceImageSearch("https://example.com/image1.jpg", "Image 1", "Description 1"));
//        results.add(new FaceImageSearch("https://example.com/image2.jpg", "Image 2", "Description 2"));
//        results.add(new FaceImageSearch("https://example.com/image3.jpg", "Image 3", "Description 3"));
//
//        return results;