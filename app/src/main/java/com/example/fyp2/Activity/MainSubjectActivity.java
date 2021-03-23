package com.example.fyp2.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.fyp2.Cards.SliderAdapter;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.R;
import com.example.fyp2.Utils.DecodeBitmapTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;
import java.util.Random;

public class MainSubjectActivity extends AppCompatActivity {

    private final int[][] dotCoords = new int[5][2];
    private final int[] pics = {R.drawable.p1};
    private final int[] maps = {R.drawable.map_paris};
    private final int[] descriptions = {1, 2, 3, 4, 5};
    private final String[] countries = {"PARIS"};
    ArrayList<String> subjectCode = new ArrayList<>();
    ArrayList<String> subjectName = new ArrayList<>();
    ArrayList<String> subjectType = new ArrayList<>();
    ArrayList<String> studyTime = new ArrayList<>();
    private final String[] places = {"The Louvre"};
    private final String[] temperatures = {"21°C"};
    private final String[] times = {"Aug 1 - Dec 15    7:00-18:00"};
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;

SliderAdapter sliderAdapter;

    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private ImageSwitcher mapSwitcher;
    private TextSwitcher temperatureSwitcher;
    private TextSwitcher placeSwitcher;
    private TextSwitcher clockSwitcher;
    private TextSwitcher descriptionsSwitcher;
    private View greenDot;

    private TextView country1TextView;
    private TextView country2TextView;
    private int countryOffset1;
    private int countryOffset2;
    private long countryAnimDuration;
    private int currentPosition;

    private DecodeBitmapTask decodeMapBitmapTask;
    private DecodeBitmapTask.Listener mapLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);
