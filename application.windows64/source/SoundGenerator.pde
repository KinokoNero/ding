import processing.sound.*;
SqrOsc sound;
float frequency = 300.0;
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
  
  void PlayDing(int index, int difficulty) {
    sound.freq(Sounds[index]);
    sound.amp(0.03);
    DingStopTime = millis() + 700/difficulty;
    isPlayingDing = true;
  }
  
  void StopDing() {
    if(isPlayingDing) {
      sound.amp(0);
      isPlayingDing = false;
    }
  }
  
  void CheckDingTime() {
    if(isPlayingDing) {
      if(millis() >= DingStopTime) {
        StopDing();
      }
    }
  }
  
  void PlayWrongDing() {
    sound.freq(150);
    sound.amp(0.05);
    DingStopTime = millis() + 500;
    isPlayingDing = true;
  }
}
