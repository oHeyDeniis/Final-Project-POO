package BlackJack.game.graphic;

import javax.swing.*;
import java.awt.*;

public class BetDialog extends JDialog {

    private float selectedBet = -1; // -1 indica que foi cancelado ou fechado sem valor
    private final JTextField txtCustomBet;
    private float maxBet;

    public BetDialog(JFrame parent, float maxBet) {
        super(parent, "Valor da aposta", true); // true ativa o comportamento modal
        this.maxBet = maxBet;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Impede fechar no 'X' sem apostar

        // --- Container Principal com preenchimento ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Instrução ---
        JLabel lblInstruction = new JLabel("Escolha um valor de aposta:");
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInstruction.setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(lblInstruction);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Botões com Valores Predefinidos ---
        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton btnChips20 = new JButton("$" + maxBet / 10); // 10% do maxBet
        JButton btnChips50 = new JButton("$" + maxBet / 2); // 50% do maxBet
        JButton btnChips100 = new JButton("$" + maxBet); // 100% do maxBet

        btnChips20.addActionListener(e -> selectPresetBet(maxBet / 10));
        btnChips50.addActionListener(e -> selectPresetBet(maxBet / 2));
        btnChips100.addActionListener(e -> selectPresetBet(maxBet));

        chipsPanel.add(btnChips20);
        chipsPanel.add(btnChips50);
        chipsPanel.add(btnChips100);
        mainPanel.add(chipsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Campo de Texto Customizado ---
        JPanel customPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblDollar = new JLabel("$");
        txtCustomBet = new JTextField(6);
        JButton btnConfirmCustom = new JButton("Confirmar Aposta");

        btnConfirmCustom.addActionListener(e -> confirmCustomBet());

        customPanel.add(lblDollar);
        customPanel.add(txtCustomBet);
        customPanel.add(btnConfirmCustom);
        mainPanel.add(customPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(parent); // Centraliza sobre a janela do jogo
    }

    // --- Ação para botões rápidos de Fichas ---
    private void selectPresetBet(float amount) {
        this.selectedBet = amount;
        dispose(); // Fecha o dialog liberando a thread
    }

    // --- Validação e Confirmação do campo de texto ---
    private void confirmCustomBet() {
        try {
            int amount = Integer.parseInt(txtCustomBet.getText().trim());
            if (amount <= -1 || amount > maxBet) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, insira um valor de aposta maior ou igual a zero e menor ou igual ao saldo ("
                                + maxBet
                                + ").",
                        "Valor Incorreto",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.selectedBet = amount;
            dispose(); // Fecha se o número for válido
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid round number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Getter para recuperar o valor final inserido ---
    public float getSelectedBet() {
        return this.selectedBet;
    }
}
