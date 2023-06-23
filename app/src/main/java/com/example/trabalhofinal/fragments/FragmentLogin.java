package com.example.trabalhofinal.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.dao.UserDao;
import com.example.trabalhofinal.database.AppDatabase;
import com.example.trabalhofinal.entities.User;
import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

public class FragmentLogin extends Fragment {

    private UserDao userDao;

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        userDao = db.userDao();

        TextInputEditText usrInput = view.findViewById(R.id.usuarioinput);
        TextInputEditText senhaInput = view.findViewById(R.id.senhainput);

        Button entrarButton = view.findViewById(R.id.btnentrar);
        Button cadastroButton = view.findViewById(R.id.btncadastro);

        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(usrInput.toString(), senhaInput.toString());
            }
        });

        cadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usrInput.toString();
                String password = senhaInput.toString();

                if (!email.trim().isEmpty() && !password.trim().isEmpty()) {
                    createUser(usrInput.toString(), senhaInput.toString());
                } else {
                    Toast.makeText(requireContext(), "E-mail ou senha inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser(String email, String password) {
        User user = new User();

        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        userDao.insertUser(user);

        if (user.getId() > 0) {
            Toast.makeText(requireContext(), "Usuário inserido com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Erro ao inserir o usuário", Toast.LENGTH_SHORT).show();
        }
    }

    public void doLogin(String email, String password) {
        User user = userDao.getUserByEmailAndPassword(email, BCrypt.hashpw(password, BCrypt.gensalt()));

        if (user != null) {
            Toast.makeText(requireContext(), "Usuário encontrado: " + user.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }
}