package sample;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;

public class ItemManager {
  Item head, now;
  int size= 0;
  private TextField editText;//编辑框
  private TextArea showText;//大屏
  private MediaPlayer mediaPlayer;//播放器


  ItemManager(MediaPlayer mediaPlayer, TextField editText, TextArea showText){
    head=new Item();//头节点初始化
    now=new Item(ItemTypeEnum.START_TIME);
    link(head,now);
    this.mediaPlayer=mediaPlayer;
    this.editText=editText;
    this.showText=showText;
    focus();
    setText();
  }

  /**
   * 定位到下一个，下一个不存在自动创建
   */
  public void next() {
    if (now.next == null) {
      //根据type进行创建
      switch (now.type) {
        case START_TIME:startNext();
          break;
        case END_TIME:endNext();
          break;
        case INFO:infoNext();
          break;
      }
      size++;
    }
    now=now.next;
    setText();
    focus();
  }

  private void infoNext() {
  link(now,new Item(ItemTypeEnum.END_TIME));
  }

  private void endNext() {
    link(now,new Item(ItemTypeEnum.START_TIME));

  }

  private void startNext() {
    link(now,new Item(ItemTypeEnum.INFO));
  }
  private void link(Item pre,Item next){
    pre.next=next;
    next.pre=pre;
  }

  /**
   * 渲染信息
   */
  private void setText() {
    String text=now.value==null?"":now.value;
    editText.setText(text);
    showText.setText(getAllText());
  }

  /**
   *
   * @return 所有的字幕信息
   */
  private String getAllText() {
    Item cur=head.next;
    StringBuilder builder=new StringBuilder();
    while(cur!=null)
    {
      String text=cur.value==null?"空":cur.value;
      builder.append(text);
      builder.append(" ");
      builder.append("\n");
      cur=cur.next;
    }
    return builder.toString();

  }

  public void last() {
    if (now.pre == head) {
      return ;
    }
    now=now.pre;
    setText();
    focus();
  }

  /**
   * 给当前now添加信息
   */
  public void makeInfo() {
   //根据type来给now添加信息
    switch (now.type) {
      case START_TIME:timeOp();
        break;
      case END_TIME:timeOp();
        break;
      case INFO:InfoOp();
        break;
    }
    // 默认移动到下一个
    next();
  }


  private void InfoOp() {
    String word=editText.getText().trim();
    now.value=word;
  }

  private void timeOp() {
    double v = mediaPlayer.getCurrentTime().toMillis();
    now.value= TimeUtil.getFormatTime(v);
  }

  /**
   * 给编辑框聚焦
   */
  public void focus(){
     editText.requestFocus();
  }

  /**
   * 实时更新
   */
  public void setShowText(){
    String allText = getAllText();
    showText.setText(allText);
  }




}
