package br.ufal.ic.p2.myfood;


public class Produto   {
    private int id;
    private String nome;
    private float valor;
    private String categoria;
    private int idRestaurante;
    // Construtor
    public Produto(int id, String nome, float valor, String categoria) {
         this.id = id;
        this.setNome(nome);
        this.setValor(valor);
        this.setCategoria(categoria);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }
        this.nome = nome;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor invalido");
        }
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria invalida");
        }
        this.categoria = categoria;
    }

    public int getIdRestaurante() { return idRestaurante; } // Método para obter o ID do restaurante

}
