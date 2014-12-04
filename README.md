BEngine
=======

An easy to use engine for making 3D games written in java

############
# Template #
############

import engine.Window;

public Game()
{
  Window.createWindow(800, 600);
  
  while(!Window.isCloseRequested())
  {
    Window.update();
  }
  
  Window.close();
}

public static void main(String[] args)
{
  new Game();
}
