package net.dev123.yibo.service.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dev123.commons.util.StringUtil;
import net.dev123.mblog.entity.User;
import net.dev123.yibo.ConversationActivity;
import net.dev123.yibo.R;
import net.dev123.yibo.YiBoApplication;
import net.dev123.yibo.common.Constants;
import net.dev123.yibo.db.LocalAccount;
import net.dev123.yibo.service.task.UpdateDirectMessageTask;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class ConversationSendClickListener implements OnClickListener {
    private ConversationActivity context;
    private LocalAccount account;
	public ConversationSendClickListener(ConversationActivity context) {
		this.context = context;
		
		YiBoApplication yibo = (YiBoApplication)context.getApplication();
		account = yibo.getCurrentAccount();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {		
        EditText etMessage = (EditText)context.findViewById(R.id.etText);
        
        User conversationUser = context.getUser();
        String displayName = conversationUser.getDisplayName();
        if (StringUtil.isEmpty(displayName)) {
        	Toast.makeText(v.getContext(), R.string.msg_message_screen_name_empty, Toast.LENGTH_SHORT).show();
        	return;
        }
        
        String message = etMessage.getText().toString().trim();
        if (StringUtil.isEmpty(message)) {
        	Toast.makeText(v.getContext(), R.string.msg_message_content_empty, Toast.LENGTH_SHORT).show();
        	return;
        }
        
		v.setEnabled(false);
		//hide input method
		InputMethodManager inputMethodManager = (InputMethodManager)v.getContext().
		    getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);	
		
		UpdateDirectMessageTask task = new UpdateDirectMessageTask(context, message, account);
		String[] arrayScreenName = displayName.split(Constants.SEPARATOR_RECEIVER);
		List<String> listScreenName = new ArrayList<String>();
		listScreenName.addAll(Arrays.asList(arrayScreenName));
		task.execute(listScreenName);
	}

}
