package com.danielkim.soundrecorder.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.fragments.EditFragment;
import com.danielkim.soundrecorder.fragments.FileViewerFragment;
import com.danielkim.soundrecorder.fragments.RecordFragment;


public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {
	
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String[] permissions = {
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.VIBRATE
			};
			int requestCode = 0;
			requestPermissions(permissions, requestCode);
		}
		
		setContentView(R.layout.activity_main);
		
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setOnPageChangeListener(this);
		tabs.setViewPager(pager);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
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
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	
	}
	
	@Override
	public void onPageSelected(int position) {
		if (position == 2) {
			EditFragment editFragment = (EditFragment) getSupportFragmentManager()
					.findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
			editFragment.resizeComponents();
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
	}
	
	public class MyAdapter extends FragmentPagerAdapter {
		
		private String[] titles = {
				getString(R.string.tab_title_record),
				getString(R.string.tab_title_saved_recordings),
				getString(R.string.tab_title_edit)
		};
		private Fragment[] fragments;
		
		public MyAdapter(FragmentManager fm) {
			super(fm);
			fragments = new Fragment[titles.length];
		}
		
		@Override
		public Fragment getItem(int position) {
			if (fragments[position] == null) {
				Fragment f = null;
				switch (position) {
					case 0: {
						f = RecordFragment.newInstance(position);
						break;
					}
					case 1: {
						f = FileViewerFragment.newInstance(position);
						break;
					}
					case 2: {
						f = EditFragment.newInstance();
						break;
					}
				}
				
				fragments[position] = f;
			}
			
			return fragments[position];
		}
		
		@Override
		public int getCount() {
			return titles.length;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
	
	public MainActivity() {
	}
}
