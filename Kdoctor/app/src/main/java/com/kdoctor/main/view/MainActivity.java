package com.kdoctor.main.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoctor.R;
import com.kdoctor.dialogs.QuestionDialog;
import com.kdoctor.fragments.drawer.view.FragmentDrawer;
import com.kdoctor.fragments.drugs.view.FragmentDrug;
import com.kdoctor.fragments.sickness.view.FragmentSickness;
import com.kdoctor.fragments.vaccines.view.FragmentVaccine;
import com.kdoctor.main.adapters.AdapterViewPager;
import com.kdoctor.main.presenter.MainActivityPresenter;
import com.kdoctor.models.Diagnosis;
import com.kdoctor.models.Sickness;
import com.kdoctor.models.SicknessCategory;
import com.kdoctor.models.Vaccine;
import com.kdoctor.services.VaccineService;
import com.kdoctor.sql.DbManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    MainActivityPresenter presenter;
    ProgressDialog progressDialog;

    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.iv_drawer)
    ImageView ivDrawer;

    List<Fragment> lstFragments;
    List<Fragment> lstRestoredFragments;

    public FragmentSickness getFragmentSickness() {
        return fragmentSickness;
    }

    FragmentSickness fragmentSickness;

    public FragmentDrug getFragmentDrug() {
        return fragmentDrug;
    }

    FragmentDrug fragmentDrug;
    FragmentVaccine fragmentVaccine;
    AdapterViewPager adapterViewPager;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //DbManager.getInstance(getApplicationContext());

        presenter = new MainActivityPresenter(this);
        progressDialog = new ProgressDialog(getApplicationContext());

        fragmentSickness = new FragmentSickness();
        fragmentDrug = new FragmentDrug();
        fragmentVaccine = new FragmentVaccine();

        lstFragments = new ArrayList<Fragment>();
        lstFragments.add(fragmentSickness);
        lstFragments.add(fragmentDrug);
        lstFragments.add(fragmentVaccine);
        lstRestoredFragments = new ArrayList<>();
        lstRestoredFragments.addAll(lstFragments);

        adapterViewPager = new AdapterViewPager(getSupportFragmentManager(), lstFragments);
        viewPager.setAdapter(adapterViewPager);

        initEventListeners();

        if (VaccineService.message != null && !VaccineService.message.equals("")){
            QuestionDialog dialog = new QuestionDialog("Tiêm chủng", VaccineService.message, new QuestionDialog.OnOneChoiceSelection() {
                @Override
                public void onButtonClick() {

                }
            });
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    void initEventListeners(){
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });
        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT, true);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlBack.setVisibility(View.GONE);
                rlMain.setVisibility(View.VISIBLE);
                ((FragmentSickness)lstFragments.get(0)).back();
            }
        });

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

                rlBack.setVisibility(View.GONE);
                rlMain.setVisibility(View.VISIBLE);
                try {
                    ((FragmentSickness) lstFragments.get(0)).back();
                }
                catch (Exception e){

                }

                BottomBarTab barTab = bottomBar.getTabWithId(tabId);
                tvTitle.setText(barTab.getTitle());
                rlMain.setBackgroundColor(barTab.getBarColorWhenSelected());
                rlBack.setBackgroundColor(barTab.getBarColorWhenSelected());
            }
        });

        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position, true);
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
                FragmentDrawer.getInstance().getAdapter().setDiagnoses(DbManager.getInstance(getApplicationContext()).getRecords(DbManager.DIAGNOSIS, Diagnosis.class));
                FragmentDrawer.getInstance().getAdapter().notifyDataSetChanged();
            }
        };
        drawer.addDrawerListener(mDrawerToggle);

        List<Vaccine> vaccines = DbManager.getInstance(getApplicationContext()).getRecords(DbManager.VACCINES, Vaccine.class);
        VaccineService.setVaccines(vaccines);

        startService(new Intent(this, VaccineService.class));
    }

    public void go(String title){
        rlMain.setVisibility(View.GONE);
        rlBack.setVisibility(View.VISIBLE);
        tvCategory.setText(title);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //DbManager.getInstance(getApplicationContext()).close();
    }
}
