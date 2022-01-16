package com.gb.mynoteorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gb.mynoteorganizer.R;

public abstract class BaseActivity extends AppCompatActivity {

    // может быть несколько активити и поэтому мы создаем отдельный класс. Best practices
    //  абстактный чтобы не реализовывать в каждой активити. SOLID. Наследуем все методы отсюда
    public void replace(int container, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commit();
    }

    public void remove(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }
}
