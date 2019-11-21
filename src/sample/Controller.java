package sample;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class Controller implements Initializable {
  @FXML
  private Button startTime;
  @FXML
  private Button endTime;
  @FXML
  private Button play;
  @FXML
  private Button pause;
  @FXML
  private MediaView mediaView;
  @FXML
  private Label mediaTimeShow;
  @FXML
  private Slider playBar;
  @FXML
  private Slider volumeBar;
  @FXML
  private HBox parent;

  private MediaPlayer mediaPlayer;//播放器
  private double totalTime;//视频总长

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //读取文件
    //MediaView设置
    Media media = new Media(getClass().getResource("test.mp4").toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaView.setMediaPlayer(mediaPlayer);
    mediaPlayer.setOnReady(() -> {
          //初始化
          mediaPlayer.pause();//暂停
          this.mediaPlayer = mediaPlayer;
          this.totalTime = mediaPlayer.getTotalDuration().toMillis();
          playBar.setMax(this.totalTime);
          playBar.setMin(0);
          playBar.setBlendMode(BlendMode.RED);
          volumeBar.setValue(80);
          volumeBar.setShowTickLabels(true);
          parent.requestFocus();

        }
    );
//    playBar.valueProperty().addListener((a,b,c)->{
//      mediaPlayer.seek(new Duration(playBar.getValue()));
//    });
    mediaPlayer.currentTimeProperty().addListener((a, b, c) -> {
      playBar.setValue(mediaPlayer.getCurrentTime().toMillis());
    });
//    parent.setOnKeyReleased(a->{
//      System.out.println(a.getCode());
//    });
  }

  @FXML
  public void play() {
    mediaPlayer.play();

  }

  @FXML
  public void pause() {
    mediaPlayer.pause();
  }

  /**
   * 滚动条控制播放
   */
  @FXML
  public void playScroll() {
    double value = playBar.getValue();
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
      mediaPlayer.pause();
    mediaPlayer.seek(new Duration(value));
    System.out.println("tets");
  }

  @FXML
  public void keyPress(KeyEvent event){
    System.out.println(event.getCode().getName());
    /**
     * 快进，后退，暂停/播放，下一个，上一个，进行编辑，
     */

  }

  public boolean check() {
    if (mediaPlayer != null)
      return true;
    return false;
  }


}
