package com.example.mysdfapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mysdfapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public enum ResearchOption {
        POPULAR,
        NEAREST,
        KEYWORDS
    }

    private ResearchOption _currentOption = ResearchOption.POPULAR;
    private String _currentCategory;

    private List<String> _researchOptionsLabels;
    private List<String> _categories;

    public List<String> getCategories() {
        return _categories;
    }

    private ActivityMainBinding _binding;
    private FragmentManager _fragmentManager;
    private DatabaseManager _databaseManager;

    public DatabaseManager getDatabaseManager() {
        return _databaseManager;
    }

    private LocationManager _locationManager;
    private GeoPoint _userLocation;

    // Main layout ui component
    private Spinner _researchSpinner;
    private Spinner _keywordSpinner;
    private Toolbar _toolbar;
    private FloatingActionButton _myspaceButton;

    public void showFloatingButton(boolean enabled) {
        if (enabled) {
            _myspaceButton.setVisibility(View.VISIBLE);
        } else {
            _myspaceButton.setVisibility(View.GONE);
        }
    }

    public void FloatingButtonSwitchToAddMode() {
        _myspaceButton.setOnClickListener(_addListener);
    }

    public void FloatingButtonSwitchToMySpaceMode() {
        _myspaceButton.setOnClickListener(_myspaceListener);
    }

    public void setFloatingButtonIcon(int resID) {
        _myspaceButton.setImageResource(resID);
    }

    private View.OnClickListener _myspaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyPostListFragment newFragment = new MyPostListFragment();
            commitFragment(newFragment, null);
        }
    };
    private View.OnClickListener _addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateAnnouncementFragment newFragment = new CreateAnnouncementFragment();
            commitFragment(newFragment, null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate()");

        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        _fragmentManager = getSupportFragmentManager();
        _databaseManager = new DatabaseManager();
        _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setContentView(_binding.getRoot());

        _researchSpinner = _binding.mainResearchSpinner;
        _keywordSpinner = _binding.mainKeywordSpinner;
        _toolbar = _binding.mainToolbar;
        _myspaceButton = _binding.myspaceButton;

        setSupportActionBar(_toolbar);

        if (savedInstanceState == null) {
            substituteFragment(new PostListFragment());
        }

        _researchOptionsLabels = new ArrayList<>();
        _researchOptionsLabels.add(getResources().getString(R.string.researchOption_popular));
        _researchOptionsLabels.add(getResources().getString(R.string.researchOption_nearest));
        _researchOptionsLabels.add(getResources().getString(R.string.researchOption_keywords));

        ArrayAdapter<String> researchAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, _researchOptionsLabels);
        _researchSpinner.setAdapter(researchAdapter);

        _researchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (_currentOption == ResearchOption.KEYWORDS) {
                            hideKeywordsOptions();
                        }
                        if (_currentOption != ResearchOption.POPULAR) {
                            _databaseManager.ResetLastDocument();
                            _currentOption = ResearchOption.POPULAR;
                        }
                        break;
                    case 1:
                        if (_currentOption == ResearchOption.KEYWORDS) {
                            hideKeywordsOptions();
                        }
                        if (_currentOption != ResearchOption.NEAREST) {
                            _databaseManager.ResetLastDocument();
                            _currentOption = ResearchOption.NEAREST;
                        }
                        break;
                    case 2:
                        if (_currentOption != ResearchOption.KEYWORDS) {
                            showKeywordsOptions();
                            _databaseManager.ResetLastDocument();
                            _currentOption = ResearchOption.KEYWORDS;
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing
            }
        });

        _categories = new ArrayList<>();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, _categories);
        _keywordSpinner.setAdapter(categoryAdapter);
        _keywordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _currentCategory = _categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing
            }
        });

        _databaseManager.retrieveCategory(new DatabaseManager.ExecuteAfterCategoryQuery() {
            @Override
            public void applyToCategories(List<String> categories) {
                String log = "Categories fetched: ";
                for (String category : categories) {
                    _categories.add(category);
                    log += category + " ";
                }
                Log.i("MainActivity", log);
                categoryAdapter.notifyDataSetChanged();
            }
        });

        _myspaceButton.setOnClickListener(_myspaceListener);

        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, 12345);
        }
    }

    private void substituteFragment(Fragment fragment) {
        Log.i("MainActivity", "substituteFragment()");
        _fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    private void commitFragment(Fragment fragment, String tag) {
        _fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void setToolbarTitle(String title) {
        _toolbar.setTitle(title);
    }

    public void setBackButtonEnabled(boolean enabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // S'il y a des fragments dans la pile de retour arrière, revenir au précédent
                getSupportFragmentManager().popBackStack();
            } else {
                // Sinon, comportement par défaut (quitter l'activité)
                onBackPressed();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    public void showKeywordsOptions() {
        _keywordSpinner.setVisibility(View.VISIBLE);
    }

    public void hideKeywordsOptions() {
        _keywordSpinner.setVisibility(View.GONE);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Application doesn't have the permission to get the location.", Toast.LENGTH_SHORT).show();
        }
        Location location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            _userLocation = new GeoPoint(latitude, longitude);
        }
    }


    public void fetchInDatabase(DatabaseManager.ExecuteToListAfterQueryAction action){
        switch (_currentOption){
            case POPULAR:
                _databaseManager.searchAnnouncementsOrderByLikes(action);
                break;
            case NEAREST:
                getLastLocation();
                if (_userLocation != null){
                    _databaseManager.searchAnnouncementsOrderByProximity(action, _userLocation);
                }
                else{
                    Toast.makeText(this, "Couldn't find the users location.", Toast.LENGTH_SHORT).show();
                }
                break;
            case KEYWORDS:
                _databaseManager.searchAnnouncementsByCategory(_currentCategory, action);
                break;
        }
    }
}