import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Server extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    public static final int POS_X = 30;
    public static final int POS_Y = 200;
    private static final String LABEL_START_SERVER_BUTTON = "Start Server";
    private static final String LABEL_STOP_SERVER_BUTTON = "Stop Server";
    private static final String TITLE_PROGRAM_MAIN_WINDOW = "SERVER";
    // название файла для хранения истории чата
    public static final String ARCHIVE = "archive";

    JButton btnStartServer;
    JButton btnStopServer;
    private boolean isServerRunning;

    JTextArea logging = new JTextArea();

    Client client;

    Server() {
        setSize(WIDTH, HEIGHT);
        setTitle(TITLE_PROGRAM_MAIN_WINDOW);
        setLocation(POS_X, POS_Y);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // формирование области логирования
        logging.setEditable(false);
        logging.setLineWrap(true);
        logging.setWrapStyleWord(true);
        add(new JScrollPane(logging));
        logging.setText(readChatHistory(ARCHIVE));

        add(createBottomPanel(), BorderLayout.SOUTH);

        client = new Client(this);

        setVisible(true);
    }

    /**
     * создание панели кнопок запуска и остановки сервера
     * @return Component Возвращает панель
     */
    private Component createBottomPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createStartServerButton());
        mainPanel.add(createStopServerButton());
        return mainPanel;
    }

    /**
     * создание кнопки запуска сервера и добавление слушателя к ней
     * @return Component Возвращает кнопку
     */
    private Component createStartServerButton() {
        btnStartServer = new JButton(LABEL_START_SERVER_BUTTON);
        btnStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerRunning) {
                    isServerRunning = true;
                    System.out.printf("Server running status: %s\n", isServerRunning);
                } else {
                    System.out.print("Server running status: currently running!\n");
                }
            }
        });
        return btnStartServer;
    }
    /**
     * создание кнопки остановки сервера и добавление слушателя к ней
     * @return Component Возвращает кнопку
     */
    private Component createStopServerButton() {
        btnStopServer = new JButton(LABEL_STOP_SERVER_BUTTON);
        btnStopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerRunning) {
                    isServerRunning = false;
                    System.out.printf("Server running status: %s\n", isServerRunning);
                } else {
                    System.out.print("Server running status: currently stopped!\n");
                }

            }
        });
        return btnStopServer;
    }
    /**
     * метод добавления текста в область логирования
     * @return void
     */
    public void log(String text) {
        logging.append(text);
    }
    /**
     * метод записи истории сообщений в файл
     * @return void
     */
    public void archive(String text) {
        try (FileWriter archive = new FileWriter(ARCHIVE, true)) {
            archive.append(text).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * метод чтения истории сообщений из файла
     * @return String
     */
    public String readChatHistory(String file) {
        int c;
        StringBuilder sb = new StringBuilder();
        try (FileReader archive = new FileReader(file)) {
            while ((c = archive.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            return sb.toString();
        }
    }

    /**
     * геттер имени файла с историей сообщений
     * @return String
     */
    public String getArchiveName() {
        return ARCHIVE;
    }
}