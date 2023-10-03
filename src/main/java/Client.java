import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class Client extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    private static final String TITLE_PROGRAM_MAIN_WINDOW = "CLIENT";
    public static final String IP_ADDRESS = "127.0.0.1";
    public static final String PORT = "8989";
    public static final String LOGIN = "Somebody";
    public static final String PASSWORD = "123456789";
    public static final String BUTTON_LOGIN = "Login";
    public static final String BUTTON_SEND = "Send";
    JButton btnLogin, btnSend;

    JTextField txtFieldIPAddress = new JTextField(IP_ADDRESS);
    JTextField txtFieldPort = new JTextField(PORT);
    JTextField txtFieldLogin = new JTextField(LOGIN);
    JPasswordField txtFieldPassword = new JPasswordField(PASSWORD);
    JTextField txtFieldMessage;
    JTextArea loggingArea;
    Server server;


    Client(Server server) {
        this.server = server;
        setSize(WIDTH, HEIGHT);
        setTitle(TITLE_PROGRAM_MAIN_WINDOW);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(createLoginPanel(), BorderLayout.NORTH);
        add(createTextArea());
        add(createSendPanel(), BorderLayout.SOUTH);
// чтение истории чата и добавление в область логирования клиента
        loggingArea.setText(server.readChatHistory(server.getArchiveName()));

        setVisible(true);
    }

    /**
     * создание панели, содержащей поля ввода информации для аутентификации на сервере
     * @return Component
     */
    private Component createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(2, 3));
        loginPanel.add(txtFieldIPAddress);
        loginPanel.add(txtFieldPort);
        loginPanel.add(txtFieldLogin);
        loginPanel.add(txtFieldPassword);
        loginPanel.add(createLoginButton());
        return loginPanel;
    }
    /**
     * создание панели, содержащей поля ввода и отправки сообщений чата
     * @return Component sendPanel
     */
    private Component createSendPanel() {
        JPanel sendPanel = new JPanel(new GridLayout(1, 2));
        sendPanel.add(createTextField());
        sendPanel.add(createSendButton());
        return sendPanel;
    }

    /**
     * создание кнопки Login и создание слушателя для добавления информации о попытке аутентификации в чат клиента,
     * в область логирования на сервере, а также записи в файл истории сообщений чата
     * @return Component btnLogin
     */
    private Component createLoginButton() {
        btnLogin = new JButton(BUTTON_LOGIN);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append(txtFieldIPAddress.getText()).append(";");
                sb.append(txtFieldPort.getText()).append(";");
                sb.append(txtFieldLogin.getText()).append(";");
                sb.append("#".repeat(txtFieldPassword.getPassword().length));

                if (!Objects.equals(sb.toString(), "")) {
                    loggingArea.append("Logging attempt: " + sb + "\n");
                    txtFieldMessage.setText("");
                    server.log("Logging attempt: " + sb + "\n");
                    server.archive(sb.toString());
                }
            }
        });
        return btnLogin;
    }
    /**
     * создание кнопки Send и создание слушателя для добавления сообщения в чат клиента,
     * в область логирования на сервере, а также записи в файл истории сообщений чата
     * @return Component btnSend
     */
    private Component createSendButton() {
        btnSend = new JButton(BUTTON_SEND);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = txtFieldMessage.getText();
                if (!Objects.equals(text, "")) {
                    loggingArea.append(text + "\n");
                    server.log(text);
                    server.archive(text);
                    txtFieldMessage.setText("");
                }
            }
        });
        return btnSend;
    }

    /**
     * создание текстового поля для ввода сообщений и добавление слушателя нажатия кнопки Enter
     * для добавления сообщения в чат клиента,
     * в область логирования на сервере, а также записи в файл истории сообщений чата
     * @return Component txtFieldMessage
     */
    private Component createTextField() {
        txtFieldMessage = new JTextField();
        setFocusable(true);
        txtFieldMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String text = txtFieldMessage.getText();
                if (e.getKeyChar() == KeyEvent.VK_ENTER && !Objects.equals(text, "")) {
                    loggingArea.append(text + "\n");
                    server.log(text);
                    server.archive(text);
                    txtFieldMessage.setText("");
                }
            }
        });
        return txtFieldMessage;
    }

    /**
     * создание области логирования сообщений чата клиента
     * @return Component JScrollPanel logging area
     */
    private Component createTextArea() {
        loggingArea = new JTextArea();
        loggingArea.setEditable(false);
        return new JScrollPane(loggingArea);
    }
}