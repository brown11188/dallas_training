package training.com.chatgcmapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import training.com.adapter.HomePageAdapter;
import training.com.common.AppConfig;

public class HomePageActivity extends AppCompatActivity {
    private final String TAB_RECENT = "Recent chat";
    private final String TAB_CONTACTS = "Contact List";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Intent intent;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        preferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        int id = preferences.getInt("userId",0);
        AppConfig.USER_ID = id;
        AppConfig.USER_NAME = preferences.getString("userName",null);
        AppConfig.REG_ID = preferences.getString("registration_id",null);
        if(id==0){
            intent = new Intent(HomePageActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_RECENT));
        tabLayout.addTab(tabLayout.newTab().setText(TAB_CONTACTS));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.pager);
        HomePageAdapter homePageAdapter = new HomePageAdapter
                (getFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(homePageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            doLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
    }
    public void doLogout(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("loginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
