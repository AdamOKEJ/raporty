import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main extends JFrame {
    private JPanel panel;
    private JPanel dol;
    private JTextArea area;
    private JButton gen;
    private JProgressBar pb;

    public Main() {
        super("inf 04 generator raportow");
        setContentPane(panel);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menu();
        gen.addActionListener(e -> start());
    }

    private void menu() {
        JMenuBar bar = new JMenuBar();
        JMenu plik = new JMenu("plik");
        JMenuItem zapisz = new JMenuItem("zapisz raport");
        JMenuItem wyjscie = new JMenuItem("wyjscie");
        zapisz.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        zapisz.addActionListener(e -> zapisz());
        wyjscie.addActionListener(e -> System.exit(0));
        plik.add(zapisz);
        plik.add(wyjscie);
        bar.add(plik);
        setJMenuBar(bar);
    }

    private void start() {
        gen.setEnabled(false);
        area.setText("");
        pb.setValue(0);
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(300);
                    publish(i);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> l) {
                for (int i : l) {
                    area.append("wiersz raportu nr " + i + "\n");
                    pb.setValue(i * 10);
                }
            }

            @Override
            protected void done() {
                gen.setEnabled(true);
            }
        }.execute();
    }

    private void zapisz() {
        if (area.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "raport jest pusty", "uwaga", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("zapisz raport");
        fc.setFileFilter(new FileNameExtensionFilter("pliki tekstowe (*.txt)", "txt"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".txt")) {
            f = new File(f.getAbsolutePath() + ".txt");
        }
        try {
            Files.writeString(f.toPath(), area.getText());
            JOptionPane.showMessageDialog(this, "zapisano");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "blad zapisu: " + ex.getMessage(), "blad", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
