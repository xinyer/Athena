package com.athena.library.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.athena.library.R;


public class T9KeyBoard extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    private KeyboardButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private KeyboardButton btnStar, btnPound;
    private ImageView btnDial, btnDel;

    private StringBuilder sb = new StringBuilder();
    private onKeyClickListener mListener;
    private onDialBtnClickListener mDialBtnClickListener;


    public T9KeyBoard(Context context) {
        super(context);
        initView(context);
    }

    public T9KeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.t9_keyboard, null);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(v, params);

        btn0 = (KeyboardButton) v.findViewById(R.id.btn0);
        btn1 = (KeyboardButton) v.findViewById(R.id.btn1);
        btn2 = (KeyboardButton) v.findViewById(R.id.btn2);
        btn3 = (KeyboardButton) v.findViewById(R.id.btn3);
        btn4 = (KeyboardButton) v.findViewById(R.id.btn4);
        btn5 = (KeyboardButton) v.findViewById(R.id.btn5);
        btn6 = (KeyboardButton) v.findViewById(R.id.btn6);
        btn7 = (KeyboardButton) v.findViewById(R.id.btn7);
        btn8 = (KeyboardButton) v.findViewById(R.id.btn8);
        btn9 = (KeyboardButton) v.findViewById(R.id.btn9);
        btnStar = (KeyboardButton) v.findViewById(R.id.btn_star);
        btnPound = (KeyboardButton) v.findViewById(R.id.btn_pound);
        btnDial = (ImageView) v.findViewById(R.id.btn_done);
        btnDel = (ImageView) v.findViewById(R.id.btn_del);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnPound.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnDial.setOnClickListener(this);

        btn0.setOnLongClickListener(this);
//        btnDel.setOnTouchListener(new DeleteCharOneByOneListener());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_done) {
            if (mDialBtnClickListener != null) {
                mDialBtnClickListener.onDialBtnClick(sb.toString());
            }
        } else {
            click(v.getId());
        }
    }

    private void click(int btnId) {
        if (R.id.btn0 == btnId) {
            sb.append("0");
        } else if (R.id.btn1 == btnId) {
            sb.append("1");
        } else if (R.id.btn2 == btnId) {
            sb.append("2");
        } else if (R.id.btn3 == btnId) {
            sb.append("3");
        } else if (R.id.btn4 == btnId) {
            sb.append("4");
        } else if (R.id.btn5 == btnId) {
            sb.append("5");
        } else if (R.id.btn6 == btnId) {
            sb.append("6");
        } else if (R.id.btn7 == btnId) {
            sb.append("7");
        } else if (R.id.btn7 == btnId) {
            sb.append("7");
        } else if (R.id.btn8 == btnId) {
            sb.append("8");
        } else if (R.id.btn9 == btnId) {
            sb.append("9");
        } else if (R.id.btn_star == btnId) {
            sb.append("*");
        } else if (R.id.btn_pound == btnId) {
            sb.append("#");
        } else if (R.id.btn_del == btnId) {
            delChar(true);
        }
        if (mListener != null) {
            mListener.onResult(sb.toString());
            int i = sb.length() - 1;
            if (i >= 0) {
                mListener.onResultLastChar(sb.charAt(i));
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.btn0) { //button 0
            sb.append("+");
            if (mListener != null) {
                mListener.onResult(sb.toString());
                int index = sb.length() - 1;
                if (index >= 0) {
                    mListener.onResultLastChar(sb.charAt(index));
                }
            }
            return true;
        }
        return false;
    }

    public void appendString(String str) {
        if (TextUtils.isEmpty(str)) return;
        sb.append(str);
        if (mListener != null) {
            mListener.onResult(sb.toString());
            int index = sb.length() - 1;
            if (index >= 0) {
                mListener.onResultLastChar(sb.charAt(index));
            }
        }
    }

    public void setString(String str) {
        if (!TextUtils.isEmpty(str)) {
            sb.delete(0, sb.length());
            sb.append(str);
            if (mListener != null) {
                mListener.onResult(sb.toString());
                int index = sb.length() - 1;
                if (index >= 0) {
                    mListener.onResultLastChar(sb.charAt(index));
                }
            }
        }
    }

    public void clearString() {
        sb.delete(0, sb.length());
        if (mListener != null) {
            mListener.onResult("");
            mListener.onResultLastChar('\u0000');
        }
    }

    /**
     * 删除字符串
     *
     * @param isShowResult 是否去搜索并显示结果
     *                     （产品逻辑：长按删除过程中不显示，长按结束才显示）
     * @return
     */
    public boolean delChar(boolean isShowResult) {
        int lastIndex = sb.length() - 1;
        if (lastIndex < 0) {
            return false;
        }
        sb.deleteCharAt(lastIndex);
//        if (mListener != null) {
//            mListener.onResult(sb.toString(), isShowResult);
//            int index = sb.length() - 1;
//            if (index >= 0)
//                mListener.onResultLastChar(sb.charAt(index));
//        }
        return true;
    }

//    final Handler deleteCharHandler = new Handler();

    /**
     * 直到删除结束才通知mListener
     */
//    private Runnable deleteCharThread = new Runnable() {
//
//        @Override
//        public void run() {
//            delChar(false);
//            deleteCharHandler.postDelayed(this, 100);
//        }
//    };

//    private class DeleteCharOneByOneListener implements OnTouchListener {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    deleteCharHandler.postDelayed(deleteCharThread, 500);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    deleteCharHandler.removeCallbacks(deleteCharThread);
//                    if (mListener != null) {
//                        mListener.onResult(sb.toString(), true);
//                        int index = sb.length() - 1;
//                        if (index >= 0) mListener.onResultLastChar(sb.charAt(index));
//                    }
//                    break;
//            }
//            return false;
//        }
//    }

    public void setOnKeyClickListener(onKeyClickListener listener) {
        mListener = listener;
    }

    public void setOnDialBtnClickListener(onDialBtnClickListener listener) {
        mDialBtnClickListener = listener;
    }

    /**
     * 按键点击事件监听，不包括通话键，通话键监听参考onDialBtnClickListener
     */
    public interface onKeyClickListener {
        /**
         * 点击键盘按键监听器
         *
         * @param str          输出的字符串
         */
        public void onResult(String str);

        /**
         * 最后一个字符
         *
         * @param c 最后一个字符
         */
        public void onResultLastChar(char c);
    }

    public interface onDialBtnClickListener {
        /**
         * 拨号键点击事件监听器
         *
         * @param str 当前输入的字符串
         */
        public void onDialBtnClick(String str);

    }

}
