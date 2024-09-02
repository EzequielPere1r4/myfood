package br.ufal.ic.p2.myfood;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Locale;



class Sistema {
    private Map<Integer, Usuario> usuarios = new HashMap<>();
    private Map<String, Integer> emailMap = new HashMap<>();
    
    private Map<Integer, Restaurante> restaurantes = new HashMap<>();
    private int restauranteIdCounter = 1;//ssssssssssssssssssssssssss
    
    private Map<Integer, Produto> produtos = new HashMap<>();
    private static final String FILE_PATH = "C:\\Users\\ezequ\\Documents\\facu\\P2\\myfood\\usuario.txt"; //como vai roda em outro pc***
    
    private int produtoIdCounter = 0; 
  

    public Sistema() {
      
        carregarDados();
    }

    public void zerarSistema() {
        usuarios.clear();
        emailMap.clear();
        restaurantes.clear();
        Usuario.idCounter = 1;
        produtos.clear();
        salvarDados(); // Atualiza o arquivo após zerar o sistema
    }

    public String getAtributoUsuario(int id, String atributo) {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario nao cadastrado.");
        }
        switch (atributo) {
            case "nome":
                return usuario.getNome();
            case "email":
                return usuario.getEmail();
            case "endereco":
            if (usuario instanceof Cliente) {
                return((Cliente) usuario).getEndereco();
            }
            break;
                //aq
            case "cpf":
                if (usuario instanceof DonoDeRestaurante){
                    return ((DonoDeRestaurante) usuario).getCpf();
                } 
                

            case "senha":
                return usuario.getSenha();
            default:
                throw new IllegalArgumentException("Atributo invalido.");
        }
        return atributo;
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) {
        validarDados(nome, email, senha, endereco, null);

        if (emailMap.containsKey(email)) {
            throw new IllegalArgumentException("Conta com esse email ja existe");
        }
        Cliente cliente = new Cliente(nome, email, senha, endereco);
        usuarios.put(cliente.getId(), cliente);
        emailMap.put(email, cliente.getId());
      //tirei o salvadados
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        validarDados(nome, email, senha, endereco, cpf);
        DonoDeRestaurante dono = new DonoDeRestaurante(nome, email, senha, endereco, cpf);

        usuarios.put(dono.getId(), dono);
        emailMap.put(email, dono.getId());

        
        if (cpf != null && (cpf.trim().isEmpty() || !Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", cpf))) {
            throw new IllegalArgumentException("CPF invalido");
        } else if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF invalido"); //dados so de dono*
        } 

       
      

        
    }

    public int login(String email, String senha) {
        Integer id = emailMap.get(email);
        if (id == null || !usuarios.get(id).getSenha().equals(senha)) {
            throw new IllegalArgumentException("Login ou senha invalidos");
        }
        return id ;
    }

    public void encerrarSistema() {
        salvarDados(); // Salva os dados antes de encerrar o sistema
    }

    //gepeto com força daq
    // Método para salvar em um arquivo
    private void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Usuario usuario : usuarios.values()) {
                if (usuario instanceof Cliente) {
                    // Salvar dados do cliente
                    writer.write(usuario.getNome() + ";" + usuario.getEmail() + ";" + usuario.getSenha() + ";" + ((Cliente) usuario).getEndereco());
                    writer.newLine();
                } else if (usuario instanceof DonoDeRestaurante) {
                    DonoDeRestaurante dono = (DonoDeRestaurante) usuario;
                    String cpf = dono.getCpf();
                    if (validarCpf(cpf)) {
                        // Salvar dados do dono de restaurante
                        writer.write(usuario.getNome() + ";" + usuario.getEmail() + ";" + usuario.getSenha() + ";" + dono.getEndereco() + ";" + cpf);
                        writer.newLine();
    
                        // Salvar dados dos restaurantes
                        for (Restaurante restaurante : dono.getRestaurantes()) {
                            writer.write("R;" + restaurante.getId() + ";" + restaurante.getDonoId() + ";" + restaurante.getNome() + ";" + restaurante.getEndereco() + ";" + restaurante.getTipoCozinha());
                            writer.newLine();
    
                            for (Produto produto : restaurante.getProdutos()) {
                                writer.write("P;"  +  produto.getId()  + ";" + restaurante.getId() + ";" + produto.getNome() + ";" + produto.getValor() + ";" + produto.getCategoria() );
                                writer.newLine();
                            }
                        }
                    } else {
                        System.out.println("CPF inválido não será salvo: " + cpf);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    private void carregarDados() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
    
                if (dados.length < 4) continue; // Verifica se a linha tem pelo menos 4 dados
    
                if (dados[0].equals("R")) {
                    // Carregar restaurante
                    if (dados.length < 6) continue; // Certifica-se de que há dados suficientes para criar um restaurante
    
                    try {
                        int idRestaurante = Integer.parseInt(dados[1]);
                        int donoId = Integer.parseInt(dados[2]);
                        String nomeRestaurante = dados[3];
                        String enderecoRestaurante = dados[4];
                        String tipoCozinha = dados[5];
    
                        // Cria o novo restaurante
                        Restaurante novoRestaurante = new Restaurante(idRestaurante, donoId, nomeRestaurante, enderecoRestaurante, tipoCozinha);
                        
                        // Adiciona o restaurante à lista
                        restaurantes.put(idRestaurante, novoRestaurante);
    
                        // Adiciona o restaurante ao dono
                        DonoDeRestaurante dono = (DonoDeRestaurante) usuarios.get(donoId);
                        if (dono != null) {
                            dono.adicionarRestaurante(novoRestaurante);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter número: " + e.getMessage());
                    }
    
                    continue;
                }
                if (dados[0].equals("P")) {
                    // Carregar produto
                    if (dados.length < 6) continue; // Certifica-se de que há dados suficientes para criar um produto
    
                    try {
                        int idProduto = Integer.parseInt(dados[1]);
                        int idRestaurante = Integer.parseInt(dados[2]);
                        String nomeProduto = dados[3];
                        float valorProduto = Float.parseFloat(dados[4]);
                        String categoriaProduto = dados[5];
    
                        // Busca o restaurante ao qual o produto pertence
                        Restaurante restaurante = restaurantes.get(idRestaurante);
                        if (restaurante != null) {
                            Produto novoProduto = new Produto(idProduto, nomeProduto, valorProduto, categoriaProduto);
                            restaurante.getProdutos().add(novoProduto);
                        }

                     
                        
                    }
                    
                    catch (NumberFormatException e) {
                        System.out.println("Erro ao converter número: " + e.getMessage());
                    }
    
                    continue;
                }
                // Carregar usuário
                String nome = dados[0];
                String email = dados[1];
                String senha = dados[2];
                String endereco = dados[3];
    
                Usuario usuario = null;
                if (dados.length == 5) {
                    String cpf = dados[4].trim();
                    if (validarCpf(cpf)) {
                        usuario = new DonoDeRestaurante(nome, email, senha, endereco, cpf);
                    } else {
                        System.out.println("CPF inválido: " + cpf);
                        continue;
                    }
                } else {
                    usuario = new Cliente(nome, email, senha, endereco);
                }
    
                if (!emailMap.containsKey(email)) {
                    usuarios.put(usuario.getId(), usuario);
                    emailMap.put(email, usuario.getId());
                }
    
                if (usuario.getId() >= Usuario.idCounter) {
                    Usuario.idCounter = usuario.getId() + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    
    private boolean validarCpf(String cpf) {
        // Valida CPF dnv
        return cpf != null && Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", cpf);
    }
    
    // Validador de dados gerais
    private void validarDados(String nome, String email, String senha, String endereco, String cpf) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }



         if (email != null && (email.trim().isEmpty() || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email))) {
            throw new IllegalArgumentException("Formato de email invalido");
        }else if (email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Email invalido");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha invalido");
        }

        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereco invalido");
        }
 
    }








