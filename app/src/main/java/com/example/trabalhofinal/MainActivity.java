package com.example.trabalhofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.trabalhofinal.fragments.FragmentHome;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        replaceFragment(new FragmentHome(), getSupportFragmentManager().beginTransaction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sair) {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static void replaceFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        /**
         * Garante que a Pilha de Fragmentos seja criada corretamente. Quando não
         * implementada, não é possível retornar ao Fragmento Inicial depois de
         * acessar a Galeria ou o Player de Música
         */
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}