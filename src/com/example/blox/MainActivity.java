package com.example.blox;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	//height and width of screen
	static int width;
	static int height;
	//surface view for dynamic drawing
	public MySurface surfaceView;
	//for coding purposes
	boolean test=true;
	//initial x and y of android
	public static int [] xs = new int [16];
	public static int [] ys = new int [16];
	public static boolean [] occupied = new boolean [16];
	public static int [] values = new int [16];
	public static int [] numberx = new int [16];
	public static int [] numbery = new int [16];
 	public static int gap = 50;
	public static int tilewidth;
	public static int tileheight;
	public static int backx ;
	public static int backy ;
	public static int backwidth;
	public static int backheight;
	public static int x;
	public static int y ;
	public static int moves;
	public boolean moved = false;
	public boolean check = false;
	public static boolean nomoves = false;
	public static boolean win = false;
	public boolean transitioning = false;
	//to delay stuff
	final Handler handler = new Handler();
	Random r = new Random();
	
	//Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/TOYZARUX.ttf");

	//Typeface font = Typeface.createFromAsset(this.getAssets(), "Bloop.ttf");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//get rid of title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//make a surface view to draw on
		
		
		//get dimensions of screen
		Display display = getWindowManager().getDefaultDisplay();    
		Point size = new Point();
		display.getSize(size);
		width= size.x;
		height= size.y;
		backx = 50;
		backy = height - 1200;
		backwidth = width - 50;
		backheight = height-250;
		tilewidth = (backwidth - 270) / 4;
		tileheight = (backheight - 965) / 4;
		for (int rows = 0; rows < 4 ; rows++)
		{
			for (int columns = 0; columns < 4 ; columns++)
			{
				xs [rows*4 + columns] = backx + ((columns+1) * gap + columns * tileheight);
				ys [rows*4 + columns] = backy + ((rows+1) * gap + rows * tileheight);
				numberx [rows*4 + columns] = xs[rows*4 + columns] + tilewidth/3;
				numbery [rows*4 + columns] = ys[rows*4 + columns] + 32 + tileheight/2;
				values[rows*4 + columns]=2;
				occupied [rows*4 + columns] = false;
			}
			
		}
		
