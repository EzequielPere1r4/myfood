package br.ufal.ic.p2.myfood;

import java.util.ArrayList;
import java.util.List;

public class DonoDeRestaurante extends Usuario {
    private List<Restaurante> restaurantes;

    public DonoDeRestaurante(String nome, String email, String senha, String endereco, String cpf) {
        super(nome, email, senha, endereco, cpf);
        this.restaurantes = new ArrayList<>();
    }

    // Método para adicionar um restaurante ao dono
    public void adicionarRestaurante(Restaurante restaurante) {
        this.restaurantes.add(restaurante);
    }

    // Método para obter a lista de restaurantes do dono
    public List<Restaurante> getRestaurantes() {
        return restaurantes;
    }
}