//Pt 2 daq


public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) {
    if (!"restaurante".equalsIgnoreCase(tipoEmpresa)) {
        throw new IllegalArgumentException("Tipo de empresa invalido");
    }

    Usuario dono = usuarios.get(donoId);
    if (!(dono instanceof DonoDeRestaurante)) {
        throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
    }

    // Verificar duplicidade de nome para o mesmo dono
    verificarDuplicidadeNome(donoId, nome, endereco);

    // Verificar duplicidade de nome e local para o mesmo dono
    verificarNomeEmUsoPorOutroDono(donoId, nome);

    Restaurante novoRestaurante = new Restaurante(restauranteIdCounter++, donoId, nome, endereco, tipoCozinha);
    restaurantes.put(novoRestaurante.getId(), novoRestaurante);

    // Adiciona o restaurante na lista de restaurantes do dono
    ((DonoDeRestaurante) dono).adicionarRestaurante(novoRestaurante);

    return  novoRestaurante.getId();




    
}


// Verifica se existe um restaurante com o mesmo nome e local para o mesmo dono
private void verificarDuplicidadeNome(int donoId, String nome, String endereco) {
    for (Restaurante restaurante : restaurantes.values()) {
        if (restaurante.getDonoId() == donoId 
                && restaurante.getNome().equalsIgnoreCase(nome) 
                && restaurante.getEndereco().equalsIgnoreCase(endereco)) {
            throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
        }
    }

}

