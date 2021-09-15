import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


class ClientConnect {

  BufferedReader reader;
  PrintWriter writer;
  Socket sock;
  String user;

  JTextArea messages_received;
  JTextField messsages_entered;
  JLabel title;
  JLabel heartitle;
  JFrame frame;
  JPanel panel;
  JButton send, disconnect, clear;


  public ClientConnect(String user) {

       this.user = user;
       frame = new JFrame("HearChat");
       panel = new JPanel();
       panel.setLayout(null);

       URL hear = this.getClass().getResource("/res/hear.png");
       ImageIcon hearicon = new ImageIcon(hear);
       title = new JLabel(hearicon);
       title.setSize(120,120);
       title.setLocation(50,10);
       panel.add(title);

       URL heart = this.getClass().getResource("/res/heartitle.png");
       ImageIcon hearticon = new ImageIcon(heart);
       heartitle = new JLabel(hearticon);
       heartitle.setSize(508,190);
       heartitle.setLocation(100,3);
       panel.add(heartitle);

       messages_received = new JTextArea(600,600);
       messages_received.setLineWrap(true);
       messages_received.setWrapStyleWord(true);
       messages_received.setEditable(false);
       messages_received.setFont(new Font("Arial", Font.BOLD, 20));
       messages_received.setForeground(Color.blue);

       JScrollPane qScroller = new JScrollPane(messages_received);
       qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
       qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       qScroller.setLocation(100,150);
       qScroller.setSize(500,220);
       panel.add(qScroller);

       messsages_entered = new JTextField();
       messsages_entered.setFont(new Font("Arial", Font.BOLD, 20));
       messsages_entered.setForeground(Color.blue);
       messsages_entered.setSize(500, 30);
       messsages_entered.setLocation(100, 400);
       panel.add(messsages_entered);

       connect_to_server();
       Thread read_messages = new Thread(new new_message());
       read_messages.start();

       send = new JButton(new ImageIcon(this.getClass().getResource("/res/send.png")));
       send.setFont(new Font("Arial", Font.PLAIN, 15));
       send.setBorder(BorderFactory.createEmptyBorder());
       send.setContentAreaFilled(false);
       send.setSize(180, 20);
       send.setLocation(130, 450);
       send.addActionListener(new SendButtonListener());
       panel.add(send);

       clear = new JButton(new ImageIcon(this.getClass().getResource("/res/clear.png")));
       clear.setFont(new Font("Arial", Font.PLAIN, 15));
       clear.setBorder(BorderFactory.createEmptyBorder());
       clear.setContentAreaFilled(false);
       clear.setSize(180, 20);
       clear.setLocation(250, 450);
       clear.addActionListener(new SendButtonListener());
       panel.add(clear);


       disconnect = new JButton(new ImageIcon(this.getClass().getResource("/res/disconnect.png")));
       disconnect.setFont(new Font("Arial", Font.PLAIN, 15));
       disconnect.setBorder(BorderFactory.createEmptyBorder());
       disconnect.setContentAreaFilled(false);
       disconnect.setSize(150, 20);
       disconnect.setLocation(400, 450);
       disconnect.addActionListener(new SendButtonListener());
       panel.add(disconnect);

       frame.add(panel);
       frame.setBounds(400, 400, 650, 550);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setResizable(false);
       frame.setVisible(true);
  }

  class SendButtonListener implements ActionListener {

       public void actionPerformed(ActionEvent e) {

           if(e.getSource() == send) {

               try {
                  writer.println(user+": "+messsages_entered.getText());
                  writer.flush();
                  messsages_entered.setText("");
                  messsages_entered.requestFocus();
               }catch(Exception ex) {
                  ex.printStackTrace();
               }

           }else if(e.getSource() == disconnect) {

                 try {
                    writer.println(user+": exited from chat.");
                    writer.flush();
                    sock.close();
                    System.exit(0);
                 }catch(IOException ex) {
                    ex.printStackTrace();
                 }

           } else if(e.getSource() == clear) {
                 messages_received.setText("");
           }
       }
  }


  private void connect_to_server() {

     try {
           sock = new Socket("127.0.0.1", 5000);
           InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
           reader = new BufferedReader(streamReader);
           writer = new PrintWriter(sock.getOutputStream());

     } catch(IOException ex) {
           ex.printStackTrace();
     }
  }

