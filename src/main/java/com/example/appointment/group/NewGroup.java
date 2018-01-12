package com.example.appointment.group;

/**新建群组的类
 * Created by MichaelOD on 2017/12/26.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.R;
import com.example.appointment.core.AConnection;
import com.example.appointment.core.ImApp;
import com.example.appointment.message.AMessage;
import com.example.appointment.message.AMessageType;
import com.example.appointment.message.ThreadUtils;
import com.example.appointment.page.Main2;

import org.w3c.dom.Text;

public class NewGroup extends Activity implements OnClickListener
{
	private EditText nameText;
	private EditText describeText;
	private android.widget.Button Button;
	private TextView text;

	private String name;
	private String describe;
	private String txt;
	Handler handler;
	ImApp app;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newgroup);
		
		nameText = (EditText) findViewById(R.id.newgroup_field);
		describeText = (EditText) findViewById(R.id.newdecsribe_field);
		Button = (android.widget.Button)findViewById(R.id.newgroup_button);
		text = (TextView)findViewById(R.id.group_error_text);
		Button.setOnClickListener(this);
		app = (ImApp) getApplication();
		app.getMyConn().addOnMessageListener(listener);
		Main2.on3=true;

	}

	public void onClick(View v)
	{
		//新建群组
		if(v.getId()==R.id.newgroup_button)
		{
			name = nameText.getText().toString(); 
			describe = describeText.getText().toString();  
			if (!name.trim().isEmpty() && !describe.trim().isEmpty())	//trim()去掉前导和后导空白
			{		
				ThreadUtils.runInSubThread(new Runnable() {
					public void run() {
						try {
							AMessage msg = new AMessage();
							msg.type = AMessageType.MSG_TYPE_NEWGROUP;
							msg.from = app.getMyNumber();
							msg.content = name + "#" + describe;//群组名和简介以#分隔
							app.getMyConn().sendMessage(msg);
						} catch (Exception e) {
							ThreadUtils.runInUiThread(new Runnable() {

								public void run() {
									Toast.makeText(getBaseContext(), "出现异常，请检查后重试", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				});
			}
			else
			{
				ThreadUtils.runInUiThread(new Runnable() {

					public void run() {
						Toast.makeText(getBaseContext(), "群组名或简介不能为空！", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		app.getMyConn().removeOnMessageListener(listener);
		Main2.on3=false;
	}
		
		private AConnection.OnMessageListener listener = new AConnection.OnMessageListener() {

			public void onReveive(final AMessage msg) {

				ThreadUtils.runInUiThread(new Runnable() {

					public void run() {
						if (AMessageType.MSG_TYPE_GROUPLIST.equals(msg.type)) {
							txt="创建成功！您的群组号为： "+msg.from;
							
							Intent intent = new Intent(NewGroup.this,
									GroupActivity.class);
							startActivity(intent);
							Toast.makeText(getBaseContext(), txt, Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				});
			}
		};
}
		