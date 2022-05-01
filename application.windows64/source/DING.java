import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DING extends PApplet {

Bell [] Bell = new Bell[16];
SoundGenerator Sound;
boolean GameStarted = false;
boolean DifficultyChosen = false;
boolean StartPlaying = false;
int Score = 0;
int Difficulty = -1;
int timeOut = 0;
int [] Sequence;
int SequencePosition = 0;
int UserSequencePosition = 0;
boolean Defeat = false;
boolean DisplayTutorial = false;
boolean SequenceComplete = false;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Setup
public void setup() {
  
  Bell[0] = new Bell(0,0*width/4,0*height/4,width/4,height/4,0xff00A2FF); //light blue
  Bell[1] = new Bell(1,1*width/4,0*height/4,width/4,height/4,0xff820078); //dark violet
  Bell[2] = new Bell(2,2*width/4,0*height/4,width/4,height/4,0xff00FF87); //seafoam
  Bell[3] = new Bell(3,3*width/4,0*height/4,width/4,height/4,0xff000000); //black
  
  Bell[4] = new Bell(4,0*width/4,1*height/4,width/4,height/4,0xff648D00); //dark lime
  Bell[5] = new Bell(5,1*width/4,1*height/4,width/4,height/4,0xffFF0000); //red
  Bell[6] = new Bell(6,2*width/4,1*height/4,width/4,height/4,0xff00FF00); //green
  Bell[7] = new Bell(7,3*width/4,1*height/4,width/4,height/4,0xff00FFFF); //cyan
  
  Bell[8] = new Bell(8,0*width/4,2*height/4,width/4,height/4,0xffFF9B00); //orange
  Bell[9] = new Bell(9,1*width/4,2*height/4,width/4,height/4,0xff0000FF); //blue
  Bell[10] = new Bell(10,2*width/4,2*height/4,width/4,height/4,0xffFFF000); //yellow
  Bell[11] = new Bell(11,3*width/4,2*height/4,width/4,height/4,0xffFF00FF); //magenta
  
  Bell[12] = new Bell(12,0*width/4,3*height/4,width/4,height/4,0xff6100FF); //violet
  Bell[13] = new Bell(13,1*width/4,3*height/4,width/4,height/4,0xff108200); //dark green
  Bell[14] = new Bell(14,2*width/4,3*height/4,width/4,height/4,0xffFF0080); //pink
  Bell[15] = new Bell(15,3*width/4,3*height/4,width/4,height/4,0xffFFFFFF); //white
  Sound = new SoundGenerator(this);
}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Keyboard
public void keyPressed() {
  if(key=='q') {
    Restart();
  }
  
  if(GameStarted==false && DisplayTutorial == false && key==ENTER) {
    GameStarted = true;
  }
  else if(GameStarted==false && DisplayTutorial == false && key=='h') {
    DisplayTutorial = true;
  }
  
  if(GameStarted==true && DifficultyChosen==true && Defeat == true && key=='r') {
    StartPlaying = false;
    Score = 0;
    Defeat = false;
    SequencePosition = 0;
    DisplayTutorial = false;
  }
  
  if(GameStarted==true && DifficultyChosen==true && key==ENTER) {
    StartPlaying = true;
  }
  
  if(GameStarted==true && DifficultyChosen==false && key=='1') {
    Difficulty=1;
    DifficultyChosen=true;
    Sequence = new int[3+Difficulty];
  }
  if(GameStarted==true && DifficultyChosen==false && key=='2') {
    Difficulty=2;
    DifficultyChosen=true;
    Sequence = new int[3+Difficulty];
  }
  if(GameStarted==true && DifficultyChosen==false && key=='3') {
    Difficulty=3;
    DifficultyChosen=true;
    Sequence = new int[3+Difficulty];
  }
}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Mouse
public void mousePressed() {
  for(int i=0; i<16; i++) {
    if(GameStarted==true && DifficultyChosen==true && Bell[i].isMouseOnBell()==true && Bell[i].isDisplayed==true && Defeat == false && StartPlaying == true && SequenceComplete == true) {
      if(Bell[i].BellID == Sequence[UserSequencePosition]) {
        Bell[i].isLit=true;
        Sound.PlayDing(i, Difficulty);
        if(UserSequencePosition < Sequence.length) {
          UserSequencePosition++;
        }
        if(UserSequencePosition == Sequence.length) {
          UserSequencePosition = 0;
          ScoreCount(Difficulty);
        }
      }
      else if(Bell[i].BellID != Sequence[UserSequencePosition]) {
        UserSequencePosition = 0;
        Bell[i].isLit=true;
        Sound.PlayWrongDing();
        Defeat = true;
      }
    }
  }
}
public void mouseReleased() {
  if(isPlayingDing) {
    Sound.CheckDingTime();
  }
  if(isPlayingDing==false) {
    for(int i=0; i<16; i++) {
      Bell[i].isLit=false;
    }
  }
}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Functions
public int ChooseRandomBell(int Difficulty) {
  int RandomBell = -1;
  if(Difficulty==1) {
    RandomBell = PApplet.parseInt(random(5,11));
    if(RandomBell==7) RandomBell = PApplet.parseInt(random(5,7));
    if(RandomBell==8) RandomBell = PApplet.parseInt(random(9,11));
  }
  if(Difficulty==2) {
    RandomBell = PApplet.parseInt(random(4,12));
  }
  if(Difficulty==3) {
    RandomBell = PApplet.parseInt(random(0,16));
  }
  return RandomBell;
}

public void Sequencer(int Difficulty) {
  if(millis() >= timeOut) {
    if(SequencePosition != Sequence.length) {
      int RandomBell = ChooseRandomBell(Difficulty);
      Sequence[SequencePosition] = RandomBell;
      SequencePosition++;
      Bell[RandomBell].isLit=true;
      Sound.PlayDing(RandomBell, Difficulty);
      timeOut = millis() + 700/Difficulty + 70/Difficulty;
    }
    else {SequenceComplete = true;}
  }
}

public void ScoreCount(int Difficulty) {
  Score += (pow(4,Difficulty));
  SequencePosition = 0;
  if(millis() >= timeOut) {
    timeOut = millis() + 1000;
  }
  SequenceComplete = false;
}

public void Restart() {
  GameStarted = false;
  DifficultyChosen = false;
  StartPlaying = false;
  Difficulty = 0;
  Score = 0;
  Defeat = false;
  SequencePosition = 0;
  DisplayTutorial = false;
}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Draw()
public void draw() {
  background(255, 255, 255);
  Sound.CheckDingTime();
  if(isPlayingDing == false) {
    for(int i=0; i<16; i++) {
    Bell[i].isLit=false;
    }
  }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Splash screen
  if(GameStarted==false && DisplayTutorial == false) {
    fill(0, 0, 0);
    textSize(width/80);
    text("Wskazówka: Zanim zaczniesz grę zapoznaj się z samouczkiem.\nJeśli nie możesz rozpocząć gry, wyświetlić samouczka lub wyjść z gry, kliknij lewym przyciskiem myszy w dowolne miejsce na ekranie i spróbuj jeszcze raz.", width/2, height/10); 
    textAlign(CENTER,CENTER);
    
    textSize(width/10);
    textAlign(CENTER,CENTER);
    fill(255,0,0);
    text("D", 5*width/16, 4*height/10);
    fill(0,255,0);
    text("I", 7*width/16, 4*height/10);
    fill(0,0,255);
    text("N", 9*width/16, 4*height/10);
    fill(255,255,0);
    text("G", 11*width/16, 4*height/10);
    
    fill(0, 0, 0);
    textSize(width/30);
    text("Naciśnij ENTER aby zagrać!", width/2, 6*height/10);
    
    textSize(width/80);
    text("Naciśnij H aby wyświetlić samouczek.\nAby wyjść z gry naciśnij przycisk ESC i poczekaj aż gra się zamknie.", width/2, 9*height/10); 
  }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Tutorial
  if(GameStarted==false && DisplayTutorial == true) {
    fill(0, 0, 0);
    textSize(width/50);
    text("Gra polega na zapamiętywaniu kolejności,\nw jakiej każdy z dzwoneczków zadzwoni\na następnie odtworzeniu tej samej\nmelodii przyciskając odpowiednie dzwoneczki.\nPoczekaj aż dzwoneczki skończą grać\na następnie odtwórz zagraną melodię.\n\nZa poprawne odtworzenie melodii dostaniesz\nokreśloną ilość punktów zależną od wybranego\npoziomu trudności.\n\nGra kończy się gdy odtworzysz błędną melodię.\n\nPamiętaj: W każdej chwili możesz wrócić do menu głównego przyciskając klawisz 'Q'.", width/2, 7*height/16); 
    textSize(width/80);
    text("Naciśnij Q aby wrócić do menu głównego.", width/2, 9*height/10); 
    textAlign(CENTER,CENTER);
    if(key==ENTER) {
      DisplayTutorial = false;
    }
  }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Choose difficulty
  if(GameStarted==true && DifficultyChosen==false) {
    fill(0, 0, 0);
    textAlign(CENTER,CENTER);
    textSize(width/30);
    text("Wybierz poziom trudności:\n1.Łatwy\n2.Normalny\n3.Trudny", width/2, height/2); 
    textSize(width/80);
    text("Wskazówka: Naciśnij odpowiedni numer na klawiaturze.", width/2, 9*height/10);
  }
  if(GameStarted==true && DifficultyChosen==true && Defeat == false) {
    fill(0, 0, 0);
    textSize(width/90);
    textAlign(RIGHT);
    text("Punkty: "+Score, 98.5f*width/100, 98*height/100);
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Easy difficulty
    if(GameStarted==true && DifficultyChosen==true && Difficulty==1) {
      if(GameStarted==true && DifficultyChosen==true && Difficulty==1 && StartPlaying == true) {
        Sequencer(Difficulty);
        Bell[5].Display();
        Bell[6].Display();
        Bell[9].Display();
        Bell[10].Display();
      }
      else {
        Bell[5].Display();
        Bell[6].Display();
        Bell[9].Display();
        Bell[10].Display();
        fill(0, 0, 0);
        textAlign(CENTER,CENTER);
        textSize(width/30);
        text("Naciśnij ENTER aby zacząć grę.", width/2, height/2); 
      }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Normal difficulty  
    if(GameStarted==true && DifficultyChosen==true && Difficulty==2) {
      if(GameStarted==true && DifficultyChosen==true && Difficulty==2 && StartPlaying == true) {
        Sequencer(Difficulty);
        for(int i=4; i<12; i++) {
          Bell[i].Display();
        }
      }
      else {
        for(int i=4; i<12; i++) {
          Bell[i].Display();
        }
        fill(0, 0, 0);
        textAlign(CENTER,CENTER);
        textSize(width/30);
        text("Naciśnij ENTER aby zacząć grę.", width/2, height/2); 
      }
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------Hard difficulty  
    if(GameStarted==true && DifficultyChosen==true && Difficulty==3) {
      if(GameStarted==true && DifficultyChosen==true && Difficulty==3 && StartPlaying == true) {
        Sequencer(Difficulty);
        for(int i=0; i<16; i++) {
          Bell[i].Display();
        }
      }
      else {
        for(int i=0; i<16; i++) {
          Bell[i].Display();
        }
        fill(0, 0, 0);
        textAlign(CENTER,CENTER);
        textSize(width/30);
        text("Naciśnij ENTER aby zacząć grę.", width/2, height/2); 
      }
    }
  }
  else if(GameStarted==true && DifficultyChosen==true && Defeat == true) {
    textAlign(CENTER,CENTER);
    fill(255, 0, 0);
    textSize(width/20);
    text("Koniec gry :(", width/2, height/2);
    fill(0, 0, 0);
    textSize(width/60);
    text("Liczba zdobytych punktów: "+Score, width/2, 3*height/5); 
    textSize(width/80);
    text("Naciśnij R aby spróbować ponownie.\nNaciśnij Q aby wrócić do menu głównego.", width/2, 9*height/10); 
  }
}
class Bell {
  int BellID;
  float BellX, BellY;
  float BellWidth, BellHeight;
  int BellLitColor;
  int BellDimColor;
  boolean isLit = false;
  boolean isDisplayed = false;
  
  Bell(int id, float xpos, float ypos, float bwidth, float bheight, int c) {
    BellID = id; //-------------------------------------------------------ID może okazać się niepotrzebne
    BellX = xpos;
    BellY = ypos;
    BellWidth = bwidth;
    BellHeight = bheight;
    BellLitColor = c;
    BellDimColor = lerpColor(0, c, 0.3f);
  }
  
  public void Display() {
    if(isLit) {
      fill(BellLitColor);
    }
    else {
      fill(BellDimColor);
    }
    rect(BellX, BellY, BellWidth, BellHeight);
    isDisplayed = true;
  }
  
  public boolean isMouseOnBell() {
    if(mouseX > BellX && mouseX < (BellX + BellWidth) && mouseY > BellY && mouseY < (BellY + BellHeight)) {
      return true;
    }
    else {
      return false;
    }
  }
}

SqrOsc sound;
float frequency = 300.0f;
float [] Sounds = new float [16];
int DingStopTime;
boolean isPlayingDing = false;

class SoundGenerator {
  SoundGenerator(PApplet p) {
    for(int i=0; i<16; i++) {
      Sounds[i] = frequency;
      frequency += 100;
    }
    sound = new SqrOsc(p);
    sound.play();
    sound.amp(0);
  }
  
  public void PlayDing(int index, int difficulty) {
    sound.freq(Sounds[index]);
    sound.amp(0.03f);
    DingStopTime = millis() + 700/difficulty;
    isPlayingDing = true;
  }
  
  public void StopDing() {
    if(isPlayingDing) {
      sound.amp(0);
      isPlayingDing = false;
    }
  }
  
  public void CheckDingTime() {
    if(isPlayingDing) {
      if(millis() >= DingStopTime) {
        StopDing();
      }
    }
  }
  
  public void PlayWrongDing() {
    sound.freq(150);
    sound.amp(0.05f);
    DingStopTime = millis() + 500;
    isPlayingDing = true;
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DING" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
