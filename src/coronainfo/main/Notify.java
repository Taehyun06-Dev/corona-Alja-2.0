package coronainfo.main;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class Notify {

    public void notify_sound_and_msg(int input) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Double volume = new Updater().get_setting_volume();
        if (volume > 0.1){
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream("/coronainfo/resources/notice_mp3.wav")));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainControl.getMaximum()/100f*volume.floatValue());
            clip.start();
        }
        tray_notify(input);
    }
    public void tray_notify(int num){
        if (!new Updater().get_setting_notice()){
            return;
        }
        try{
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(ImageIO.read(getClass().getResource("/coronainfo/resources/info_64px.png")).getSource());
            TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Corona-Alja v2.0");
            tray.add(trayIcon);
            trayIcon.displayMessage("코로나-알자 v2.0", "신규 확진자 "+num+"명이 발생하였습니다.", TrayIcon.MessageType.WARNING);
        }catch(Exception ex){
            new Updater().notice_info("시스템 알림을 표시할 수 없습니다!", true);
        }
    }
}
