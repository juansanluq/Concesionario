package com.example.juan.concesionario;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {

    public static boolean pestañaNuevos = false;
    public static boolean pestañaOcasion = false;
    public static boolean pestañaExtras = false;
    public static Vehiculo vehiculoDetalle;
    public static ArrayList<Vehiculo> lista_vehiculos;
    public static ArrayList<Vehiculo> lista_vehiculos_ocasion;
    public static ArrayList<Extra> lista_extras;
    FloatingActionButton fab;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        fab.startAnimation(myAnim);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fab.startAnimation(myAnim);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
            int numeroPestaña = getArguments().getInt(ARG_SECTION_NUMBER);

            ListView lv;
            CochesAdapter adaptadorCoches;
            ExtrasAdapter adaptadorExtras;


            lv = rootView.findViewById(R.id.lv);
            lv.setEmptyView(rootView.findViewById(R.id.emptyElement));

            ConectorBBDD baseDatos = new ConectorBBDD(getContext());

            if (numeroPestaña == 1)
            {
                pestañaNuevos = true;
                pestañaOcasion = false;
                pestañaExtras = false;
            }
            else if (numeroPestaña == 2)
            {
                pestañaNuevos = false;
                pestañaOcasion = true;
                pestañaExtras = false;
            }
            else if (numeroPestaña == 3)
            {
                pestañaNuevos = false;
                pestañaOcasion = false;
                pestañaExtras = true;
            }
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            if(pestañaNuevos)
            {
                lista_vehiculos = baseDatos.recuperarVehiculosNuevos();
                adaptadorCoches = new CochesAdapter(getActivity(),lista_vehiculos);
                lv.setAdapter(adaptadorCoches);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        vehiculoDetalle = lista_vehiculos.get(position);
                        Intent intent = new Intent(getContext(),Detalle.class);
                        startActivity(intent);
                    }
                });
            }
            else if(pestañaOcasion)
            {
                lista_vehiculos_ocasion = baseDatos.recuperarVehiculosOcasion();
                adaptadorCoches = new CochesAdapter(getActivity(),lista_vehiculos_ocasion);
                lv.setAdapter(adaptadorCoches);
            }
            else if(pestañaExtras)
            {
                lista_extras = baseDatos.recuperarExtras();
                adaptadorExtras = new ExtrasAdapter(getActivity(),lista_extras);
                lv.setAdapter(adaptadorExtras);
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}