initfromFirebase();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && decodeMapBitmapTask != null) {
            decodeMapBitmapTask.cancel(true);
        }
    }

    private void initSwitchers() {
        temperatureSwitcher = (TextSwitcher) findViewById(R.id.ts_temperature);
        temperatureSwitcher.setFactory(new TextViewFactory(R.style.TemperatureTextView, true));
        try {
            temperatureSwitcher.setCurrentText(subjectType.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
//        temperatureSwitcher.setCurrentText(temperatures[0]);

        placeSwitcher = (TextSwitcher) findViewById(R.id.ts_place);
        placeSwitcher.setFactory(new TextViewFactory(R.style.PlaceTextView, false));
//        placeSwitcher.setCurrentText(places[0]);
        try {
            placeSwitcher.setCurrentText(subjectName.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        clockSwitcher = (TextSwitcher) findViewById(R.id.ts_clock);
        clockSwitcher.setFactory(new TextViewFactory(R.style.ClockTextView, false));
        clockSwitcher.setCurrentText(times[0]);
        try {
            clockSwitcher.setCurrentText(studyTime.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        descriptionsSwitcher = (TextSwitcher) findViewById(R.id.ts_description);
        descriptionsSwitcher.setInAnimation(this, android.R.anim.fade_in);
        descriptionsSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        descriptionsSwitcher.setFactory(new TextViewFactory(R.style.DescriptionTextView, false));
        descriptionsSwitcher.setCurrentText("12312312");

        mapSwitcher = (ImageSwitcher) findViewById(R.id.ts_map);
        mapSwitcher.setInAnimation(this, R.anim.fade_in);
        mapSwitcher.setOutAnimation(this, R.anim.fade_out);
        mapSwitcher.setFactory(new ImageViewFactory());
        mapSwitcher.setImageResource(maps[0]);

        mapLoadListener = new DecodeBitmapTask.Listener() {
            @Override
            public void onPostExecuted(Bitmap bitmap) {
                ((ImageView) mapSwitcher.getNextView()).setImageBitmap(bitmap);
                mapSwitcher.showNext();
            }
        };
    }

    private void initCountryText() {
        countryAnimDuration = getResources().getInteger(R.integer.labels_animation_duration);
        countryOffset1 = getResources().getDimensionPixelSize(R.dimen.left_offset);
        countryOffset2 = getResources().getDimensionPixelSize(R.dimen.card_width);
        country1TextView = (TextView) findViewById(R.id.tv_country_1);
        country2TextView = (TextView) findViewById(R.id.tv_country_2);

        country1TextView.setX(countryOffset1);
        country2TextView.setX(countryOffset2);
//        country1TextView.setText(countries[0]);
        try {
            country1TextView.setText(subjectCode.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
        country2TextView.setAlpha(0f);

//        country1TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
//        country2TextView.setTypeface(Typeface.createFromAsset(getAssets(), "open-sans-extrabold.ttf"));
    }

    private void initGreenDot() {
        mapSwitcher.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mapSwitcher.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final int viewLeft = mapSwitcher.getLeft();
                final int viewTop = mapSwitcher.getTop() + mapSwitcher.getHeight() / 3;

                final int border = 100;
                final int xRange = Math.max(1, mapSwitcher.getWidth() - border * 2);
                final int yRange = Math.max(1, (mapSwitcher.getHeight() / 3) * 2 - border * 2);

                final Random rnd = new Random();


            }
        });
    }

    private void setCountryText(String text, boolean left2right) {
        final TextView invisibleText;
        final TextView visibleText;
        if (country1TextView.getAlpha() > country2TextView.getAlpha()) {
            visibleText = country1TextView;
            invisibleText = country2TextView;
        } else {
            visibleText = country2TextView;
            invisibleText = country1TextView;
        }

        final int vOffset;
        if (left2right) {
            invisibleText.setX(0);
            vOffset = countryOffset2;
        } else {
            invisibleText.setX(countryOffset2);
            vOffset = 0;
        }

        invisibleText.setText(text);

        final ObjectAnimator iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f);
        final ObjectAnimator vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f);
        final ObjectAnimator iX = ObjectAnimator.ofFloat(invisibleText, "x", countryOffset1);
        final ObjectAnimator vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset);

        final AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(iAlpha, vAlpha, iX, vX);
        animSet.setDuration(countryAnimDuration);
        animSet.start();
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        setCountryText
                (
                        subjectCode.get(pos % subjectCode.size())
//                        countries[pos % countries.length]
                        , left2right);

        temperatureSwitcher.setInAnimation(MainSubjectActivity.this, animH[0]);
        temperatureSwitcher.setOutAnimation(MainSubjectActivity.this, animH[1]);
        temperatureSwitcher.setText(temperatures[pos % temperatures.length]);
        temperatureSwitcher.setText(subjectType.get(pos % subjectType.size()));

        placeSwitcher.setInAnimation(MainSubjectActivity.this, animV[0]);
        placeSwitcher.setOutAnimation(MainSubjectActivity.this, animV[1]);
//        placeSwitcher.setText(places[pos % places.length]);
        placeSwitcher.setText(subjectName.get(pos % subjectName.size()));

        clockSwitcher.setInAnimation(MainSubjectActivity.this, animV[0]);
        clockSwitcher.setOutAnimation(MainSubjectActivity.this, animV[1]);
        clockSwitcher.setText(times[pos % times.length]);
        clockSwitcher.setText(studyTime.get(pos % studyTime.size()));


        showMap(maps[pos % maps.length]);

        ViewCompat.animate(greenDot)
                .translationX(dotCoords[pos % dotCoords.length][0])
                .translationY(dotCoords[pos % dotCoords.length][1])
                .start();

        currentPosition = pos;
    }

    private void showMap(@DrawableRes int resId) {
        if (decodeMapBitmapTask != null) {
            decodeMapBitmapTask.cancel(true);
        }

        final int w = mapSwitcher.getWidth();
        final int h = mapSwitcher.getHeight();

        decodeMapBitmapTask = new DecodeBitmapTask(getResources(), resId, w, h, mapLoadListener);
        decodeMapBitmapTask.execute();
    }

    private class TextViewFactory implements ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View makeView() {
            final TextView textView = new TextView(MainSubjectActivity.this);

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextAppearance(MainSubjectActivity.this, styleId);
            } else {
                textView.setTextAppearance(styleId);
            }

            return textView;
        }

    }

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(MainSubjectActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            final ViewGroup.LayoutParams lp = new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm = (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(MainSubjectActivity.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);
                Toast.makeText(MainSubjectActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                } else {
                    final CardView cardView = (CardView) view;
                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                    final ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(MainSubjectActivity.this, sharedView, "shared");
                    startActivity(intent, options.toBundle());
                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }
    private void initfromFirebase() {
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();

        Task<QuerySnapshot> docRef = fStore.collection("Enrollment").whereEqualTo("studentID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    EnrollClass enrollClass = new EnrollClass();
                    enrollClass.setTimeStamp(documentSnapshot.get("timeStamp").toString());
                    enrollClass.setSubjectCode(documentSnapshot.get("subjectCode").toString());
                    enrollClass.setStudyMinutes(documentSnapshot.get("studyMinutes").toString());
                    enrollClass.setStudentID(documentSnapshot.get("studentID").toString());
                    enrollClass.setSubjectType(documentSnapshot.get("subjectType").toString());

                    subjectCode.add((documentSnapshot.get("subjectCode").toString()));
                    subjectName.add((documentSnapshot.get("subjectCode").toString()));
                    subjectType.add((documentSnapshot.get("subjectType").toString()));
                    studyTime.add((documentSnapshot.get("studyMinutes").toString()));

                }
//                sliderAdapter.notifyDataSetChanged();
                sliderAdapter= new SliderAdapter(pics, subjectCode.size(), new OnCardClickListener());

                initRecyclerView();
                initCountryText();
                initSwitchers();
                initGreenDot();

            }
        });

    }


}