// Verifica se existe um restaurante com o mesmo nome e n mesmo dono
 
private void verificarNomeEmUsoPorOutroDono(int donoId, String nome) {
    for (Restaurante restaurante : restaurantes.values()) {
        if (restaurante.getNome().equalsIgnoreCase(nome) && restaurante.getDonoId() != donoId) {//!= 
            throw new IllegalArgumentException("Empresa com esse nome ja existe");
        }
    }
}



//funfa
 



public String getEmpresasDoUsuario(int idDono) {
    Usuario usuario = usuarios.get(idDono);
    if (!(usuario instanceof DonoDeRestaurante)) {
        throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
    }

    StringBuilder empresasBuilder = new StringBuilder();
    empresasBuilder.append("[");

    boolean first = true;
    for (Restaurante restaurante : restaurantes.values()) {
        if (restaurante.getDonoId() == idDono)
        
        
        {
            if (!first) {
                empresasBuilder.append(", ");
            }
            empresasBuilder.append("[")
                .append(restaurante.getNome())
                .append(", ")
                .append(restaurante.getEndereco())
                .append("]");
            first = false;
        }
    }

    empresasBuilder.append("]");
    return   "{" + empresasBuilder.toString() + "}";
}




public String getAtributoEmpresa(int idEmpresa, String atributo) {
    Restaurante restaurante = restaurantes.get(idEmpresa);
    if (restaurante == null) {
       throw new IllegalArgumentException("Empresa nao cadastrada");
    }
       //gtp
    if (atributo == null) {
       throw new IllegalArgumentException("Atributo invalido");
    } 
    
    
    if (atributo.trim().isEmpty())  {
        throw new IllegalArgumentException("Atributo invalido");
        
     }
    
    switch (atributo) {
       case "nome":
           return restaurante.getNome();
          
       case "endereco":
           return restaurante.getEndereco();
    
       case "tipoCozinha":
           return restaurante.getTipoCozinha();
      
       case "dono":
           return usuarios.get(restaurante.getDonoId()).getNome();
    
       default:
           throw new IllegalArgumentException("Atributo invalido");
    
       }
       
    }

 public int getIdEmpresa(int idDono, String nome, Integer indice) {

  

    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }
    
    List<Integer> ids = new ArrayList<>();
    for (Restaurante restaurante : restaurantes.values()) {
        if (restaurante.getDonoId() == idDono && restaurante.getNome().equalsIgnoreCase(nome)) {
            ids.add(restaurante.getId());
        }
    }

    if (ids.isEmpty()) {
        throw new IllegalArgumentException("Nao existe empresa com esse nome");
    }

    if (indice == null || indice < 0) {
        throw new IllegalArgumentException("Indice invalido");
    }

    if (indice >= ids.size()) {
        throw new IllegalArgumentException("Indice maior que o esperado");
    }

    return ids.get(indice); // Retorna o int diretamente


   
}











