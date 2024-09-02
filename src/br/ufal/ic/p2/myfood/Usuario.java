package br.ufal.ic.p2.myfood;

public class Usuario {
    protected static int idCounter = 1;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String endereco;
    private String cpf;

    public Usuario(String nome, String email, String senha, String endereco, String cpf) {
        this.id = idCounter++;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return id +  nome + ";" + email + ";" + senha + ";" + endereco + ";" + (cpf != null ? "" + cpf : "" + ";");
    }
}
