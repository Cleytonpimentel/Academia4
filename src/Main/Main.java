package Main;

import java.util.List;
import java.util.Scanner;

import DAO.AtividadeDAO;
import DAO.InstrutorDAO;
import DAO.MembroDAO;
import DAO.PerfilDAO;
import DAO.PessoaDAO;
import DAO.PlanoDAO;
import DAO.SalaDAO;
import Entidades.Atividade;
import Entidades.Instrutor;
import Entidades.Membro;
import Entidades.Perfil;
import Entidades.Pessoa;
import Entidades.Plano;
import Entidades.Sala;
import Serviços.InstrutorService;
import Serviços.SalaService;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static MembroDAO membroDAO = new MembroDAO();
    private static InstrutorService instrutorService = new InstrutorService(new InstrutorDAO());
    private static SalaDAO salaDAO = new SalaDAO(); // Criação do objeto SalaDAO
    private static SalaService salaService = new SalaService(salaDAO);
    
    

    public static void main(String[] args) {
        while (true) {
            System.out.println("Bem-vindo ao menu de gerenciamento da academia:");
            System.out.println("1. Gerenciar Membros");
            System.out.println("2. Gerenciar Planos");
            System.out.println("3. Gerenciar Atividades");
            System.out.println("4. Gerenciar Instrutores");
            System.out.println("5. Gerenciar Salas");
            System.out.println("0. Sair");

            int opcao = 0;
            try {
                opcao = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar buffer
                continue;
            }
            scanner.nextLine(); // Limpar o buffer após a leitura do inteiro

            switch (opcao) {
                case 1:
                    gerenciarMembros();
                    break;
                case 2:
                    gerenciarPlanos();
                    break;
                case 3:
                    gerenciarAtividades();
                    break;
                case 4:
                    gerenciarInstrutores();
                    break;
                case 5:
                    gerenciarSalas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
    }

    private static void gerenciarMembros() {
        System.out.println("Gerenciar Membros:");
        System.out.println("1. Cadastrar Membro");
        System.out.println("2. Buscar Membro");
        System.out.println("3. Atualizar Membro");
        System.out.println("4. Remover Membro");
        System.out.println("5. Listar Membros");
        System.out.println("6. Sair");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        PessoaDAO pessoaDAO = new PessoaDAO();
        PlanoDAO planoDAO = new PlanoDAO();
        PerfilDAO perfilDAO = new PerfilDAO();

        switch (opcao) {
        case 1:
            // Cadastrar Membro
            System.out.println("Cadastro de Membro:");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            // Exibir os planos e pegar o plano escolhido
            System.out.println("Escolha o plano para o membro:");
            System.out.println("1. Semanal (R$ 50,00)");
            System.out.println("2. Mensal (R$ 150,00)");
            System.out.println("3. Semestral (R$ 750,00)");
            System.out.println("4. Anual (R$ 1400,00)");

            System.out.print("ID do Plano (1/2/3/4): ");
            int planoId = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            // Validar a opção de plano
            if (planoId < 1 || planoId > 4) {
                System.out.println("Plano inválido. Escolha um plano entre 1 e 4.");
                break;
            }

            // Buscar o plano no banco usando o método buscarPlanoPorId
            Plano plano = planoDAO.buscarPlanoPorId(planoId);

            // Adicionar informações do perfil
            System.out.println("Informações do Perfil:");
            System.out.print("Sexo (Masculino/Feminino): ");
            String sexo = scanner.nextLine();
            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            System.out.print("Altura (em metros, ex: 1.75): ");
            double altura = scanner.nextDouble();
            System.out.print("Peso (em kg, ex: 70.5): ");
            double peso = scanner.nextDouble();
            scanner.nextLine(); // Limpar o buffer

            // Criar objeto Perfil
            Perfil perfil = new Perfil(sexo, idade, altura, peso);

            // Criar o membro e associar o perfil
            if (plano != null) {
                Membro membro = new Membro(nome, cpf, endereco, telefone, plano);
                pessoaDAO.cadastrarPessoa(membro); // Usando o método de cadastro
                System.out.println("Membro cadastrado com sucesso! ID: " + membro.getId());

                // Após cadastrar o membro, associar o perfil a ele
                perfilDAO.cadastrarPerfil(perfil, membro.getId()); // Associar o perfil ao membro
                System.out.println("Perfil do membro cadastrado com sucesso!");
            } else {
                System.out.println("Cadastro de membro não realizado. Plano inválido.");
            }
            break;

        case 2:
            // Buscar Membro
            System.out.print("Digite o ID do Membro: ");
            int idBusca = scanner.nextInt();
            scanner.nextLine();

            Membro membroBuscado = pessoaDAO.buscarMembroPorId(idBusca);
            if (membroBuscado != null) {
                // Mostrar detalhes do membro incluindo o perfil
                Perfil perfilMembro = membroBuscado.getPerfil();
                System.out.printf("ID: %d\nNome: %s\nCPF: %s\nPlano: %s\nEndereço: %s\nTelefone: %s\nSexo: %s\nIdade: %d\nAltura: %.2f\nPeso: %.2f\n", 
                    membroBuscado.getId(), 
                    membroBuscado.getNome(), 
                    membroBuscado.getCpf(),
                    membroBuscado.getPlano() != null ? membroBuscado.getPlano().getNomePlano() : "Sem plano",
                    membroBuscado.getEndereco(),
                    membroBuscado.getTelefone(),
                    perfilMembro.getSexo(),
                    perfilMembro.getIdade(),
                    perfilMembro.getAltura(),
                    perfilMembro.getPeso());
            } else {
                System.out.println("Membro não encontrado.");
            }
            break;
        case 3:
            // Atualizar Membro
            System.out.print("Digite o ID do Membro para atualizar: ");
            int idAtualizar = scanner.nextInt();
            scanner.nextLine();

            Membro membroParaAtualizar = pessoaDAO.buscarMembroPorId(idAtualizar);
            if (membroParaAtualizar != null) {
                System.out.println("Membro encontrado.");

                System.out.print("Novo Endereço (Deixe em branco para não alterar): ");
                String novoEndereco = scanner.nextLine();
                if (!novoEndereco.isEmpty()) {
                    membroParaAtualizar.setEndereco(novoEndereco);
                }

                System.out.print("Novo Telefone (Deixe em branco para não alterar): ");
                String novoTelefone = scanner.nextLine();
                if (!novoTelefone.isEmpty()) {
                    membroParaAtualizar.setTelefone(novoTelefone);
                }

                // Atualizar perfil diretamente
                Perfil perfilAtualizar = membroParaAtualizar.getPerfil();

                System.out.print("Novo Sexo (Deixe em branco para não alterar): ");
                String novoSexo = scanner.nextLine();
                if (!novoSexo.isEmpty()) {
                    perfilAtualizar.setSexo(novoSexo);
                }

                System.out.print("Nova Idade (Deixe em branco para não alterar): ");
                String novaIdadeStr = scanner.nextLine();
                if (!novaIdadeStr.isEmpty()) {
                    perfilAtualizar.setIdade(Integer.parseInt(novaIdadeStr));
                }

                System.out.print("Nova Altura (Deixe em branco para não alterar): ");
                String novaAlturaStr = scanner.nextLine();
                if (!novaAlturaStr.isEmpty()) {
                    perfilAtualizar.setAltura(Double.parseDouble(novaAlturaStr));
                }

                System.out.print("Novo Peso (Deixe em branco para não alterar): ");
                String novoPesoStr = scanner.nextLine();
                if (!novoPesoStr.isEmpty()) {
                    perfilAtualizar.setPeso(Double.parseDouble(novoPesoStr));
                }

                // Atualizar perfil no banco
                if (perfilDAO.atualizarPerfil(perfilAtualizar, membroParaAtualizar.getId())) {  // Correção aqui
                    pessoaDAO.update(membroParaAtualizar); // Atualizar o membro
                    System.out.println("Membro atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar perfil.");
                }
            } else {
                System.out.println("Membro não encontrado.");
            }
            break;

        case 4:
            // Remover Membro
            System.out.print("Digite o ID do Membro para remover: ");
            int idRemover = scanner.nextInt();
            scanner.nextLine();

            Membro membroParaExcluir = pessoaDAO.buscarMembroPorId(idRemover);
            if (membroParaExcluir != null) {
                // Excluir membro, o perfil é automaticamente excluído por estar vinculado
                pessoaDAO.excluirPessoa(idRemover); 
                System.out.println("Membro removido com sucesso!");
            } else {
                System.out.println("Membro não encontrado.");
            }
            break;

        case 5:
            // Listar Membros
            List<Pessoa> pessoas = pessoaDAO.getPessoas();
            if (pessoas.isEmpty()) {
                System.out.println("Nenhum membro cadastrado.");
            } else {
                System.out.println("+-----------------------------------------------------------------------------------------------------------------------------+");
                System.out.printf("| %-5s | %-20s | %-15s | %-10s | %-45s | %-15s | %-10s | %-5s | %-5s | %-5s |%n", 
                    "ID", "Nome", "CPF", "Plano", "Endereço", "Telefone", "Sexo", "Idade", "Altura", "Peso");
                System.out.println("+-------------------------------------------------------------------------------------------------------------------------------+");

                for (Pessoa pessoa : pessoas) {
                    if (pessoa instanceof Membro) {
                        Membro membro = (Membro) pessoa;
                        Perfil perfilMembro = membro.getPerfil();
                        System.out.printf("| %-5d | %-20s | %-15s | %-10s | %-45s | %-15s | %-10s | %-5d | %-5.2f | %-5.2f |%n",
                                membro.getId(),
                                membro.getNome(),
                                membro.getCpf(),
                                membro.getPlano() != null ? membro.getPlano().getNomePlano() : "Sem plano",
                                membro.getEndereco(),
                                membro.getTelefone(),
                                perfilMembro.getSexo(),
                                perfilMembro.getIdade(),
                                perfilMembro.getAltura(),
                                perfilMembro.getPeso());
                    }
                }
            }
            break;

            case 6:
                // Sair
                System.out.println("Saindo...");
                System.exit(0);
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }
    }



    private static void gerenciarPlanos() {
        // Exibe as opções de gerenciamento de planos
        System.out.println("Gerenciar Planos:");
        System.out.println("1. Cadastrar Plano");
        System.out.println("2. Listar Planos");
        System.out.println("3. Atualizar Plano");
        System.out.println("4. Remover Plano");
        System.out.print("Escolha uma opção: ");
        
        int opcao = scanner.nextInt();
        scanner.nextLine();  // Limpar o buffer

        PlanoDAO planoDAO = new PlanoDAO();  // Instanciando o DAO de Plano

        switch (opcao) {
            case 1:
                // Cadastrar Plano
                System.out.println("Cadastro de Plano:");
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Valor: ");
                double valor = scanner.nextDouble();
                scanner.nextLine();  // Limpar o buffer

                // Cria o plano e o adiciona ao banco
                Plano novoPlano = new Plano(nome, valor);
                planoDAO.adicionarPlano(novoPlano);

                System.out.println("Plano cadastrado com sucesso!");
                break;

            case 2:
                // Listar Planos
                System.out.println("Lista de Planos Cadastrados:");
                List<Plano> planos = planoDAO.listarPlanos();
                if (planos.isEmpty()) {
                    System.out.println("Nenhum plano cadastrado.");
                } else {
                    // Exibe a lista de planos com uma boa formatação
                    System.out.println("+-----------------------------------------+");
                    System.out.printf("| %-5s | %-20s | %-15s |%n", "ID", "Nome", "Valor");
                    System.out.println("+-----------------------------------------+");

                    for (Plano planoList : planos) {
                        System.out.printf("| %-5d | %-20s | %-15.2f |%n",
                                planoList.getId(),
                                planoList.getNome(),
                                planoList.getValor());
                    }

                    System.out.println("+-----------------------------------------+");
                }
                break;

            case 3:
                // Atualizar Plano
                System.out.print("Digite o ID do Plano para atualizar: ");
                int planoId = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer

                // Aqui, você pode buscar o plano no banco e atualizar
                Plano planoParaAtualizar = planoDAO.listarPlanos().stream()
                                                    .filter(plano -> plano.getId() == planoId)
                                                    .findFirst().orElse(null);
                if (planoParaAtualizar != null) {
                    System.out.println("Plano encontrado.");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Novo Valor: ");
                    double novoValor = scanner.nextDouble();
                    scanner.nextLine();  // Limpar o buffer

                    // Atualiza as informações do plano
                    planoParaAtualizar.setNomePlano(novoNome);
                    planoParaAtualizar.setValor(novoValor);

                    // Aqui você implementaria o método de atualização no DAO, se necessário
                    planoDAO.adicionarPlano(planoParaAtualizar); // Atualizando o plano

                    System.out.println("Plano atualizado com sucesso!");
                } else {
                    System.out.println("Plano não encontrado.");
                }
                break;

            case 4:
                // Remover Plano
                System.out.print("Digite o ID do Plano para remover: ");
                int planoIdRemover = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer

                // Buscar o plano no banco e removê-lo
                Plano planoParaRemover = planoDAO.listarPlanos().stream()
                                                  .filter(plano -> plano.getId() == planoIdRemover)
                                                  .findFirst().orElse(null);
                if (planoParaRemover != null) {
                    // Aqui você implementaria o método de remover no DAO, se necessário
                    planoDAO.adicionarPlano(planoParaRemover); // Removendo o plano

                    System.out.println("Plano removido com sucesso!");
                } else {
                    System.out.println("Plano não encontrado.");
                }
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    private static void gerenciarAtividades() {
        // Exibe as opções de gerenciamento de atividades
        System.out.println("Gerenciar Atividades:");
        System.out.println("1. Cadastrar Atividade");
        System.out.println("2. Listar Atividades");
        System.out.println("3. Atualizar Atividade");
        System.out.println("4. Remover Atividade");
        System.out.println("5. Buscar Atividade por Nome");
        System.out.print("Escolha uma opção: ");
        
        int opcao = scanner.nextInt();
        scanner.nextLine();  // Limpar o buffer

        AtividadeDAO atividadeDAO = new AtividadeDAO();  // Instanciando o DAO de Atividade

        switch (opcao) {
            case 1:
                // Cadastrar Atividade
                System.out.println("Cadastro de Atividade:");
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Descrição: ");
                String descricao = scanner.nextLine();
                System.out.print("Instrutor: ");
                String instrutor = scanner.nextLine();

                // Cria a atividade e a adiciona ao banco
                Atividade novaAtividade = new Atividade(nome, descricao, instrutor);
                atividadeDAO.cadastrarAtividade(novaAtividade);

                System.out.println("Atividade cadastrada com sucesso!");
                break;

            case 2:
                // Listar Atividades
                System.out.println("Lista de Atividades Cadastradas:");
                List<Atividade> atividades = atividadeDAO.getAtividade();
                if (atividades.isEmpty()) {
                    System.out.println("Nenhuma atividade cadastrada.");
                } else {
                    // Exibe a lista de atividades com uma boa formatação
                    System.out.println("+----------------------------------------------------------------------------+");
                    System.out.printf("| %-5s | %-20s | %-30s | %-10s |%n", "ID", "Nome", "Descrição", "Instrutor");
                    System.out.println("+----------------------------------------------------------------------------+");

                    for (Atividade atividadeList : atividades) {
                        System.out.printf("| %-5d | %-20s | %-30s | %-10s |%n",
                                atividadeList.getId(),
                                atividadeList.getNome(),
                                atividadeList.getDescricao(),
                                atividadeList.getInstrutor());
                    }

                    System.out.println("+----------------------------------------------------------------------------+");
                }
                break;

            case 3:
                // Atualizar Atividade
                System.out.print("Digite o ID da Atividade para atualizar: ");
                int atividadeId = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer

                // Buscar a atividade no banco
                Atividade atividadeParaAtualizar = atividadeDAO.getAtividade().stream()
                                                                .filter(a -> a.getId() == atividadeId)
                                                                .findFirst().orElse(null);
                if (atividadeParaAtualizar != null) {
                    System.out.println("Atividade encontrada.");
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Nova Descrição: ");
                    String novaDescricao = scanner.nextLine();
                    System.out.print("Novo Instrutor: ");
                    String novoInstrutor = scanner.nextLine();

                    // Atualiza as informações da atividade
                    atividadeParaAtualizar.setNome(novoNome);
                    atividadeParaAtualizar.setDescricao(novaDescricao);
                    atividadeParaAtualizar.setInstrutor(novoInstrutor);

                    // Atualiza a atividade no banco
                    atividadeDAO.update(atividadeParaAtualizar);

                    System.out.println("Atividade atualizada com sucesso!");
                } else {
                    System.out.println("Atividade não encontrada.");
                }
                break;

            case 4:
                // Remover Atividade
                System.out.print("Digite o ID da Atividade para remover: ");
                int atividadeIdRemover = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer

                // Remover a atividade no banco
                atividadeDAO.deleteByID(atividadeIdRemover);

                System.out.println("Atividade removida com sucesso!");
                break;

            case 5:
                // Buscar Atividade por Nome
                System.out.print("Digite o nome da Atividade para buscar: ");
                String nomeAtividadeBusca = scanner.nextLine();

                // Buscar atividade pelo nome
                Atividade atividadeBuscada = atividadeDAO.buscarAtividadePorNome(nomeAtividadeBusca);
                if (atividadeBuscada != null) {
                    System.out.println("Atividade encontrada:");
                    System.out.println("ID: " + atividadeBuscada.getId());
                    System.out.println("Nome: " + atividadeBuscada.getNome());
                    System.out.println("Descrição: " + atividadeBuscada.getDescricao());
                    System.out.println("Instrutor: " + atividadeBuscada.getInstrutor());
                } else {
                    System.out.println("Atividade não encontrada.");
                }
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    private static void gerenciarInstrutores() {
        System.out.println("Gerenciar Instrutores:");
        System.out.println("1. Cadastrar Instrutor");
        System.out.println("2. Buscar Instrutor");
        System.out.println("3. Atualizar Instrutor");
        System.out.println("4. Remover Instrutor");
        System.out.println("5. Listar Instrutores");

        int opcao = scanner.nextInt();
        scanner.nextLine();  // Consumir a nova linha pendente

        switch (opcao) {
            case 1:
                // Cadastro de Instrutor
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("Especialidade: ");
                String especialidade = scanner.nextLine();

                // Criação do novo instrutor com os dados inseridos
                Instrutor novoInstrutor = new Instrutor(nome, cpf, especialidade);
                instrutorService.cadastrarInstrutor(novoInstrutor);
                System.out.println("Instrutor cadastrado com sucesso!");
                break;

            case 2:
                // Buscar Instrutor
                System.out.print("CPF: ");
                cpf = scanner.nextLine();
                Instrutor instrutor = instrutorService.buscarInstrutor(cpf);
                if (instrutor != null) {
                    System.out.println("ID: " + instrutor.getId());
                    System.out.println("Nome: " + instrutor.getNome());
                    System.out.println("CPF: " + instrutor.getCpf());
                    System.out.println("Especialidade: " + instrutor.getEspecialidade());
                } else {
                    System.out.println("Instrutor não encontrado.");
                }
                break;

            case 3:
                // Atualizar Instrutor
                System.out.print("CPF: ");
                cpf = scanner.nextLine();
                instrutor = instrutorService.buscarInstrutor(cpf);
                if (instrutor != null) {
                    System.out.print("Nome (atual: " + instrutor.getNome() + "): ");
                    nome = scanner.nextLine();
                    System.out.print("Especialidade (atual: " + instrutor.getEspecialidade() + "): ");
                    String especialidade1 = scanner.nextLine();
                    
                    // Atualizando os dados do instrutor
                    instrutor.setNome(nome);
                    instrutor.setEspecialidade(especialidade1);
                    instrutorService.atualizarInstrutor(instrutor);
                    System.out.println("Instrutor atualizado com sucesso!");
                } else {
                    System.out.println("Instrutor não encontrado.");
                }
                break;

            case 4:
                // Remover Instrutor
                System.out.print("CPF: ");
                cpf = scanner.nextLine();
                instrutorService.removerInstrutor(cpf);
                System.out.println("Instrutor removido com sucesso!");
                break;

            case 5:
                // Listar todos os Instrutores
                List<Instrutor> instrutores = instrutorService.listarInstrutores();
                if (instrutores.isEmpty()) {
                    System.out.println("Nenhum instrutor cadastrado.");
                } else {
                    System.out.println("=== Lista de Instrutores ===");
                    System.out.println("|----------------------------------------------------------------------------|");
                    // Cabeçalho da tabela
                    System.out.printf("| %-5s | %-25s | %-15s | %-20s |%n", "ID", "Nome", "CPF", "Especialidade");
                    System.out.println("|----------------------------------------------------------------------------|");
                    
                    // Exibindo os dados dos instrutores
                    for (Instrutor i : instrutores) {
                        System.out.printf("| %-5d | %-25s | %-15s | %-20s |%n", 
                                          i.getId(), i.getNome(), i.getCpf(), i.getEspecialidade());
                    }
                    
                    System.out.println("|----------------------------------------------------------------------------|");
                }
                break;

            default:
                System.out.println("Opção inválida!");
                break;      
        }
    }


    private static void gerenciarSalas() {
        System.out.println("Gerenciar Salas:");
        System.out.println("1. Cadastrar Sala");
        System.out.println("2. Buscar Sala");
        System.out.println("3. Atualizar Sala");
        System.out.println("4. Remover Sala");
        System.out.println("5. Listar Salas");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Nome: ");
                String nomeSala = scanner.nextLine();
                System.out.print("Capacidade: ");
                int capacidade = scanner.nextInt();
                scanner.nextLine();
                Sala novaSala = new Sala(nomeSala, capacidade);
                salaService.cadastrarSala(novaSala);
                System.out.println("Sala cadastrada com sucesso!");
                break;
            case 2:
                System.out.print("ID: ");
                int idSala = scanner.nextInt();
                Sala sala = salaService.buscarSala(idSala);
                if (sala != null) {
                    // Exibição formatada
                    System.out.println("+-------------------+--------------------+");
                    System.out.println("| ID                | Capacidade         |");
                    System.out.println("+-------------------+--------------------+");
                    System.out.printf("| %-17d | %-18d |\n", sala.getSalaId(), sala.getCapacidade());
                    System.out.println("+-------------------+--------------------+");
                } else {
                    System.out.println("Sala não encontrada.");
                }
                break;
            case 3:
                System.out.print("ID: ");
                idSala = scanner.nextInt();
                scanner.nextLine();
                sala = salaService.buscarSala(idSala);
                if (sala != null) {
                    System.out.print("Novo Nome: ");
                    nomeSala = scanner.nextLine();
                    System.out.print("Nova Capacidade: ");
                    capacidade = scanner.nextInt();
                    sala.setNome(nomeSala);
                    sala.setCapacidade(capacidade);
                    salaService.atualizarSala(sala);
                    System.out.println("Sala atualizada com sucesso!");
                } else {
                    System.out.println("Sala não encontrada.");
                }
                break;
            case 4:
                System.out.print("ID: ");
                idSala = scanner.nextInt();
                salaService.removerSala(idSala);
                System.out.println("Sala removida com sucesso!");
                break;
            case 5:
                List<Sala> salas = salaService.listarSalas();
                if (salas.isEmpty()) {
                    System.out.println("Nenhuma sala cadastrada.");
                } else {
                    // Cabeçalho para exibição
                    System.out.println("+-------------------+--------------------+");
                    System.out.println("| ID                | Capacidade         |");
                    System.out.println("+-------------------+--------------------+");

                    // Exibindo cada sala com formatação
                    for (Sala s : salas) {
                        System.out.printf("| %-17d | %-18d |\n", s.getSalaId(), s.getCapacidade());
                    }

                    // Rodapé para a tabela
                    System.out.println("+-------------------+--------------------+");
                }
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }
}