  class new_message implements Runnable {

        public void run() {

            String message;

            try {
                   while((message = reader.readLine()) != null) {
                      messages_received.append(message + "\n");
                   }

            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
  }

}


class Login extends JFrame implements ActionListener {

     private Container c;
     private JLabel login_icon;
     private JLabel username;
     private JTextField username_field;
     private JLabel information;
     private JLabel copyright;
     private JButton submit, reset;

     public Login() {

       setTitle("HearChat Login");
       setBounds(400, 400, 400, 400);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       setResizable(true);
       c = getContentPane();
       c.setLayout(null);

       URL url1 = this.getClass().getResource("/res/login.png");
       ImageIcon login_image_icon = new ImageIcon(url1);
       login_icon = new JLabel(login_image_icon);
       login_icon.setSize(200,200);
       login_icon.setLocation(80,3);
       c.add(login_icon);

       username = new JLabel("Username:");
       username.setForeground(Color.white);
       username.setBackground(Color.blue);
       username.setOpaque(true);
       username.setFont(new Font("monospaced", Font.BOLD, 20));
       username.setSize(110,40);
       username.setLocation(50,180);
       c.add(username);

       username_field = new JTextField();
       username_field.setFont(new Font("monospaced", Font.PLAIN, 20));
       username_field.setSize(190, 40);
       username_field.setLocation(170, 180);
       c.add(username_field);

       submit = new JButton("Connect");
       submit.setFont(new Font("Arial", Font.PLAIN, 15));
       submit.setSize(100, 20);
       submit.setLocation(100, 240);
       submit.addActionListener(this);
       c.add(submit);

       reset = new JButton("Reset");
       reset.setFont(new Font("Arial", Font.PLAIN, 15));
       reset.setSize(100, 20);
       reset.setLocation(200, 240);
       reset.addActionListener(this);
       c.add(reset);

       information = new JLabel("");
       information.setForeground(Color.black);
       information.setFont(new Font("monospaced", Font.BOLD, 15));
       information.setSize(200,40);
       information.setLocation(110,270);
       c.add(information);

       copyright = new JLabel("\u00a9 Copyright HearSecurity 2021");
       copyright.setForeground(Color.black);
       copyright.setFont(new Font("monospaced", Font.BOLD, 15));
       copyright.setSize(300,40);
       copyright.setLocation(60,300);
       c.add(copyright);

       setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

           if(e.getSource() == submit) {

                if(username_field.getText().isEmpty()) {
                    information.setText("Choose an user.");
                } else {
                    information.setText("");

                setinitialvalue();
                boolean value = checkifuserexist(username_field.getText());
                if(value == false) {
                   boolean connection = testConnection();
                   if(connection == true) {
                     writeusertofile(username_field.getText());
                     this.dispose();
                     ClientConnect client = new ClientConnect(username_field.getText());

                   } else {
                     information.setText("Connection failed.");
                   }

                } else {
                   information.setText("User already exists.");
                }
              }

           } else if(e.getSource() == reset) {
                username_field.setText("");
                information.setText("");
           }
    }

    public boolean checkifuserexist(String user) {

      boolean value = false;
      try {
        String content = new Scanner(new File("users.txt")).useDelimiter("\\Z").next();
        value = content.indexOf(user) != -1 ? true:false;
      }catch(FileNotFoundException e) {
          System.out.println("File not found");
      } catch(NoSuchElementException e) {
          System.out.println("No String found.");
      }
      return value;
    }

    public void setinitialvalue() {

      try(FileWriter fw = new FileWriter("users.txt", true);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter out = new PrintWriter(bw)) {
         out.println("hearchat");
      } catch (IOException e) {
         System.out.println("File not found.");
      }
    }

    public void writeusertofile(String user) {

      try(FileWriter fw = new FileWriter("users.txt", true);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter out = new PrintWriter(bw))
  {
      out.println(user);
  } catch (IOException e) {
      System.out.println("File not found.");
  }

    }

    public boolean testConnection() {

      Socket sock;

      try {
            sock = new Socket("127.0.0.1", 5000);
      } catch(IOException ex) {
            return false;
      } catch(NullPointerException ex) {
            return false;
      }

      return true;
    }
 }


public class HearClient {

    public static void main(String[] args) {

          Login login = new Login();
    }
}
