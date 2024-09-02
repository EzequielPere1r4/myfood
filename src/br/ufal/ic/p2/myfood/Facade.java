package br.ufal.ic.p2.myfood;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Facade {

    private Sistema sistema;


     public String lerArquivo(String caminho) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminho)));
    }

    
    public Facade() {
        sistema = new Sistema();
    }

    public void zerarSistema() {
        sistema.zerarSistema();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) {
        sistema.criarUsuario(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        sistema.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public String getAtributoUsuario(int id, String atributo) {
        return sistema.getAtributoUsuario(id, atributo);
    }

    public int login(String email, String senha) {
        return sistema.login(email, senha);
    }

    public void encerrarSistema() {
        sistema.encerrarSistema();
    }




//2 

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) {
        return sistema.criarEmpresa(tipoEmpresa, donoId, nome, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int idDono) {
        return sistema.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice) {
        return sistema.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int idEmpresa, String atributo) {
        return sistema.getAtributoEmpresa(idEmpresa, atributo);
    }




    //3
    public int criarProduto(int idEmpresa, String nome, float valor, String categoria) {
        return sistema.criarProduto(idEmpresa, nome, valor, categoria);
    }

    // Método para editar um produto
    public void editarProduto(int produtoId, String nome, float valor, String categoria) {
        sistema.editarProduto(produtoId, nome, valor, categoria);
    }

    // Método para obter um atributo de um produto específico
    public String getProduto (String nome, int empresaId, String atributo) {
        return sistema.getProduto(nome, empresaId, atributo);
    }
    // Método para listar todos os produtos de uma empresa
    public String listarProdutos(int empresaId) {
        return sistema.listarProdutos(empresaId);
    }







//4
// Cria um novo pedido para um cliente em uma empresa
 

}
 




