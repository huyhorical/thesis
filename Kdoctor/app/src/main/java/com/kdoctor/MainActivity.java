package com.kdoctor;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kdoctor.adapters.AdapterViewPager;
import com.kdoctor.bases.IView;
import com.kdoctor.fragments.FragmentDrawer;
import com.kdoctor.fragments.FragmentSickness;
import com.kdoctor.fragments.vaccines.view.FragmentVaccine;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    MainActivityPresenter presenter;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    List<Fragment> lstFragments;
    FragmentSickness fragmentSickness;
    FragmentSickness fragmentSickness2;
    FragmentVaccine fragmentVaccine;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);

        fragmentSickness = new FragmentSickness();
        fragmentSickness2 = new FragmentSickness();
        fragmentVaccine = new FragmentVaccine();

        lstFragments = new ArrayList<Fragment>();
        lstFragments.add(fragmentSickness);
        lstFragments.add(fragmentSickness2);
        lstFragments.add(fragmentVaccine);

        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager(), lstFragments);
        viewPager.setAdapter(adapterViewPager);

        initEventListeners();
    }

    void initEventListeners(){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_sickness:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_drug:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_vaccine:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }

            }
        });

        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                0,
                0
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawer.addDrawerListener(mDrawerToggle);
    }
}
