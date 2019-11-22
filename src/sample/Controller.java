package sample;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Time;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {

  @FXML
  private MediaView mediaView;
  @FXML
  private Slider playBar;
  @FXML
  private Slider volumeBar;
  @FXML
  private HBox parent;
  @FXML
  private TextField editText;
  @FXML
  private TextArea showText;
  @FXML
  private Label currentTimeShow;
  @FXML
  private Label totalTimeShow;

  private MediaPlayer mediaPlayer;//播放器
  private double totalTime;//视频总长
  private ItemManager itemManager;
  private double distance = 5 * 1000;//快进，后退时间--5秒

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
          volumeBar.setValue(80);
          volumeBar.setShowTickLabels(true);
          parent.requestFocus();
          totalTimeShow.setText(TimeUtil.getFormatTime(totalTime));
          itemManager = new ItemManager(this.mediaPlayer, editText, showText);//manager初始化
        }
    );

    mediaPlayer.currentTimeProperty().addListener((a, b, c) -> {
      double now = mediaPlayer.getCurrentTime().toMillis();
      playBar.setValue(now);
      //视频时间同步更新
      currentTimeShow.setText(TimeUtil.getFormatTime(now));//时间条显示
    });
    mediaPlayer.volumeProperty().bind(volumeBar.valueProperty());//音量绑定


  }


  @FXML
  public void play() {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
      mediaPlayer.pause();
    else
      mediaPlayer.play();
  }

  @FXML
  public void forward() {
    double currentTime = mediaPlayer.getCurrentTime().toMillis();
    double toTime = currentTime + distance;//抵达的时间
    Duration duration = new Duration(toTime);
    if (toTime >= totalTime) {
      mediaPlayer.stop();
    } else

      mediaPlayer.seek(new Duration(toTime));
  }

  @FXML
  public void back() {
    double currentTime = mediaPlayer.getCurrentTime().toMillis();
    double toTime = currentTime - distance;//抵达的时间
    if (toTime < 0) {
      toTime = 0;
    }
    mediaPlayer.seek(new Duration(toTime));
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
  }

  @FXML
  public void keyPress(KeyEvent event) {
    System.out.println(event.getCode().getName());
    /**
     * 快进，后退，暂停/播放，下一个，上一个，进行编辑，
     */
    String name = event.getCode().getName();
    System.out.println(name);
    if (name.equals("Tab")) {
      play();
    } else if (name.equals("Up")) {
      itemManager.last();
    } else if (name.equals("Down")) {
      itemManager.next();
    } else if (name.equals("Enter")) {
      itemManager.makeInfo();
    } else if (name.equals("")) {
      //前进
    } else if (name.equals("")) {
      //后退
    }


  }


  @FXML
  public void export() {

  }


}
