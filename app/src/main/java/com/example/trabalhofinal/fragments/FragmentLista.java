package com.example.trabalhofinal.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.MyItemRecyclerViewAdapter;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.interfaces.OnItemClickListener;
import com.example.trabalhofinal.placeholder.PlaceholderContent;

public class FragmentLista extends Fragment {
    private static final String ARG_PARAM1 = "codigo-rastreio";
    private static final String ARG_COLUMN_COUNT = "column-count";

    private FragmentManager fragmentManager;
    private int mColumnCount = 1;

    public FragmentLista() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_list, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            MyItemRecyclerViewAdapter recyclerViewAdapter = new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS);

            recyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    FragmentRastreio fragmentRastreio = new FragmentRastreio();
                    Bundle args = new Bundle();

                    args.putString(ARG_PARAM1, "ABLUBLE2");
                    fragmentRastreio.setArguments(args);

                    MainActivity.replaceFragment(fragmentRastreio, fragmentManager.beginTransaction());
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        return view;
    }
}