//precisa passar o id com ${}, esse int safado 6 erros 120
//temos iddono e donoid, segura sabomba aw
//att2 magicamente funfou DEUS é bom



//pt 3
public int criarProduto(int idEmpresa, String nome, float valor, String categoria) {
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }
    if (valor <= 0) {
        throw new IllegalArgumentException("Valor invalido");
    }
    if (categoria == null || categoria.trim().isEmpty()) {
        throw new IllegalArgumentException("Categoria invalido");
    }

    Restaurante restaurante = restaurantes.get(idEmpresa);
    if (restaurante == null) {
        throw new IllegalArgumentException("Restaurante nao encontrado");
    }

    for (Produto produto : restaurante.getProdutos()) {
        if (produto.getNome().equals(nome)) {
            throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
        }
    }

    int idProduto = gerarIdProduto(); // Método para gerar um ID único
    Produto novoProduto = new Produto(idProduto, nome, valor, categoria);
    restaurante.adicionarProduto(novoProduto);
    produtos.put(idProduto, novoProduto); // Adiciona o produto ao mapa global de produtos
    salvarDados(); // Salva os dados em arquivo após a criação do produto

    return idProduto++;
}

private Produto encontrarProdutoPorId(int idProduto) {
    return produtos.get(idProduto);
}

private int gerarIdProduto() {
    // Implementar lógica para gerar um ID único
    return ++produtoIdCounter;
}

public void editarProduto(int idProduto, String nome, float valor, String categoria) {
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }
    if (valor <= 0) {
        throw new IllegalArgumentException("Valor invalido");
    }
    if (categoria == null || categoria.trim().isEmpty()) {
        throw new IllegalArgumentException("Categoria invalido");
    }

    Produto produtoExistente = encontrarProdutoPorId(idProduto);
    if (produtoExistente == null) {
        throw new IllegalArgumentException("Produto nao cadastrado");
    }



       


    
    // Atualiza os atributos do produto existente
    produtoExistente.setNome(nome);
    produtoExistente.setValor(valor);
    produtoExistente.setCategoria(categoria);

    int idEmpresa = produtoExistente.getIdRestaurante();
    Restaurante restaurante = restaurantes.get(idEmpresa);
    if (restaurante == null) {
        throw new IllegalArgumentException("Restaurante nao encontrado para o ID: " + idEmpresa);
    }




for (Produto produto : restaurante.getProdutos()) {
    if (produto.getId() != idProduto && produto.getNome().equals(nome)) {
        throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
    }
}

    
    // Atualiza o produto na coleção do restaurante
    restaurante.atualizarProduto(produtoExistente); // Supõe que atualizarProduto substitui o antigo pelo editado
    
    // Atualiza o mapa global de produtos
    produtos.put(idProduto, produtoExistente);
    
    // Salva os dados em arquivo após a edição do produto
        salvarDados();

}


public String getProduto(String nome, int idEmpresa, String atributo) {
    // Encontrar o produto com base no nome e idEmpresa
    Restaurante restaurante = restaurantes.get(idEmpresa);
    if (restaurante == null) {
        return "Restaurante nao encontrado";
    }

    Produto produto = restaurante.getProdutoByNome(nome);
    if (produto == null) {
        return "Produto nao encontrado";
    }

    switch (atributo) {
        case "nome":
            return produto.getNome();
        case "valor":
            return String.format(Locale.US, "%.2f", produto.getValor()); // Formata com ponto como separador decimal
        case "categoria":
            return produto.getCategoria();
        case "empresa":
            return restaurante.getNome();
        default:
            throw new IllegalArgumentException("Atributo nao existe");
    }
}


public String listarProdutos(int empresaId) {
    Restaurante restaurante = restaurantes.get(empresaId);
    if (restaurante == null) {
        throw new IllegalArgumentException("Empresa nao encontrada");
    }

    List<String> nomesProdutos = restaurante.getProdutos().stream()
        .map(Produto::getNome)
        .collect(Collectors.toList());

    return "{[" + String.join(", ", nomesProdutos) + "]}";
}

  


 


 









//4


 

}



