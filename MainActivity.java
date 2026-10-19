package com.findit.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE = 41;
    private Uri selectedImage;
    private ImageView preview;
    private TextView imageStatus;
    private Button visualSearchButton;
    private EditText queryInput;

    private final int ink = Color.rgb(28, 34, 54);
    private final int muted = Color.rgb(102, 110, 130);
    private final int blue = Color.rgb(56, 89, 214);
    private final int pale = Color.rgb(242, 245, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        buildScreen();
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(250, 251, 255));
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(28), dp(22), dp(28));
        scroll.addView(page);

        TextView logo = text("FindIt", 32, ink, true);
        page.addView(logo);
        TextView subtitle = text("Discover public pages linked to an image", 15, muted, false);
        LinearLayout.LayoutParams subParams = params(-1, -2);
        subParams.topMargin = dp(5);
        page.addView(subtitle, subParams);

        LinearLayout uploadCard = card();
        LinearLayout.LayoutParams cardParams = params(-1, -2);
        cardParams.topMargin = dp(24);
        page.addView(uploadCard, cardParams);
        TextView uploadTitle = text("1  ·  Choose a photo", 18, ink, true);
        uploadCard.addView(uploadTitle);
        preview = new ImageView(this);
        preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        preview.setBackground(roundBg(Color.rgb(238, 241, 249), 18));
        LinearLayout.LayoutParams previewParams = params(-1, dp(210));
        previewParams.topMargin = dp(14);
        uploadCard.addView(preview, previewParams);
        imageStatus = text("No photo selected. Choose a clear image you have permission to use.", 13, muted, false);
        LinearLayout.LayoutParams statusParams = params(-1, -2);
        statusParams.topMargin = dp(10);
        uploadCard.addView(imageStatus, statusParams);
        Button chooseButton = button("Choose photo", true);
        LinearLayout.LayoutParams chooseParams = params(-1, dp(50));
        chooseParams.topMargin = dp(14);
        uploadCard.addView(chooseButton, chooseParams);
        chooseButton.setOnClickListener(v -> pickImage());

        LinearLayout searchCard = card();
        LinearLayout.LayoutParams searchParams = params(-1, -2);
        searchParams.topMargin = dp(16);
        page.addView(searchCard, searchParams);
        searchCard.addView(text("2  ·  Search public results", 18, ink, true));
        TextView help = text("Share the photo to a visual-search service such as Google Lens, if available. FindIt does not identify people by face.", 14, muted, false);
        LinearLayout.LayoutParams helpParams = params(-1, -2);
        helpParams.topMargin = dp(9);
        searchCard.addView(help, helpParams);
        visualSearchButton = button("Open visual search options", true);
        visualSearchButton.setEnabled(false);
        visualSearchButton.setAlpha(0.5f);
        LinearLayout.LayoutParams visualParams = params(-1, dp(50));
        visualParams.topMargin = dp(14);
        searchCard.addView(visualSearchButton, visualParams);
        visualSearchButton.setOnClickListener(v -> shareImageForSearch());

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(231, 234, 243));
        LinearLayout.LayoutParams dividerParams = params(-1, dp(1));
        dividerParams.topMargin = dp(20);
        searchCard.addView(divider, dividerParams);

        TextView queryTitle = text("Search with a name or public username", 15, ink, true);
        LinearLayout.LayoutParams qt = params(-1, -2); qt.topMargin = dp(16);
        searchCard.addView(queryTitle, qt);
        queryInput = new EditText(this);
        queryInput.setSingleLine(true);
        queryInput.setTextSize(15);
        queryInput.setHint("Name, handle, or public website");
        queryInput.setPadding(dp(13), 0, dp(13), 0);
        queryInput.setBackground(roundBg(Color.WHITE, 12));
        LinearLayout.LayoutParams inputParams = params(-1, dp(52));
        inputParams.topMargin = dp(10);
        searchCard.addView(queryInput, inputParams);
        Button webButton = button("Search public web pages", false);
        LinearLayout.LayoutParams webParams = params(-1, dp(50));
        webParams.topMargin = dp(10);
        searchCard.addView(webButton, webParams);
        webButton.setOnClickListener(v -> searchWeb());

        LinearLayout note = card();
        note.setBackground(roundBg(pale, 16));
        LinearLayout.LayoutParams noteParams = params(-1, -2);
        noteParams.topMargin = dp(16);
        page.addView(note, noteParams);
        note.addView(text("Privacy & accuracy", 15, ink, true));
        TextView noteBody = text("FindIt does not upload or store your photo itself. When you share it, the selected third-party service may receive it under its own policies. Results are not proof that two pages show the same person. Use this for legitimate, respectful searches of public information.", 13, muted, false);
        LinearLayout.LayoutParams nb = params(-1, -2); nb.topMargin = dp(7);
        note.addView(noteBody, nb);

        setContentView(scroll);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        try {
            startActivityForResult(intent, PICK_IMAGE);
        } catch (ActivityNotFoundException e) {
            Intent fallback = new Intent(Intent.ACTION_GET_CONTENT);
            fallback.setType("image/*");
            startActivityForResult(Intent.createChooser(fallback, "Choose a photo"), PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImage = data.getData();
            try {
                preview.setImageURI(selectedImage);
                imageStatus.setText("Photo selected. Your image stays on your device until you choose to share it.");
                visualSearchButton.setEnabled(true);
                visualSearchButton.setAlpha(1f);
            } catch (Exception e) {
                selectedImage = null;
                Toast.makeText(this, "Couldn't open that image. Please choose another.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void shareImageForSearch() {
        if (selectedImage == null) {
            Toast.makeText(this, "Choose a photo first.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, selectedImage);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setClipData(android.content.ClipData.newUri(getContentResolver(), "FindIt image", selectedImage));
        try {
            startActivity(Intent.createChooser(share, "Choose a visual-search app"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No compatible app found. Try installing a visual-search app, then try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void searchWeb() {
        String query = queryInput.getText().toString().trim();
        if (query.isEmpty()) {
            queryInput.setError("Enter a name or public username");
            return;
        }
        Uri uri = Uri.parse("https://www.google.com/search?q=" + Uri.encode(query));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No browser available.", Toast.LENGTH_LONG).show();
        }
    }

    private LinearLayout card() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(16), dp(16), dp(16), dp(16));
        layout.setBackground(roundBg(Color.WHITE, 18));
        if (android.os.Build.VERSION.SDK_INT >= 21) layout.setElevation(dp(2));
        return layout;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setGravity(Gravity.CENTER_VERTICAL);
        return view;
    }

    private Button button(String label, boolean primary) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setTextColor(primary ? Color.WHITE : blue);
        b.setBackground(roundBg(primary ? blue : pale, 13));
        return b;
    }

    private GradientDrawable roundBg(int color, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(radius));
        return bg;
    }

    private LinearLayout.LayoutParams params(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
