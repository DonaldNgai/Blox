package com.example.blox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class MySurface extends SurfaceView implements SurfaceHolder.Callback{

	
	//initial position of the image
	public static SecondThread thread;
	
	public MySurface(Context context) {
		super(context);
		getHolder().addCallback(this);
		thread = new SecondThread(getHolder(),this);
		
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//	if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			x = (int) event.getX();
//			y = (int) event.getY();
//		}
//		return true;
//	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		thread.setRunning(true);
		
		thread.start();

		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			
			try{
				
				thread.join();
				retry= false;
				
			}
			
			catch (InterruptedException e) {}
			
		}
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		  Paint back = new Paint();
		  Paint title = new Paint();
		  Paint numbers = new Paint();
		  Paint tile = new Paint();
		  
		  RectF rectangle = new RectF(MainActivity.backx, MainActivity.backy, MainActivity.backwidth, MainActivity.backheight);
		  RectF coverrectangle = new RectF(0 , MainActivity.height - 1210, MainActivity.width, 520);
		  RectF tilerectangle ;


		  
		  //title.setTypeface(custom_font);
		  title.setColor(Color.parseColor("#A0A0A0"));
		  title.setTextSize(190);
		  title.setAntiAlias(true);
		  
		  //numbers.setTypeface(custom_font);
		  numbers.setColor(Color.BLACK);
		  numbers.setTextSize(110);
		  numbers.setAntiAlias(true);
		  
		  tile.setColor(Color.WHITE);
		  
		  back.setColor(Color.parseColor("#CDCDB4"));
		  back.setTextSize(60);
		  back.setAntiAlias(true);

		  
		  
				  
		  //draw background
		  canvas.drawColor(Color.parseColor("#FFF5EE"));
		  
		  
		  //draw background box
		  canvas.drawRoundRect(rectangle, 100, 100, back);
		  
		  //draw all the tiles inside box
		  for (int i = 0; i < 16 ; i++)
		  {
			  tilerectangle = new RectF(MainActivity.xs[i], MainActivity.ys[i], MainActivity.xs[i]+MainActivity.tilewidth, MainActivity.ys[i]+MainActivity.tileheight);
			  

			  canvas.drawRoundRect(tilerectangle, 25, 25, tile);
			  
			  if (MainActivity.occupied[i])
			  {
				  if (MainActivity.values [i] > 1000)
				  {
					  numbers.setTextSize(67);
					  tile.setColor(Color.RED);
					  numbers.setColor(Color.parseColor("#ADFF2F"));
					  canvas.drawRoundRect(tilerectangle, 25, 25, tile);
					  canvas.drawText(Integer.toString(MainActivity.values[i]), MainActivity.numberx[i]-43, MainActivity.numbery[i]-10, numbers);
					  numbers.setTextSize(110);
					  numbers.setColor(Color.BLACK);
					  tile.setColor(Color.WHITE);
				  }
				  else if (MainActivity.values [i] > 100)
				  {
					  numbers.setTextSize(80);
					  numbers.setColor(Color.WHITE);
					  tile.setColor(Color.parseColor("#FFD700"));
					  canvas.drawRoundRect(tilerectangle, 25, 25, tile);
					  canvas.drawText(Integer.toString(MainActivity.values[i]), MainActivity.numberx[i]-35, MainActivity.numbery[i], numbers);
					  numbers.setTextSize(110);
					  numbers.setColor(Color.BLACK);
					  tile.setColor(Color.WHITE);

				  }
				  else if (MainActivity.values [i] > 10)
				  {
					  if (MainActivity.values[i] == 16)
					  {
						  tile.setColor(Color.parseColor("#FF0000"));
						  canvas.drawRoundRect(tilerectangle, 25, 25, tile);


					  }
					  else if (MainActivity.values[i] ==32)
					  {
						  tile.setColor(Color.parseColor("#FF6600"));
						  canvas.drawRoundRect(tilerectangle, 25, 25, tile);


					  }
					  else if (MainActivity.values[i] ==64)
					  {
						  tile.setColor(Color.parseColor("#FFCC11"));
						  canvas.drawRoundRect(tilerectangle, 25, 25, tile);


					  }
					  numbers.setTextSize(90);
					  numbers.setColor(Color.WHITE);
					  canvas.drawText(Integer.toString(MainActivity.values[i]), MainActivity.numberx[i]-20, MainActivity.numbery[i], numbers);
					  numbers.setTextSize(110);
					  numbers.setColor(Color.BLACK);
					  tile.setColor(Color.WHITE);

				  }
				  else
				  {
					  canvas.drawText(Integer.toString(MainActivity.values[i]), MainActivity.numberx[i], MainActivity.numbery[i], numbers);
				  }
			  
			  }
			  
		  }
		  canvas.drawText(Integer.toString(MainActivity.moves), MainActivity.width/2 -150 , MainActivity.height - 1225, title);
		  if (MainActivity.win)
		  {
			  canvas.drawText("Fuck Yeah", MainActivity.width/3 -150 , MainActivity.height- 130, numbers);
		  }
			  else if (MainActivity.nomoves==true){
  canvas.drawText("Game Over :(", MainActivity.width/3 -150 , MainActivity.height- 130, numbers);

}
		  
		  //display screen dimensions
		  canvas.drawText(Integer.toString(MainActivity.width)+" , "+Integer.toString(MainActivity.height), 0, 100, back);
		  canvas.drawText("BLOX", MainActivity.width/3 -100 , MainActivity.height - 1400, title);
		  
		  
		  
		  //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		  //renders image using x and y parameter x and y value is filled by the touch //event
		  //canvas.drawBitmap(image, x, y, null);
		}
	
}
