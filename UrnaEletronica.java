import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class UrnaEletronica extends JFrame {

    private final JTextField campoNumero = new JTextField();
    private final JLabel foto = new JLabel("", SwingConstants.CENTER);
    private final JLabel nome = new JLabel("SEU VOTO", SwingConstants.CENTER);
    private final JLabel partido = new JLabel("", SwingConstants.CENTER);
    private final JLabel mensagem = new JLabel(
        "Digite o número do candidato", SwingConstants.CENTER
    );

    private boolean votoEmBranco = false;

    private final String[][] candidatos = {
        {"13", "Luiz Inácio Lula da Silva", "PT - Partido dos Trabalhadores", "lula.jpeg"},
        {"22", "Candidato fictício B", "Partido Beta", "candidato2.jpg"},
        {"44", "Candidato fictício C", "Partido Gama", "candidato3.jpg"}
    };

    public UrnaEletronica() {
        setTitle("Urna Eletrônica - Simulação");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBorder(new EmptyBorder(18, 18, 18, 18));
        principal.setBackground(new Color(235, 235, 230));

        JLabel titulo = new JLabel(
            "JUSTIÇA ELEITORAL — SIMULAÇÃO",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 23));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel tela = new JPanel(new BorderLayout(15, 10));
        tela.setBackground(Color.WHITE);
        tela.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel dados = new JPanel(new BorderLayout(5, 10));
        dados.setBackground(Color.WHITE);

        JLabel instrucao = new JLabel("NÚMERO:");
        instrucao.setFont(new Font("Arial", Font.BOLD, 16));

        campoNumero.setFont(new Font("Monospaced", Font.BOLD, 30));
        campoNumero.setEditable(false);
        campoNumero.setHorizontalAlignment(JTextField.CENTER);
        campoNumero.setBackground(Color.WHITE);
        campoNumero.setPreferredSize(new Dimension(220, 48));

        JPanel entrada = new JPanel(new BorderLayout(8, 8));
        entrada.setBackground(Color.WHITE);
        entrada.add(instrucao, BorderLayout.NORTH);
        entrada.add(campoNumero, BorderLayout.CENTER);

        JPanel identificacao = new JPanel(new GridLayout(3, 1, 5, 5));
        identificacao.setBackground(Color.WHITE);

        nome.setFont(new Font("Arial", Font.BOLD, 19));
        partido.setFont(new Font("Arial", Font.PLAIN, 14));
        mensagem.setFont(new Font("Arial", Font.PLAIN, 14));

        identificacao.add(nome);
        identificacao.add(partido);
        identificacao.add(mensagem);

        foto.setPreferredSize(new Dimension(180, 210));
        foto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        foto.setText("Foto do candidato");

        dados.add(entrada, BorderLayout.NORTH);
        dados.add(identificacao, BorderLayout.CENTER);

        tela.add(dados, BorderLayout.CENTER);
        tela.add(foto, BorderLayout.EAST);

        JPanel teclado = new JPanel(new GridLayout(4, 3, 7, 7));
        teclado.setPreferredSize(new Dimension(235, 270));
        teclado.setBackground(new Color(235, 235, 230));

        for (int i = 1; i <= 9; i++) {
            final String numero = String.valueOf(i);
            JButton botao = criarBotao(numero, new Color(45, 45, 45));
            botao.addActionListener(e -> digitar(numero));
            teclado.add(botao);
        }

        JButton vazio = criarBotao(" ", Color.GRAY);
        vazio.setEnabled(false);
        teclado.add(vazio);

        JButton zero = criarBotao("0", new Color(45, 45, 45));
        zero.addActionListener(e -> digitar("0"));
        teclado.add(zero);

        JButton apagar = criarBotao("CORRIGE", new Color(210, 65, 45));
        apagar.setFont(new Font("Arial", Font.BOLD, 12));
        apagar.addActionListener(e -> corrigir());
        teclado.add(apagar);

        JPanel ladoDireito = new JPanel(new BorderLayout(8, 8));
        ladoDireito.setBackground(new Color(235, 235, 230));
        ladoDireito.add(teclado, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(2, 1, 7, 7));
        acoes.setBackground(new Color(235, 235, 230));

        JButton branco = criarBotao("BRANCO", Color.WHITE);
        branco.setForeground(Color.BLACK);
        branco.addActionListener(e -> votarBranco());

        JButton confirma = criarBotao("CONFIRMA", new Color(30, 150, 65));
        confirma.setPreferredSize(new Dimension(200, 55));
        confirma.addActionListener(e -> confirmar());

        acoes.add(branco);
        acoes.add(confirma);
        ladoDireito.add(acoes, BorderLayout.SOUTH);

        principal.add(tela, BorderLayout.CENTER);
        principal.add(ladoDireito, BorderLayout.EAST);

        setContentPane(principal);
        atualizarCandidato();
        setVisible(true);
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 19));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void digitar(String numero) {
        if (votoEmBranco || campoNumero.getText().length() >= 2) {
            return;
        }

        campoNumero.setText(campoNumero.getText() + numero);
        atualizarCandidato();
    }

    private void atualizarCandidato() {
        String numero = campoNumero.getText();

        nome.setText("VOTO PARA");
        partido.setText("");
        foto.setIcon(null);
        foto.setText("Foto do candidato");
        mensagem.setText("Confira os dados do candidato");

        if (numero.isEmpty()) {
            nome.setText("SEU VOTO");
            mensagem.setText("Digite o número do candidato");
            return;
        }

        for (String[] c : candidatos) {
            if (c[0].equals(numero)) {
                nome.setText(c[1]);
                partido.setText(c[2]);

                File arquivo = new File(c[3]);
                if (arquivo.exists()) {
                    ImageIcon original = new ImageIcon(c[3]);
                    Image imagem = original.getImage().getScaledInstance(
                        170, 200, Image.SCALE_SMOOTH
                    );
                    foto.setIcon(new ImageIcon(imagem));
                    foto.setText("");
                } else {
                    foto.setText("<html><center>Imagem não encontrada<br>"
                        + c[3] + "</center></html>");
                }
                return;
            }
        }

        if (numero.length() == 2) {
            nome.setText("VOTO NULO");
            partido.setText("Número não cadastrado");
            mensagem.setText("Número inválido");
        }
    }

    private void corrigir() {
        campoNumero.setText("");
        votoEmBranco = false;
        atualizarCandidato();
    }

    private void votarBranco() {
        votoEmBranco = true;
        campoNumero.setText("");
        nome.setText("VOTO EM BRANCO");
        partido.setText("");
        foto.setIcon(null);
        foto.setText("Voto em branco");
        mensagem.setText("Pressione CONFIRMA para registrar");

        int escolha = JOptionPane.showConfirmDialog(
            this,
            "Deseja votar em BRANCO?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION
        );

        if (escolha == JOptionPane.YES_OPTION) {
            registrar("VOTO EM BRANCO");
        } else {
            corrigir();
        }
    }

    private void confirmar() {
        String numero = campoNumero.getText();
        String escolhido = null;

        for (String[] c : candidatos) {
            if (c[0].equals(numero)) {
                escolhido = c[1];
                break;
            }
        }

        if (escolhido == null) {
            JOptionPane.showMessageDialog(
                this,
                "Número inválido. Digite um candidato cadastrado.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int escolha = JOptionPane.showConfirmDialog(
            this,
            "Candidato: " + escolhido
                + "\nNúmero: " + numero
                + "\n\nConfirma seu voto?",
            "Confirmação do voto",
            JOptionPane.YES_NO_OPTION
        );

        if (escolha == JOptionPane.YES_OPTION) {
            registrar(escolhido);
        }
    }

    private void registrar(String candidato) {
        JOptionPane.showMessageDialog(
            this,
            "FIM!\nVoto registrado na simulação.",
            "Voto confirmado",
            JOptionPane.INFORMATION_MESSAGE
        );

        campoNumero.setText("");
        votoEmBranco = false;
        nome.setText("FIM");
        partido.setText(candidato);
        mensagem.setText("Voto registrado na simulação");
        foto.setIcon(null);
        foto.setText("FIM");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UrnaEletronica::new);
    }
}
