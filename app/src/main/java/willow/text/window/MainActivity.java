package willow.text.window;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.graphics.*;
import android.util.*;
import android.view.View.*;

public class MainActivity extends Activity 
{int top;
float x,y;
long startTime;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayout;
	private Win mDesktopLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		createWindowManager();
		createDesktopLayout();
		showDesk();
		
    }
	
	
	
	
	class Win extends LinearLayout{
		public Win(Context context) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);// 水平排列
			//设置宽高
			this.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT,
												   LayoutParams.WRAP_CONTENT));
			View view = LayoutInflater.from(context).inflate( 
				R.layout.dt, null); 
			this.addView(view);
	}
}
	private void createDesktopLayout() {
		mDesktopLayout = new Win(this);
		mDesktopLayout.setOnTouchListener(new OnTouchListener() {
				float mTouchStartX;
				float mTouchStartY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 获取相对屏幕的坐标，即以屏幕左上角为原点
					x = event.getRawX();
					y = event.getRawY(); // 25是系统状态栏的高度
					Log.i("startP", "startX" + mTouchStartX + "====startY"
						  + mTouchStartY);
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							// 获取相对View的坐标，即以此View左上角为原点
							mTouchStartX = event.getX();
							mTouchStartY = event.getY();
							Log.i("startP", "startX" + mTouchStartX + "====startY"
								  + mTouchStartY);
							long end = System.currentTimeMillis() - startTime;
							// 双击的间隔在 300ms以下
							if (end < 300) {
								closeDesk();
							}
							startTime = System.currentTimeMillis();
							break;
						case MotionEvent.ACTION_MOVE:
							// 更新浮动窗口位置参数
							mLayout.x = (int) (x - mTouchStartX);
							mLayout.y = (int) (y - mTouchStartY);
							mWindowManager.updateViewLayout(v, mLayout);
							break;
						case MotionEvent.ACTION_UP:
							// 更新浮动窗口位置参数
							mLayout.x = (int) (x - mTouchStartX);
							mLayout.y = (int) (y - mTouchStartY);
							mWindowManager.updateViewLayout(v, mLayout);
							// 可以在此记录最后一次的位置
							mTouchStartX = mTouchStartY = 0;
							break;
					}
					return true;
				}
			});
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Rect rect = new Rect();
		// /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
		getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		top = rect.top;//状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度
		Log.i("top",""+top);
	}
	/**
	 * 显示DesktopLayout
	 */
	private void showDesk() {
		mWindowManager.addView(mDesktopLayout, mLayout);
		finish();
	}
	/**
	 * 关闭DesktopLayout
	 */
	private void closeDesk() {
		mWindowManager.removeView(mDesktopLayout);
		finish();
	}
	/**
	 * 设置WindowManager
	 */
	private void createWindowManager() {
		// 取得系统窗体
		mWindowManager = (WindowManager) getApplicationContext()
			.getSystemService("window");
		// 窗体的布局样式
		mLayout = new WindowManager.LayoutParams();
		// 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
		mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		// 设置窗体焦点及触摸：
		// FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
		//mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		//mLayout.flags=WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		// 设置显示的模式
		mLayout.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		mLayout.format = PixelFormat.RGBA_8888;
		// 设置对齐的方法
		mLayout.gravity = Gravity.TOP | Gravity.LEFT;
		// 设置窗体宽度和高度
		mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
	}

}
