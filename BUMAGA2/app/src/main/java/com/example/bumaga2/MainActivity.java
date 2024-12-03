package com.example.bumaga2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText titleText, developerText;
    private Button selectImageButton, addButton, updateButton, deleteButton;
    private ListView listView;
    private List<Game> gameList;
    private GameAdapter adapter;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        titleText = findViewById(R.id.titleText);
        developerText = findViewById(R.id.developerText);
        selectImageButton = findViewById(R.id.selectImageButton);
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        listView = findViewById(R.id.listView);

        gameList = Paper.book().read("games", new ArrayList<>());
        adapter = new GameAdapter(this, gameList);
        listView.setAdapter(adapter);

        selectImageButton.setOnClickListener(v -> selectImage());
        addButton.setOnClickListener(v -> addGame());
        updateButton.setOnClickListener(v -> showUpdateDialog());
        deleteButton.setOnClickListener(v -> showDeleteDialog());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                selectedImage = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addGame() {
        String title = titleText.getText().toString();
        String developer = developerText.getText().toString();

        if (title.isEmpty()) {
            Log.e("MainActivity", "Не указано название");
            return;
        }

        if (developer.isEmpty()) {
            Log.e("MainActivity", "Не указан разработчик");
            return;
        }

        if (selectedImage == null) {
            Log.e("MainActivity", "Не выбрана картинка");
            return;
        }

        Game game = new Game(title, developer, selectedImage);
        gameList.add(game);
        Paper.book().write("games", gameList);
        adapter.notifyDataSetChanged();
        titleText.setText("");
        developerText.setText("");
        selectedImage = null;
    }

    private void showUpdateDialog() {
        if (gameList.isEmpty()) {
            Log.e("MainActivity", "Список игр пуст");
            return;
        }

        String[] gameTitles = new String[gameList.size()];
        for (int i = 0; i < gameList.size(); i++) {
            gameTitles[i] = gameList.get(i).getTitle();
        }

        new AlertDialog.Builder(this)
                .setTitle("Выберите игру для обновления")
                .setItems(gameTitles, (dialog, which) -> {
                    Game selectedGame = gameList.get(which);
                    showUpdateGameDialog(selectedGame);
                })
                .show();
    }

    private void showUpdateGameDialog(Game game) {
        final EditText titleEditText = new EditText(this);
        final EditText developerEditText = new EditText(this);
        final ImageView imageView = new ImageView(this);
        titleEditText.setText(game.getTitle());
        developerEditText.setText(game.getDeveloper());
        imageView.setImageBitmap(game.getImage());

        new AlertDialog.Builder(this)
                .setTitle("Обновить игру")
                .setView(new LinearLayout(this) {{
                    setOrientation(LinearLayout.VERTICAL);
                    addView(titleEditText);
                    addView(developerEditText);
                    addView(imageView);
                }})
                .setPositiveButton("Обновить", (dialog, which) -> {
                    String newTitle = titleEditText.getText().toString();
                    String newDeveloper = developerEditText.getText().toString();
                    game.setTitle(newTitle);
                    game.setDeveloper(newDeveloper);
                    if (selectedImage != null) {
                        game.setImage(selectedImage);
                        selectedImage = null;
                    }
                    Paper.book().write("games", gameList);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showDeleteDialog() {
        if (gameList.isEmpty()) {
            Log.e("MainActivity", "Список игр пуст");
            return;
        }

        String[] gameTitles = new String[gameList.size()];
        for (int i = 0; i < gameList.size(); i++) {
            gameTitles[i] = gameList.get(i).getTitle();
        }

        new AlertDialog.Builder(this)
                .setTitle("Выберите игру для удаления")
                .setItems(gameTitles, (dialog, which) -> {
                    gameList.remove(which);
                    Paper.book().write("games", gameList);
                    adapter.notifyDataSetChanged();
                })
                .show();
    }
}