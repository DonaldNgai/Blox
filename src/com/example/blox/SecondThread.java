package com.example.blox;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//thread to hold surface
	class SecondThread extends Thread {
		
		SurfaceHolder surfaceHolder;
		MySurface mySurface;
		static boolean _run = false;
		Canvas c;
		
		 public SecondThread(SurfaceHolder surfaceHolder, MySurface mySurface) {
			    
			 	this.surfaceHolder = surfaceHolder;
			    this.mySurface = mySurface;
			    
		 }
		 
		 public static void setRunning(boolean run){
			 
			_run = run;
			 
		 }
		 
		 
		 @Override
	     public void run() {
			 super.run();
			 while (_run) {
					
					try {
						c = surfaceHolder.lockCanvas(null);
						synchronized (surfaceHolder) {
							mySurface.onDraw(c);
						}
					} finally {
						if (c != null) {
							surfaceHolder.unlockCanvasAndPost(c);
						}
					}
			   }
			 
		 }
		
	}
