package com.itsaky.androidide;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityTerminalBinding;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.terminal.TerminalEmulator;
import com.itsaky.terminal.TerminalSession;
import com.itsaky.terminal.TerminalSessionClient;
import com.itsaky.terminal.TextStyle;
import com.itsaky.terminal.view.TerminalView;
import com.itsaky.terminal.view.TerminalViewClient;
import java.util.Map;

public class TerminalActivity extends StudioActivity {
    
    private ActivityTerminalBinding binding;
    private TerminalView terminal;
    private TerminalSession session;
    
    private int currentTextSize = 0;
    
    private final Client client = new Client();
    private static final Logger LOG = Logger.instance("TerminalActivity");
    
    @Override
    protected View bindLayout() {
        binding = ActivityTerminalBinding.inflate(getLayoutInflater());
        terminal = new TerminalView(this, null);
        terminal.setTerminalViewClient(client);
        terminal.setTextSize(currentTextSize = SizeUtils.dp2px(10));
        terminal.setTypeface(TypefaceUtils.jetbrainsMono());
        terminal.attachSession(createSession());
        terminal.setKeepScreenOn(true);
        
        binding.getRoot().addView(terminal, new ViewGroup.LayoutParams(-1, -1));
        
        return binding.getRoot();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setTerminalCursorBlinkingState(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setTerminalCursorBlinkingState(false);
    }
    
    private void setTerminalCursorBlinkingState(boolean start ) {
        if(terminal != null && terminal.mEmulator != null) {
            terminal.setTerminalCursorBlinkerState(start, true);
        }
    }
    
    private TerminalSession createSession() {
        final Map<String, String> envs = Environment.getEnvironment(true);
        final String[] env = new String[envs.size()];
        int i=0;
        for(Map.Entry<String, String> entry : envs.entrySet()) {
            env[i] = entry.getKey() + "=" + entry.getValue();
            i++;
        }
        
        session = new TerminalSession(
            Environment.BASH.getAbsolutePath(), // Shell command
            Environment.HOME.getAbsolutePath(), // Working directory
            new String[]{}, // Arguments
            env, // Environment variables
            TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS, // Transcript rows
            client // TerminalSessionClient
        );
        
        return session;
    }
    
    
    
    
    
    
    
    private final class Client implements TerminalViewClient, TerminalSessionClient {
        
        @Override
        public boolean onKeyUp(int keyCode, KeyEvent e) {
            return false;
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && !session.isRunning()) {
                finish();
            }
            return false;
        }

        @Override
        public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
            return false;
        }

        @Override
        public void onTextChanged(TerminalSession changedSession) {
            terminal.onScreenUpdated();
        }

        @Override
        public void onTitleChanged(TerminalSession changedSession) {
        }

        @Override
        public void onSessionFinished(TerminalSession finishedSession) {
            finish();
        }

        @Override
        public void onCopyTextToClipboard(TerminalSession session, String text) {
            ClipboardUtils.copyText("AndroidIDE Terminal", text);
        }

        @Override
        public void onPasteTextFromClipboard(TerminalSession session) {
            String clip = ClipboardUtils.getText().toString();
            if(clip != null && clip.trim().length() >= 0) {
                if(terminal != null && terminal.mEmulator != null) {
                    terminal.mEmulator.paste(ClipboardUtils.getText().toString());
                }
            }
        }

        @Override
        public void onBell(TerminalSession session) {
        }

        @Override
        public void onColorsChanged(TerminalSession session) {
        }

        @Override
        public void onTerminalCursorStateChange(boolean state) {
        }

        @Override
        public Integer getTerminalCursorStyle() {
            return TerminalEmulator.DEFAULT_TERMINAL_CURSOR_STYLE;
        }

        @Override
        public float onScale(float scale) {
            if(scale < 0.9f || scale > 1.1f) {
                boolean increase = scale > 1.1f;
                int newSize = (increase ? 1 : -1) * 2;
                currentTextSize += newSize;
                if(terminal != null)
                    terminal.setTextSize(currentTextSize);
            }
            
            return scale;
        }

        @Override
        public void onSingleTapUp(MotionEvent e) {
            KeyboardUtils.showSoftInput(terminal);
        }

        @Override
        public boolean shouldBackButtonBeMappedToEscape() {
            return false;
        }

        @Override
        public boolean shouldEnforceCharBasedInput() {
            return true;
        }

        @Override
        public boolean shouldUseCtrlSpaceWorkaround() {
            return false;
        }

        @Override
        public boolean isTerminalViewSelected() {
            return true;
        }

        @Override
        public void copyModeChanged(boolean copyMode) {
        }

        @Override
        public boolean onLongPress(MotionEvent event) {
            return false;
        }

        @Override
        public boolean readControlKey() {
            return false;
        }

        @Override
        public boolean readAltKey() {
            return false;
        }


        @Override
        public void onEmulatorSet() {
            setTerminalCursorBlinkingState(true);
            
            if(session != null) {
                binding.getRoot().setBackgroundColor(session.getEmulator().mColors.mCurrentColors[TextStyle.COLOR_INDEX_BACKGROUND]);
            }
        }

        @Override
        public void logError(String tag, String message) {
            LOG.e(message);
        }

        @Override
        public void logWarn(String tag, String message) {
            LOG.w(message);
        }

        @Override
        public void logInfo(String tag, String message) {
            LOG.i(message);
        }

        @Override
        public void logDebug(String tag, String message) {
            LOG.d(message);
        }

        @Override
        public void logVerbose(String tag, String message) {
            LOG.v(message);
        }

        @Override
        public void logStackTraceWithMessage(String tag, String message, Exception e) {
            LOG.e(message, ThrowableUtils.getFullStackTrace(e));
        }

        @Override
        public void logStackTrace(String tag, Exception e) {
            LOG.e(e.getMessage(), ThrowableUtils.getFullStackTrace(e));
        }
    }
}