random();
random();
//		occupied [8]=true ;
//		occupied [7]=true;
//		values [8]=1024 ;
//		values [7]=1024;
		
		x = xs[0] + 25;
		y = ys[0] + 15;
		
		
		

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MySurface.thread.interrupt();
		SecondThread.setRunning(false);
		
	}
	
	
	protected void onResume()
	{
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		FrameLayout game = new FrameLayout(this);	 
		RelativeLayout GameButtons = new RelativeLayout (this);
		surfaceView = new MySurface(this);
		Button b1 = new Button(this);
		b1.setWidth(300);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		params2.addRule(RelativeLayout.CENTER_HORIZONTAL);  
		b1.setLayoutParams(params2);
		b1.setText("Restart Game");
		//set the contentview as the drawing canvas
		GameButtons.addView(b1);
		b1.setOnClickListener(this);
				game.addView(surfaceView);
				game.addView(GameButtons);
				setContentView(game);
		//make a ontouch listener for swiping gesture
		surfaceView.setOnTouchListener(new OnSwipeTouchListener(this) {
            
        });
		  Context context = getApplicationContext();
		  Toast toast= Toast.makeText(context,"restore",Toast.LENGTH_SHORT);
		  toast.show();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putInt("MyInt", moves);
	  surfaceView = new MySurface(this);
	  surfaceView.setOnTouchListener(new OnSwipeTouchListener(this) {
          
      });
	  // etc.
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  
	  int moves = savedInstanceState.getInt("MyInt");
	}
	
	public void onClick(View v)
	{
		moved = false;
		nomoves = false;
		check = false;
		moves = 0;
		win = false;
		for (int rows = 0; rows < 4 ; rows++)
		{
			for (int columns = 0; columns < 4 ; columns++)
			{
				xs [rows*4 + columns] = backx + ((columns+1) * gap + columns * tileheight);
				ys [rows*4 + columns] = backy + ((rows+1) * gap + rows * tileheight);
				numberx [rows*4 + columns] = xs[rows*4 + columns] + tilewidth/3;
				numbery [rows*4 + columns] = ys[rows*4 + columns] + 32 + tileheight/2;
				values[rows*4 + columns]=2;
				occupied [rows*4 + columns] = false;

			}
			
		}
		random();
		random();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void random ()
	{
		int number = r.nextInt ( 16 );
		while (occupied[number])
		{
			number = r.nextInt ( 16 );
		}
		occupied[number] = true;
	}
	
	public void checkend ()
	{
		for (int i = 0; i < 15 ; i++)
		{
			if (values[i]==2048)
					{
				win= true;
					}
		else if (occupied[i])
					{
				check =true;
					}
			else
			{
				check = false;
			}
		}
		if (check)
		{
			if (values[0]!=values[1] && values[0] != values[4])
			{
				if (values[2]!=values[3] && values[2] != values[6] && values [2]!= values [1])
				{
					if (values[5]!=values[1] && values[5] != values[4] && values [5]!= values [6] && values [5]!= values [9])
					{
						if (values[7]!=values[3] && values[7] != values[6] && values [7]!= values [11] )
						{
							if (values[8]!=values[4] && values[8] != values[9] && values [8]!= values [12] )
							{
								if (values[10]!=values[6] && values[10] != values[9] && values [10]!= values [11] && values [10]!= values [14])
								{
									if (values[13]!=values[9] && values[11] != values[9] && values [13]!= values [14] )
									{
										if (values[15]!=values[11] && values[15] != values[14] )
										{
											nomoves = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void transition (int n1, int n2, int direction)
	{
		int tempx;
		int tempnx;
		int tempy;
		int tempny;
		transitioning = true;
		//swipe left
		if (direction == 1)
		{
		tempx = xs[n1];
		tempnx = numberx[n1];
		
			while (xs[n1] != xs[n2])
			{
				
				try {
					Thread.sleep((long) 0.98);
					xs[n1] = xs[n1] - 2;
					numberx[n1] = numberx[n1]-2;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			xs[n1] = tempx;
			numberx[n1]=tempnx;
		}
		//swipe right
		else if (direction == 2)
		{
			tempx = xs[n1];
			tempnx = numberx[n1];
			
				while (xs[n1] != xs[n2])
				{
					
					try {
						Thread.sleep((long) 0.98);
						xs[n1] = xs[n1] + 2;
						numberx[n1] = numberx[n1]+2;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				xs[n1] = tempx;
				numberx[n1]=tempnx;
		}
		//swipe up
		else if (direction == 3)
		{
			tempy = ys[n1];
			tempny = numbery[n1];
			
				while (ys[n1] != ys[n2])
				{
					
					try {
						Thread.sleep((long) 0.98);
						ys[n1] = ys[n1] - 2;
						numbery[n1] = numbery[n1]-2;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ys[n1] = tempy;
				numbery[n1]=tempny;
		}
		//swipe down
		else
		{
			tempy = ys[n1];
			tempny = numbery[n1];
			
				while (ys[n1] != ys[n2])
				{
					
					try {
						Thread.sleep((long) 0.98);
						ys[n1] = ys[n1] + 2;
						numbery[n1] = numbery[n1]+2;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ys[n1] = tempy;
				numbery[n1]=tempny;
		}
		transitioning = false;

	}
	
	//class for gesture recognition
	 class OnSwipeTouchListener implements OnTouchListener  {
		 
		 public final GestureDetector gestureDetector;
		 
		 public OnSwipeTouchListener (Context ctx) {
			 
			 gestureDetector = new GestureDetector(ctx, new GestureListener());		
			 
		 }
		 
		 public boolean onTouch(final View view, final MotionEvent motionEvent) {
		        return gestureDetector.onTouchEvent(motionEvent);
		    }
		 
		 public final class GestureListener extends SimpleOnGestureListener{
			 
			 public static final int SWIPE_THRESHOLD = 100;
		        public static final int SWIPE_VELOCITY_THRESHOLD = 100;
		 
		        @Override
		        public boolean onDown(MotionEvent e) {
		              return true;
		        }
		        
		 
		 @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            boolean result = false;
	            try {
	                float diffY = e2.getY() - e1.getY();
	                float diffX = e2.getX() - e1.getX();
	                if (!win)
	                {
	                if (Math.abs(diffX) > Math.abs(diffY)) {
	                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffX > 0) {
	                            onSwipeRight();
        	        			 

	                        } else {
	                            onSwipeLeft();
        	        			 

	                        }
	                    }
	                } else {
	                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffY > 0) {
	                            onSwipeBottom();
        	        			 

	                        } else {
	                            onSwipeTop();
        	        			 

	                        }
	                    }
	                }
	            }
	                
	            } catch (Exception exception) {
	                exception.printStackTrace();
	            }
	            if (moved && !transitioning)
                {
                	
                	random();
                	moved = false;
                	checkend();
                	
                }
	            return result;
	        }
		 
		 //what do to when different swipes are detected
		 public void onSwipeTop() {
             
//             if (y > backy + tileheight)
//             {
//            	 y = y - gap - tileheight;
//            	 Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            	 
//             }
			 for (int row = 1 ; row < 4 ; row++)
        	 {
        		 for (int column = 0 ; column < 4 ; column++)
        		 {
        			 //[column + row*4]
        		 if (occupied[column + row*4] && !occupied[column + (row-1)*4])
        		 {
        			 if (row == 1)
        					 {
        				 transition(column + row*4,column + (row-1)*4,3);
        			 occupied[column + (row-1)*4] = true;
        			 occupied[column + row*4] = false;
        			 values[column + (row-1)*4] = values [column + row*4];
        			 values[column + row*4] = 2;
        			 moves++;
        			 moved = true;

        					 }
        			 else if (row == 2)
        			 {
        				 if (!occupied[column + (row-2)*4])
        				 {
            				 transition(column + row*4,column + (row-2)*4,3);
        					 occupied[column + (row-2)*4] = true;
                			 occupied[column + row*4] = false;
                			 values[column + (row-2)*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else if (values[column + (row-2)*4] == values[column + row*4])
        				 {
            				 transition(column + row*4,column + (row-2)*4,3);
        					 occupied[column + (row-2)*4] = true;
                			 occupied[column + row*4] = false;
                			 values[column + (row-2)*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else
        				 {
            				 transition(column + row*4,column + (row-1)*4,3);
        					 occupied[column + (row-1)*4] = true;
                			 occupied[column + row*4] = false;
                			 values[column + (row-1)*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        			 else if (row == 3)
        			 {
        				 if (!occupied[column + (row-2)*4])
        				 {
        					 if (!occupied[column + (row-3)*4])
            				 {
                				 transition(column + row*4,column + (row-3)*4,3);
            					 occupied[column + (row-3)*4] = true;
                    			 occupied[column + row*4] = false;
                    			 values[column + (row-3)*4] = values [column + row*4];
                    			 values[column + row*4] = 2;
                    			 moves++;
                    			 moved = true;

            				 }
            				 //i added !occupied column + row-2 as well
            				 //gotta check if row 0 is occupied, if it isn't then just move
            				 //need to check if row - 1 is occupied, if it isn't check if row -2 is occupied, if it isn't, check if row -3 is occupied
            				 //if it is, check if 
            				 else if (values[column + (row-3)*4] == values[column + row*4])
            				 {
                				 transition(column + row*4,column + (row-3)*4,3);
            					 //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            					 occupied[column + (row-3)*4] = true;
                    			 occupied[column + row*4] = false;
                    			 values[column + (row-3)*4] = values [column + row*4] * 2;
                    			 values[column + row*4] = 2;
                    			 moves++;
                    			 moved = true;

            				 }
            				 
            				 else
            				 {
                				 transition(column + row*4,column + (row-2)*4,3);
            					 occupied[column + (row-2)*4] = true;
                    			 occupied[column + row*4] = false;
                    			 values[column + (row-2)*4] = values [column + row*4];
                    			 values[column + row*4] = 2;
                    			 moves++;
                    			 moved = true;
            				 }

        				 }
        				 else if (values[column + (row-2)*4] == values[column + row*4])
        				 {
            				 transition(column + row*4,column + (row-2)*4,3);
    					 occupied[column + (row-2)*4] = true;
            			 occupied[column + row*4] = false;
            			 values[column + (row-2)*4] = values [column + row*4]*2;
            			 values[column + row*4] = 2;
            			 moves++;
            			 moved = true;
        				 }
        				 else
        				 {
            				 transition(column + row*4,column + (row-1)*4,3);
        					 occupied[column + (row-1)*4] = true;
                			 occupied[column + row*4] = false;
                			 values[column + (row-1)*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        			 
        		 }
        		 else if (occupied[column + row*4] && values[column + (row-1)*4] == values[column + row*4])
        		 {

    				 transition(column + row*4,column + (row-1)*4,3);
        			 values[column + (row-1)*4] = values[column + (row-1)*4] * 2;
        			 values[column + row*4] = 2;
        			 occupied[column + (row-1)*4] = true;
        			 occupied[column + row*4] = false;
        			 moves++;
        			 moved = true;

        			 
        		 }
        		 }
        	 }
         }
         public void onSwipeRight() {
             //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
        	 for (int column = 2 ; column >= 0 ; column--)
        	 {
        		 for (int row = 0 ; row < 4 ; row++)
        		 {
        			 //[column + row*4]
        		 if (occupied[column + row*4] && !occupied[(column+1) + row*4])
        		 {
        			 if (column == 2)
        					 {
        				 transition(column + row*4,(column+1) + row*4,2);
        			 occupied[(column+1) + row*4] = true;
        			 occupied[column + row*4] = false;
        			 values[(column+1) + row*4] = values [column + row*4];
        			 values[column + row*4] = 2;
        			 moves++;
        			 moved = true;

        					 }
        			 else if (column == 1)
        			 {
        				 if (!occupied[(column+2) + row*4])
        				 {
            				 transition(column + row*4,(column+2) + row*4,2);
        					 occupied[(column+2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+2) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else if (values[(column+2) + row*4] == values[column + row*4])
        				 {
            				 transition(column + row*4,(column+2) + row*4,2);
        					 occupied[(column+2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+2) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else
        				 {
            				 transition(column + row*4,(column+1) + row*4,2);
        					 occupied[(column+1) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+1) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        			 else if (column == 0)
        			 {
        				 if (!occupied[(column+2) + row*4])
        				 {
        				 if (!occupied[(column+3) + row*4])
        				 {
            				 transition(column + row*4,(column+3) + row*4,2);
        					 occupied[(column+3) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+3) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else if (values[(column+3) + row*4] == values[column + row*4])
        				 {
            				 transition(column + row*4,(column+3) + row*4,2);
        					 occupied[(column+3) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+3) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else
        				 {
            				 transition(column + row*4,(column+2) + row*4,2);
        					 occupied[(column+2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+2) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        				 }
        				 else if (values[(column+2) + row*4] == values[column + row*4])
        				 {
            				 transition(column + row*4,(column+2) + row*4,2);
        					 occupied[(column+2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+2) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        				 
        				 else
        				 {
            				 transition(column + row*4,(column+1) + row*4,2);
        					 occupied[(column+1) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column+1) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        			 
        		 }
        		 else if (occupied[column + row*4] && values[(column+1) + row*4] == values[column + row*4])
        		 {

    				 transition(column + row*4,(column+1) + row*4,2);
        			 values[(column+1) + row*4] = values[(column+1) + row*4] * 2;
        			 values[column + row*4] = 2;
        			 occupied[(column+1) + row*4] = true;
        			 occupied[column + row*4] = false;
        			 moves++;
        			 moved = true;

        			 
        		 }
        		 }
        	 }
        	 
//             if (x < backx + width - tilewidth)
//             {
            	 
//            	 while(x <= width - 150)
//            	 {
//            		 try {
//						Thread.sleep((long) 0.5);
//						x = x+2;
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//            			        
//            			  
//            	 }
//            	 Toast.makeText(MainActivity.this, "transition", Toast.LENGTH_SHORT).show();
//            	 x = x + gap + tilewidth - 14;
//             }
             
         }
         public void onSwipeLeft() {
             
        	 for (int column = 1 ; column < 4 ; column++)
        	 {
        		 for (int row = 0 ; row < 4 ; row++)
        		 {
        			 //[column + row*4]
        		 if (occupied[column + row*4] && !occupied[(column-1) + row*4])
        		 {
        			 if (column == 1)
        					 {
        				 transition(column + row*4,(column-1) + row*4,1);
        			 occupied[(column-1) + row*4] = true;
        			 occupied[column + row*4] = false;
        			 values[(column-1) + row*4] = values [column + row*4];
        			 values[column + row*4] = 2;
        			 moves++;
        			 moved = true;

        					 }
        			 else if (column == 2)
        			 {
        				 if (!occupied[(column-2) + row*4])
        				 {
        					 transition(column + row*4,(column-2) + row*4,1);
        					 occupied[(column-2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-2) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else if (values[(column-2) + row*4] == values[column + row*4])
        				 {
        					 transition(column + row*4,(column-2) + row*4,1);
        					 occupied[(column-2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-2) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

        				 }
        				 else
        				 {
        					 transition(column + row*4,(column-2) + row*4,1);
        					 occupied[(column-1) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-1) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        			 else if (column == 3)
        			 {
        				 if (!occupied[(column-2) + row*4])
        				 {
        				 if (!occupied[(column-3) + row*4])
        				 {
        					 transition(column + row*4,(column-3) + row*4,1);
        					 occupied[(column-3) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-3) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

                			 
        				 }
        				 else if (values[(column-3) + row*4] == values[column + row*4])
        				 {
        					 transition(column + row*4,(column-3) + row*4,1);
        					 occupied[(column-3) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-3) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

                			 
        				 }
        				 else
        				 {
        					 transition(column + row*4,(column-2) + row*4,1);
        					 occupied[(column-2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-2) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 }
        				 else if (values[(column-2) + row*4] == values[column + row*4])
        				 {
        					 transition(column + row*4,(column-2) + row*4,1);
        					 occupied[(column-2) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-2) + row*4] = values [column + row*4] * 2;
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        				 else
        				 {
        					 transition(column + row*4,(column-1) + row*4,1);
        					 occupied[(column-1) + row*4] = true;
                			 occupied[column + row*4] = false;
                			 values[(column-1) + row*4] = values [column + row*4];
                			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;
        				 }
        			 
        			 }
        			 
        		 }
        		 else if (occupied[column + row*4] && values[(column-1) + row*4] == values[column + row*4])
        		 {
        			 transition(column + row*4,(column-1) + row*4,1);
        			 values[(column-1) + row*4] = values[(column-1) + row*4] * 2;
        			 values[column + row*4] = 2;
        			 occupied[(column-1) + row*4] = true;
        			 occupied[column + row*4] = false;
        			 moves++;
        			 moved = true;

        			 
        		 }
        		 }
        	 }
         }
         
         public void onSwipeBottom() {
        	 
        	 for (int row = 2; row >= 0 ; row--)
     		{
     			for (int column = 3; column >= 0 ; column--)
     			{
     				
     				if (occupied[column + row*4] && !occupied[column + (row+1)*4])
     				{
     					if (row == 2)
     					{
           				 transition(column + row*4,column + (row+1)*4,4);
     						occupied[column + (row+1)*4] = true;
     	        			 occupied[column + row*4] = false;
     	        			 values[column + (row+1)*4] = values [column + row*4];
     	        			 values[column + row*4] = 2;
     	        			 moves++;
     	        			 moved = true;

     					}
     					else if ( row == 1)
     					{
     						if (!occupied[column + (row+2)*4])
           				 {
     	           				 transition(column + row*4,column + (row+2)*4,4);
           					 occupied[column + (row+2)*4] = true;
                   			 occupied[column + row*4] = false;
                   			 values[column + (row+2)*4] = values [column + row*4];
                   			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

           				 }
           				 else if (values[column + (row+2)*4] == values[column + row*4])
           				 {
           					//Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
               				 transition(column + row*4,column + (row+2)*4,4);
           					 occupied[column + (row+2)*4] = true;
                   			 occupied[column + row*4] = false;
                   			 values[column + (row+2)*4] = values [column + row*4] * 2;
                   			 values[column + row*4] = 2;
                			 moves++;
                			 moved = true;

           				 }
           				else
         				 {
              				 transition(column + row*4,column + (row+1)*4,4);
         					occupied[column + (row+1)*4] = true;
                			 occupied[column + row*4] = false;
                			 values[column + (row+1)*4] = values [column + row*4];
                			 values[column + row*4] = 2;
              			 moves++;
              			 moved = true;
         				 }
     						
     					}
     					else if (row == 0)
     					{
     						if (!occupied[column + (row+2)*4])
     						{
     						if (!occupied[column + (row+3)*4])
              				 {
     	           				 transition(column + row*4,column + (row+3)*4,4);
              					 occupied[column + (row+3)*4] = true;
                      			 occupied[column + row*4] = false;
                      			 values[column + (row+3)*4] = values [column + row*4];
                      			 values[column + row*4] = 2;
                    			 moves++;
                    			 moved = true;

              				 }
              				 else if (values[column + (row+3)*4] == values[column + row*4])
              				 {
                   				 transition(column + row*4,column + (row+3)*4,4);
              					 occupied[column + (row+3)*4] = true;
                      			 occupied[column + row*4] = false;
                      			 values[column + (row+3)*4] = values [column + row*4] * 2;
                      			 values[column + row*4] = 2;
                    			 moves++;
                    			 moved = true;

              				 }
              				 else
              				 {
                   				 transition(column + row*4,column + (row+2)*4,4);
              					occupied[column + (row+2)*4] = true;
                     			 occupied[column + row*4] = false;
                     			 values[column + (row+2)*4] = values [column + row*4];
                     			 values[column + row*4] = 2;
                   			 moves++;
                   			 moved = true;
              				 }
     						}
     						else if (values[column + (row+2)*4] == values[column + row*4])
             				 {
     	           				 transition(column + row*4,column + (row+2)*4,4);
             					 occupied[column + (row+2)*4] = true;
                     			 occupied[column + row*4] = false;
                     			 values[column + (row+2)*4] = values [column + row*4] * 2;
                     			 values[column + row*4] = 2;
                   			 moves++;
                   			 moved = true;

             				 }
             				 else
             				 {
                   				 transition(column + row*4,column + (row+1)*4,4);
             					occupied[column + (row+1)*4] = true;
                    			 occupied[column + row*4] = false;
                    			 values[column + (row+1)*4] = values [column + row*4];
                    			 values[column + row*4] = 2;
                  			 moves++;
                  			 moved = true;
             				 }
     					}
     				}
     				else if (occupied[column + row*4] && values[column + (row+1)*4] == values[column + row*4])
           		 {

          				 transition(column + row*4,column + (row+1)*4,4);
           			 values[column + (row+1)*4] = values[column + (row+1)*4] * 2;
           			 values[column + row*4] = 2;
           			 occupied[column + (row+1)*4] = true;
           			 occupied[column + row*4] = false;
        			 moves++;
        			 moved = true;

           			 
           		 } 
           		 
     				
     			
     		}
        	 
         }
         }

		 }    
	    }

	

	

}


