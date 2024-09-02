package br.ufal.ic.p2.myfood;

import java.util.ArrayList;
import java.util.List;

public class Restaurante {
    private int id;
    private int donoId;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private List<Produto> produtos;

    public Restaurante(int id, int donoId, String nome, String endereco, String tipoCozinha) {
        this.id = id;
        this.donoId = donoId;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
                this.produtos = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public int getDonoId() {
        return donoId;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDonoId(int donoId) {
        this.donoId = donoId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    @Override
    public String toString() {
        return  id + ";"   +donoId + ";" +  nome + ";"+  endereco  + ";" + tipoCozinha ;
    }



    public Produto criarProduto(String nome, float valor, String categoria) {
        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
            }
        }
        int id = gerarIdUnico(); // Assumindo que você tem uma função que gera IDs únicos.
        Produto novoProduto = new Produto(id, nome, valor, categoria);
        produtos.add(novoProduto);
        return novoProduto;
    }

    public void editarProduto(int id, String nome, float valor, String categoria) {
        
        Produto produto = getProdutoById(id);
        produto.setNome(nome);
        produto.setValor(valor);
        produto.setCategoria(categoria);
    }

    public Produto getProdutoById(int id) {
        return produtos.stream()
                .filter(produto -> produto.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produto nao cadastrado"));
    }

    public Produto getProdutoByNome(String nome) {
        return produtos.stream()
                .filter(produto -> produto.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado"));
    }

    public String listarProdutos() {
        StringBuilder builder = new StringBuilder("{[");
        for (int i = 0; i < produtos.size(); i++) {
            builder.append(produtos.get(i).getNome());
            if (i < produtos.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]}");
        return builder.toString();
    }

    private int gerarIdUnico() {
        // Implementação de geração de IDs únicos.
        // Pode ser baseada em um contador estático, ou outro mecanismo.
        return produtos.size() + 1; // Exemplo simples, pode variar.
    }


    public List<Produto> getProdutos() {
        return produtos;
    }


     // Método para adicionar um produto
     public void adicionarProduto(Produto produto) {
        produtos.add(produto);


        
    }

    public void atualizarProduto(Produto produtoAtualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produtoAtualizado.getId()) {
                produtos.set(i, produtoAtualizado); // Substitui o produto existente pelo atualizado
                return;
            }
        }
    }
    

}
