import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Application {
    public final Scanner ler = new Scanner(System.in);
    public final Connection conn;
    public final Statement st;

    
    public Application (Connection conn, Statement st) {
        this.conn = conn;
        this.st = st;
    }
    
    public void clear() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.err.format("Erro ao limpar a tela: %s", e.getMessage());
        }
    }
    
    
    public void FrontEnd () {
        int escolha;
        while (true) {
            System.out.println("\n---------- SEJA BEM-VINDO A LISTA DE DESEJOS ----------");
            System.out.println(" \n     OBS:Primeiro você precisa criar uma lista.\n");
            System.out.println("    1 - Criar lista nova.");
            System.out.println("    2 - Ler lista antiga.");
            System.out.println("    0 - Sair.");
            System.out.print("\nEscolha: ");
            escolha = ler.nextInt();
            clear();
            switch (escolha) {
                case 1:
                    this.createTable();
                    break;
                case 2:
                    this.insertData();
                    break;
                case 3:
                    this.readData();
                    break;
                case 4:
                    this.updateData();
                    break;
                case 5:
                    this.deleteData(";");
                    break;
                case 0:
                    System.out.println("Adeus!");
                    return;
                default:
                    System.out.println("Erro.");
                    break;
            }
        }
    }
    
    
    public void createTable() {
        try {
            System.out.println("---------- CRIAR NOVA LISTA ----------");
            System.out.print("\nNome da lista: ");
            String nomeTabela = ler.next();
            String SQLCriarTabela = "CREATE TABLE " + nomeTabela + " (id SERIAL PRIMARY KEY, preco float, nome VARCHAR(60), tipo VARCHAR(60));";
            st.executeUpdate(SQLCriarTabela);
            System.out.print("\nCriando lista");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            clear();
            this.insertData();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    }

    
    public void insertData () {
        try {
            System.out.println("---------- LISTAS DISPONIVEIS ----------\n");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});

            // Iterar sobre os resultados e imprimir os nomes das tabelas
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName + "\n");
            }
            System.out.print("\nDigite o nome da lista que quer inserir dados:");
            String nomeTabela = ler.next();
            clear();
            System.out.println("---------- INSERIR DADOS ----------");
            System.out.print("\nNome do item: ");
            String nome = ler.next();
            System.out.print("\nPreço do item: ");
            float preco = ler.nextFloat();
            String SQLInserirDados = "INSERT INTO " + nomeTabela + " (preco, nome) VALUES (" + preco + ", '" + nome + "')";
            st.executeUpdate(SQLInserirDados);
            clear();
            System.out.print("\ninserindo dados");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    }
    
    public void readData () {
        ResultSet result;
        try {
            System.out.println("---------- LISTAS DISPONIVEIS ----------\n");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});

            // Iterar sobre os resultados e imprimir os nomes das tabelas
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName + "\n");
            }
            System.out.print("\nDigite o nome da lista que quer ler:");
            String nomeTabela = ler.next();
            clear();
            String SQLLerDados = "SELECT * FROM " + nomeTabela;
            result = st.executeQuery(SQLLerDados);
            while (result.next()) {
                System.out.println("--------------------------------------------------");
                System.out.println("nome: " + result.getString("nome"));
                System.out.println("preco: " + result.getFloat("preco"));
            }
            result.close();
            System.out.print("\nleia dados");
            Thread.sleep(2000);
            System.out.print(".");
            Thread.sleep(2000);
            System.out.print(".");
            Thread.sleep(2000);
            System.out.print(".");
            Thread.sleep(2000);        
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    
    }
    
    public void updateData() {
        try {
            System.out.println("---------- LISTAS DISPONIVEIS ----------\n");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});

            // Iterar sobre os resultados e imprimir os nomes das tabelas
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName + "\n");
            }

            System.out.print("Digite o nome da tabela que deseja atualizar: ");
            String nomeTabela = ler.next();
            clear();

            System.out.println("---------- DADOS ATUAIS ----------");
            String SQLLerDados = "SELECT * FROM " + nomeTabela;
            ResultSet result = st.executeQuery(SQLLerDados);
            while (result.next()) {
                System.out.println("--------------------------------------------------");
                System.out.println("ID: " + result.getInt("ID"));
                System.out.println("nome: " + result.getString("nome"));
                System.out.println("preco: " + result.getFloat("preco"));
            }
            result.close();

            System.out.print("\nDigite o ID da linha que deseja atualizar: ");
            int idLinha = ler.nextInt();
            clear();

            System.out.print("Digite o nome da coluna a ser atualizada: ");
            String nomeColuna = ler.next();

            // Verificar se a coluna existe na tabela
            ResultSet columns = metaData.getColumns(conn.getCatalog(), null, nomeTabela, nomeColuna);
            boolean colunaExiste = columns.next();
            columns.close();

            if (!colunaExiste) {
                // A coluna não existe, então adiciona a coluna à tabela
                String SQLAlterTable = "ALTER TABLE " + nomeTabela + " ADD COLUMN " + nomeColuna + " VARCHAR(60)";
                st.executeUpdate(SQLAlterTable);
                System.out.println("Coluna adicionada à tabela.");
            }

            System.out.print("Digite o novo valor: ");
            String novoValor = ler.next();

            String SQLAtualizarDados = "UPDATE " + nomeTabela + " SET " + nomeColuna + " = '" + novoValor + "' WHERE ID = " + idLinha;
            st.executeUpdate(SQLAtualizarDados);
            System.out.println("Data updated...");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    
    
    public void deleteData (String string) {

        try {
            System.out.println("---------- DELETAR LISTA ----------\n");

            System.out.print("listas disponíveis:\n\n");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});

            
            // Iterar sobre os resultados e imprimir os nomes das tabelas
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName+"\n");
            }
            System.out.print("\nDigite o nome da lista que quer excluir:");
            String tabela = ler.next(); 
            String SQLdeletarDados = "DROP TABLE "+ tabela+ ";";
            st.executeUpdate(SQLdeletarDados);
            clear();
            System.out.print("\nDeletado com sucesso");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    
    }
}
