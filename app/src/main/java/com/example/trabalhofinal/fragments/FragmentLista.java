package com.example.trabalhofinal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.MyItemRecyclerViewAdapter;
import com.example.trabalhofinal.R;
import com.example.trabalhofinal.entities.Encomenda;
import com.example.trabalhofinal.interfaces.OnItemClickListener;

import java.util.List;

public class FragmentLista extends Fragment {
    private static final String ARG_PARAM1 = "user-id";
    private static final String ARG_PARAM2 = "encomenda-id";
    private static final String ARG_PARAM3 = "encomenda-codigo";
    private static final String ARG_PARAM4 = "encomenda-descricao";
    private static final String ARG_PARAM5 = "encomenda-userId";

    private final List<Encomenda> listaEncomendas;

    private FragmentManager fragmentManager;
    private String userId;
    private int mColumnCount = 1;

    public FragmentLista(List<Encomenda> listaEncomendas) {
        this.listaEncomendas = listaEncomendas;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_list, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            MyItemRecyclerViewAdapter recyclerViewAdapter = new MyItemRecyclerViewAdapter(listaEncomendas);

            recyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    FragmentEditaCodigo fragmentEditaCodigo = new FragmentEditaCodigo();
                    Bundle args = new Bundle();

                    args.putLong(ARG_PARAM2, listaEncomendas.get(position).getId());
                    args.putString(ARG_PARAM3, listaEncomendas.get(position).getCodigo());
                    args.putString(ARG_PARAM4, listaEncomendas.get(position).getDescricao());
                    args.putLong(ARG_PARAM5, listaEncomendas.get(position).getUserId());
                    fragmentEditaCodigo.setArguments(args);

                    MainActivity.replaceFragment(fragmentEditaCodigo, fragmentManager.beginTransaction());
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        return view;
    }
}