package br.com.maddu.gravador;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RecordingManager manager = new RecordingManager();

    public static void main(String[] args) {

        int option;

        do {
            showMenu();
            option = readInt("Escolha uma opção: ");

            switch (option) {
                case 1 -> criarGravacao();
                case 2 -> listarGravacoes();
                case 3 -> renomearGravacao();
                case 4 -> excluirGravacao();
                case 5 -> duplicarGravacao();
                case 6 -> favoritarGravacao();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

            System.out.println();
        } while (option != 0);
    }

    private static void showMenu() {
        System.out.println("==== MENU GRAVADOR ====");
        System.out.println("1 - Criar gravação (inputar nome)");
        System.out.println("2 - Listar gravações");
        System.out.println("3 - Renomear gravação");
        System.out.println("4 - Excluir gravação");
        System.out.println("5 - Duplicar gravação");
        System.out.println("6 - Favoritar / desfavoritar gravação");
        System.out.println("0 - Sair");
    }

    private static void criarGravacao() {
        System.out.println("== Criar gravação ==");
        System.out.print("Nome da gravação: ");
        String name = scanner.nextLine();

        // por enquanto
        String filePath = "C:/audios/" + System.currentTimeMillis() + ".m4a";

        Recording rec = manager.addRecording(filePath, name);
        System.out.println("Gravação criada:");
        System.out.println(rec);
    }

    private static void listarGravacoes() {
        System.out.println("== Lista de gravações ==");
        List<Recording> all = manager.getAllRecordings();

        if (all.isEmpty()) {
            System.out.println("Nenhuma gravação cadastrada.");
            return;
        }

        for (Recording r : all) {
            System.out.println("ID: " + r.getId());
            System.out.println("Nome: " + r.getName());
            System.out.println("Arquivo: " + r.getFilePath());
            System.out.println("Criado em: " + r.getCreatedAt());
            System.out.println("Favorita: " + (r.isFavorite() ? "★" : "-"));
            System.out.println("---------------------------");
        }
    }

    private static void renomearGravacao() {
        System.out.println("== Renomear gravação ==");
        listarGravacoes();

        System.out.print("Digite o ID da gravação que deseja renomear: ");
        String id = scanner.nextLine();

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();

        boolean ok = manager.renameRecording(id, novoNome);

        if (ok) {
            System.out.println("Gravação renomeada com sucesso!");
        } else {
            System.out.println("Não foi possível renomear. Verifique o ID.");
        }
    }

    private static void excluirGravacao() {
        System.out.println("== Excluir gravação ==");
        listarGravacoes();

        System.out.print("Digite o ID da gravação que deseja excluir: ");
        String id = scanner.nextLine();

        boolean ok = manager.deleteRecording(id);

        if (ok) {
            System.out.println("Gravação excluída com sucesso!");
        } else {
            System.out.println("Não foi possível excluir. Verifique o ID.");
        }
    }

    private static void duplicarGravacao() {
        System.out.println("== Duplicar gravação ==");
        listarGravacoes();

        System.out.print("Digite o ID da gravação que deseja duplicar: ");
        String id = scanner.nextLine();

        Recording copy = manager.duplicateRecording(id);

        if (copy != null) {
            System.out.println("Gravação duplicada com sucesso!");
            System.out.println("Nova gravação:");
            System.out.println(copy);
        } else {
            System.out.println("Não foi possível duplicar. Verifique o ID.");
        }
    }

    private static void favoritarGravacao() {
        System.out.println("== Favoritar / desfavoritar gravação ==");
        listarGravacoes();

        System.out.print("Digite o ID da gravação que deseja favoritar/desfavoritar: ");
        String id = scanner.nextLine();

        boolean ok = manager.toggleFavorite(id);

        if (ok) {
            System.out.println("Status de favorito atualizado com sucesso!");
        } else {
            System.out.println("Não foi possível atualizar. Verifique o ID.");
        }
    }

    private static int readInt(String label) {
        System.out.print(label);
        while (!scanner.hasNextInt()) {
            System.out.println("Digite um número válido.");
            scanner.next(); // descarta entrada inválida
            System.out.print(label);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consumir quebra de linha
        return value;
    }
}
