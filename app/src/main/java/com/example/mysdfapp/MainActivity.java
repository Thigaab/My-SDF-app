package com.example.mysdfapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mysdfapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public enum ResearchOption{
        POPULAR,
        NEAREST,
        KEYWORDS
    }

    private ResearchOption _currentOption = ResearchOption.POPULAR;

    private List<String> _researchOptionsLabels;
    private ActivityMainBinding _binding;
    private FragmentManager _fragmentManager;

    // Main layout ui component
    private Spinner _researchSpinner;
    private Spinner _keywordSpinner;
    private Toolbar _toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate()");

        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        _fragmentManager = getSupportFragmentManager();

        setContentView(_binding.getRoot());

        _researchSpinner = _binding.mainResearchSpinner;
        _keywordSpinner = _binding.mainKeywordSpinner;
        _toolbar = _binding.mainToolbar;

        setSupportActionBar(_toolbar);

        if (savedInstanceState == null){
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
                switch (position){
                    case 0:
                        if (_currentOption == ResearchOption.KEYWORDS){
                            hideKeywordsOptions();
                        }
                        _currentOption = ResearchOption.POPULAR;
                        break;
                    case 1:
                        if (_currentOption == ResearchOption.KEYWORDS){
                            hideKeywordsOptions();
                        }
                        _currentOption = ResearchOption.NEAREST;
                        break;
                    case 2:
                        if (_currentOption != ResearchOption.KEYWORDS){
                            showKeywordsOptions();
                        }
                        _currentOption = ResearchOption.KEYWORDS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing
            }
        });
    }

    private void substituteFragment(Fragment fragment){
        Log.i("MainActivity", "substituteFragment()");
        _fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    private void commitFragment(Fragment fragment, String tag){
        _fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void setToolbarTitle(String title){
        _toolbar.setTitle(title);
    }

    public void setBackButtonEnabled(boolean enabled){
        _toolbar.setBackInvokedCallbackEnabled(enabled);
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

        // If we want to add buttons

        return super.onOptionsItemSelected(item);
    }

    public void showKeywordsOptions(){
        _keywordSpinner.setVisibility(View.VISIBLE);
    }

    public void hideKeywordsOptions(){
        _keywordSpinner.setVisibility(View.GONE);
    }
}