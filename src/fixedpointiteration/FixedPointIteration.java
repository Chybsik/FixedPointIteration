package fixedpointiteration;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.*;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.stream.*;

/**
 *
 * @author Timur
 */
public class FixedPointIteration {

    int power;
    double[][] a;
    double[] b;
    double[] vector;
    double[] prevVector;
    int steps;
    double q;
    double epsilon;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        int[] n = new int[]{1, 2, 3};
        int[] m = n.clone();
        m[0] = 10;
        System.out.println(n[0]);
         */

        FixedPointIteration i = new FixedPointIteration();
        i.Create();
        i.Calculate();
    }

    public void Create() {

        power = 4;
        epsilon = Math.pow(10, -12);

        /*
        try {
            power = InInt("Задайте размерность");
        } catch (IOException e) {
        }

        a = new double[power][power];

        for (int i = 0; i < power; i++) {
            for (int j = 0; j < power; j++) {
                try {
                    a[i][j] = InDouble("Введите i =" + i + " j =" + j + " элемент");
                } catch (IOException e) {
                }
            }
        }

        b = new double[power];

        for (int i = 0; i < power; i++) {
            try {
                b[i] = InDouble("Введите i =" + i + " элемент");
            } catch (IOException e) {
            }
        }
         */
        a = new double[4][4];

        a[0] = new double[]{10.9, 1.2, 2.1, 0.9};
        a[1] = new double[]{1.2, 11.2, 1.5, 2.5};
        a[2] = new double[]{2.1, 1.5, 9.8, 1.3};
        a[3] = new double[]{0.9, 2.5, 1.3, 12.1};

        b = new double[]{-7, 5.3, 10.3, 24.6};
    }

    public void Calculate() {
        //Создание альфа- и бета-матриц
        for (int i = 0; i < power; i++) {
            double temp = a[i][i];

            b[i] /= temp;
            for (int j = 0; j < power; j++) {
                a[i][j] /= temp;
                a[i][j] *= -1;
            }
            a[i][i] = 0;
        }

        vector = b.clone();

        //Проверка условия сходимости
        q = 0;
        for (int i = 0; i < power; i++) {
            double temp = 0;
            for (int j = 0; j < power; j++) {
                temp += Math.abs(a[i][j]);
            }
            if (q < temp) {
                q = temp;
            }
        }

        if (q >= 1) {
            System.out.println("Условие сходимости не выполнено");
            return;
        }

        //Исполнение метода Зейделя
        steps = 0;
        double apriorEstimation;
        do {
            prevVector = vector.clone();
            for (int i = 0; i < power; i++) {
                vector[i] = NewVector(i);
            }
            if (steps == 0) {
                System.out.println(ApriorEstimation(true));
            }
            steps++;
        } while (PosteriorEstimation() > epsilon);

        //Вывод
        for (int i = 0; i < power; i++) {
            System.out.println(vector[i]);
        }
        System.out.println(steps);
    }

    public double NewVector(int currentString) {
        double result = 0;

        for (int i = 0; i < power; i++) {
            result += a[currentString][i] * vector[i];
        }
        return result + b[currentString];
    }

    public double ApriorEstimation(boolean returnSteps) {
        double norm = 0;
        for (int i = 0; i < power; i++) {
            if (norm < Math.abs(vector[i] - prevVector[i])) {
                norm = Math.abs(vector[i] - prevVector[i]);
            }
        }

        double k = Math.log(epsilon / norm * (1 - q)) / Math.log(q);
        if (returnSteps) {
            return k;
        } else {
            return Math.pow(q, k) / (1 - q) * norm;
        }
    }

    public double PosteriorEstimation() {
        double norm = 0;
        for (int i = 0; i < power; i++) {
            if (norm < Math.abs(vector[i] - prevVector[i])) {
                norm = Math.abs(vector[i] - prevVector[i]);
            }
        }

        return q / (1 - q) * norm;
    }

    public static double InDouble(String message) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //System.out.print(message);
        //String s = br.readLine();
        System.out.println(message + " (Enter Double)");
        double d = 0;
        try {
            d = Double.parseDouble(br.readLine());
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Format!");
            System.exit(0);

        }
        return d;
    }

    public static int InInt(String message) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //System.out.print(message);
        //String s = br.readLine();
        System.out.println(message + " (Enter Integer)");
        int d = 0;
        try {
            d = Integer.parseInt(br.readLine());
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid Format!");
            System.exit(d);
        }
        return d;
    }


    /*
    public static class AddComponentOnJFrameAtRuntime extends JFrame implements ActionListener {

        JPanel panel;
        int currentPower = 3;

        public AddComponentOnJFrameAtRuntime() {
            super("Fixed Point Iteration");
            setLayout(new BorderLayout());
            this.panel = new JPanel();
            this.panel.setLayout(new GridLayout(3, 3));
            add(panel, BorderLayout.CENTER);
            JButton button = new JButton("Увеличить размерность");
            add(button, BorderLayout.SOUTH);
            button.addActionListener(this);
            JButton button2 = new JButton("Увеличить размерность");
            add(button, BorderLayout.SOUTH);
            //button2.addActionListener(this);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500, 500);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent evt) {
            for (int i = 0; i < Math.pow(currentPower + 1, 2) - Math.pow(currentPower, 2); i++) {
                JTextArea text = new JTextArea();
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                text.setBorder(border);
                this.panel.add(new JTextArea());
            }
            currentPower++;
            this.panel.revalidate();
            validate();
        }
        /*
        public void Reorginize(int power) {
            this.panel.setLayout(new GridLayout(power, power));
        }*/
    //}
    //*/
}
