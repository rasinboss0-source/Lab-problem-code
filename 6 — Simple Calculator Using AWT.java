import java.awt.*;
import java.awt.event.*;

public class Main extends Frame implements ActionListener {

    TextField t1, t2, t3;
    Button add, div;

    Main() {
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new Label("First Number"));
        t1 = new TextField();
        add(t1);

        add(new Label("Second Number"));
        t2 = new TextField();
        add(t2);

        add = new Button("Add");
        div = new Button("Divide");
        add(add);
        add(div);

        add(new Label("Result"));
        t3 = new TextField();
        t3.setEditable(false);
        add(t3);

        add.addActionListener(this);
        div.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setTitle("Simple Calculator");
        setSize(350, 200);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        double a = Double.parseDouble(t1.getText());
        double b = Double.parseDouble(t2.getText());

        if (e.getSource() == add) {
            t3.setText("" + (a + b));
        } else {
            if (b == 0)
                t3.setText("Cannot divide by zero");
            else
                t3.setText("" + (a / b));
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
 
