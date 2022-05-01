class Bell {
  int BellID;
  float BellX, BellY;
  float BellWidth, BellHeight;
  color BellLitColor;
  color BellDimColor;
  boolean isLit = false;
  boolean isDisplayed = false;
  
  Bell(int id, float xpos, float ypos, float bwidth, float bheight, color c) {
    BellID = id; //-------------------------------------------------------ID może okazać się niepotrzebne
    BellX = xpos;
    BellY = ypos;
    BellWidth = bwidth;
    BellHeight = bheight;
    BellLitColor = c;
    BellDimColor = lerpColor(0, c, 0.3);
  }
  
  void Display() {
    if(isLit) {
      fill(BellLitColor);
    }
    else {
      fill(BellDimColor);
    }
    rect(BellX, BellY, BellWidth, BellHeight);
    isDisplayed = true;
  }
  
  boolean isMouseOnBell() {
    if(mouseX > BellX && mouseX < (BellX + BellWidth) && mouseY > BellY && mouseY < (BellY + BellHeight)) {
      return true;
    }
    else {
      return false;
    }
  }
}
