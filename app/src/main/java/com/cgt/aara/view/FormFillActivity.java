package com.cgt.aara.view;

import static com.cgt.aara.Utility.getImagePathFromUri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cgt.aara.R;
import com.cgt.aara.adapter.UserAdapter;
import com.cgt.aara.data.UserDataClass;
import com.cgt.aara.database.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FormFillActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private static final int REQUEST_CAMERA_PERMISSION = 3;
    private TextView headerTextView;
    private EditText nameEditText;
    private EditText mobileNumberEditText;
    private EditText dateOfBirthEditText;
    private EditText addressEditText;
    private Spinner incomeSalarySpinner;
    private Button saveButton;
    private ImageView profilePicImageView;
    private ImageView imgSort;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    private DatabaseHelper databaseHelper;
    private String currentPhotoPath;
    private List<UserDataClass> userDataClassList;
    LinearLayout llyImage;
    private ViewCreate viewCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            LinearLayout parentLayout = new LinearLayout(this);
            llyImage = new LinearLayout(this);

            parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            parentLayout.setOrientation(LinearLayout.VERTICAL);
            parentLayout.setPadding(16, 16, 16, 16);
            parentLayout.setGravity(Gravity.CENTER | Gravity.TOP);

            setContentView(parentLayout);
            viewCreate = new ViewCreate();

            headerTextView = viewCreate.createTextView(FormFillActivity.this, parentLayout, getResources().getString(R.string.aara_form_fill));

            nameEditText = viewCreate.createEditText(FormFillActivity.this, parentLayout, getResources().getString(R.string.customer_name));
            nameEditText.setSingleLine(true);

            mobileNumberEditText = viewCreate.createEditText(FormFillActivity.this, parentLayout, getResources().getString(R.string.mobile_number));
            mobileNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            mobileNumberEditText.setSingleLine(true);

            dateOfBirthEditText = viewCreate.createEditText(FormFillActivity.this, parentLayout, getResources().getString(R.string.date_of_birth));
            addressEditText = viewCreate.createEditText(FormFillActivity.this, parentLayout, getResources().getString(R.string.address));
            addressEditText.setLines(3); // Set the number of lines to display
            addressEditText.setGravity(Gravity.TOP | Gravity.START); // Set gravity to top-left
            addressEditText.setSingleLine(false); // Allow multiple lines

            incomeSalarySpinner = viewCreate.createSpinner(FormFillActivity.this, parentLayout, getResources().getString(R.string.income_salary));
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.income_salaries, R.layout.spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            incomeSalarySpinner.setAdapter(spinnerAdapter);

            profilePicImageView = viewCreate.createImageView(FormFillActivity.this, parentLayout, getResources().getString(R.string.profile_picture));
            profilePicImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasCameraPermission()) {
                        selectImageSource();
                    } else {
                        requestCameraPermission();
                    }
                }
            });


            saveButton = viewCreate.createButton(FormFillActivity.this, parentLayout, getResources().getString(R.string.save));
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUser();
                }
            });

            imgSort = viewCreate.createSortImageView(FormFillActivity.this, llyImage, parentLayout, getResources().getString(R.string.sort));
            llyImage.setVisibility(View.GONE);
            imgSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSortMenu(v);
                }
            });
            userRecyclerView = new RecyclerView(this);
            userRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            ));
            userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            parentLayout.addView(userRecyclerView);

            databaseHelper = new DatabaseHelper(this);
            userDataClassList = databaseHelper.getUserList();
            if (userDataClassList != null && userDataClassList.size() > 0) {
                llyImage.setVisibility(View.VISIBLE);
            }
            userAdapter = new UserAdapter(userDataClassList);
            userRecyclerView.setAdapter(userAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSortMenu(View v) {
        try {


            PopupMenu popupMenu = new PopupMenu(this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.sort_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    userDataClassList = databaseHelper.getUserList();

                    if (item.getItemId() == R.id.sort_by_address) {
                        sortByAddress();
                        return true;
                    } else if (item.getItemId() == R.id.sort_by_salary) {
                        sortBySalary();
                    } else {
                        sortByName();
                    }
                    return true;

                }

            });
            popupMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectImageSource() {
        try {


            final CharSequence[] options = {getResources().getString(R.string.take_photo),
                    getResources().getString(R.string.choose_from_gallery),
                    getResources().getString(R.string.cancel)};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.select_photo));
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals(getResources().getString(R.string.take_photo))) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals(getResources().getString(R.string.choose_from_gallery))) {
                    openGallery();
                } else if (options[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        try {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this,
                            "com.cgt.aara.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = imageFile.getAbsolutePath();
            return imageFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openGallery() {
        try {


            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        try {


        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.CAMERA"},
                REQUEST_CAMERA_PERMISSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImageSource();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Profile picture captured from camera, you can save the photo path or perform any necessary operations
            // For example, set the captured image to the ImageView:
            profilePicImageView.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            // Profile picture selected from gallery, you can retrieve the selected image URI and perform any necessary operations
            // For example, set the selected image to the ImageView:
            Uri selectedImageUri = data.getData();
            String imagePath = getImagePathFromUri(selectedImageUri, this);
            if (imagePath != null) {
                currentPhotoPath = imagePath;
            }
            profilePicImageView.setImageURI(selectedImageUri);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUser() {
        try {


        String name = nameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String dateOfBirth = dateOfBirthEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String incomeSalary = incomeSalarySpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(mobileNumber) || TextUtils.isEmpty(dateOfBirth)) {
            Toast.makeText(this, "Please enter necessary fields", Toast.LENGTH_SHORT).show();
        } else {

            UserDataClass userDataClass = new UserDataClass();
            userDataClass.setProfilePic(currentPhotoPath); // Save the photo path or other relevant information
            userDataClass.setName(name);
            userDataClass.setMobileNumber(mobileNumber);
            userDataClass.setDateOfBirth(dateOfBirth);
            userDataClass.setAddress(address);
            userDataClass.setIncomeSalary(incomeSalary);


            databaseHelper.insertUser(userDataClass);
            List<UserDataClass> userDataClassList = databaseHelper.getUserList();

            if (userDataClassList != null && userDataClassList.size() > 0) {
                llyImage.setVisibility(View.VISIBLE);
            }
            userAdapter.updateUserList(userDataClassList);

            nameEditText.setText("");
            mobileNumberEditText.setText("");
            dateOfBirthEditText.setText("");
            addressEditText.setText("");
            incomeSalarySpinner.setSelection(0);
            profilePicImageView.setImageResource(R.drawable.ic_profile_pic);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void sortByName() {
        try {


        userDataClassList.sort((u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()));
        userAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sortByAddress() {
        try {


        userDataClassList.sort((u1, u2) -> u1.getAddress().compareToIgnoreCase(u2.getAddress()));
        userAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sortBySalary() {
        try {


        userDataClassList.sort((u1, u2) -> u1.getIncomeSalary().compareToIgnoreCase(u2.getIncomeSalary()));
        userAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
