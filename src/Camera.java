import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends JFrame {

    private JLabel camerascreen;
    private JButton btncapture;
    private VideoCapture capture;
    private Mat image;
    private boolean clicked = false;

    public Camera(){

        setLayout(null);

        camerascreen = new JLabel();
        camerascreen.setBounds(0,0,640,560);
        add(camerascreen);

        btncapture = new JButton("capture");
        btncapture.setBounds(280,450,80,40);
        add(btncapture);

        btncapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });

        setSize(640,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void startCamera(){
        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;
        ImageIcon icon;


        while(true){
            capture.read(image);
            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);

            imageData = buf.toArray();
            icon = new ImageIcon(imageData);
            camerascreen.setIcon(icon);

            if(clicked){
                String name = JOptionPane.showInputDialog("Enter Image name");
                if(name == null){
                    name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
                }
                Imgcodecs.imwrite("image/"+ name + ".jpg", image);
                clicked = false;
            }
        }
    }
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Camera cam = new Camera();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cam.startCamera();
                    }
                }).start();
            }

        });
    }
}
