package com.synertone.aggregation.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.synertone.aggregation.bean.LinkListState;
import com.synertone.aggregation.bean.ServiceInfoModel;
import com.synertone.aggregation.utils.ChechIpMask;
import com.synertone.aggregation.utils.LvHeightUtil;


/**
 * 这是一个通用的ViewHolder, 将会装载AbsListView子类的item View, 并且将item
 * view中的子视图进行缓存和索引，使得用户能够方便的获取这些子view, 减少了代码重复。
 *
 * @author mrsimple
 */
public class CommonViewHolder {
    /**
     *
     */
    private View mContentView;

    /**
     * 构造函数
     *
     * @param context Context
     * @param layoutId ListView、GridView或者其他AbsListVew子类的 Item View的资源布局id
     */
    protected CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
        mContentView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mContentView.setTag(this);
    }

    /**
     * 获取CommonViewHolder，当convertView为空的时候从布局xml装载item view,
     * 并且将该CommonViewHolder设置为convertView的tag, 便于复用convertView.
     *
     * @param context Context
     * @param convertView Item view
     * @param layoutId 布局资源id, 例如R.layout.my_listview_item.
     * @return 通用的CommonViewHolder实例
     */
    public static CommonViewHolder getViewHolder(Context context, View convertView,
                                                 ViewGroup parent, int layoutId) {

        context = (context == null && parent != null) ? parent.getContext() : context;
        CommonViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new CommonViewHolder(context, parent, layoutId);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }

        // 将当前item view设置为ViewFinder要查找的root view, 这一步不能搞错，否则查找不到对象的view
        // ViewFinder.initContentView(viewHolder.getContentView());

        return viewHolder;
    }

    /**
     * @return 当前项的convertView, 在构造函数中装载
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 为id为textViewId的TextView设置文本内容
     *
     * @param textViewId 视图id
     * @param text 要设置的文本内容
     */
    public void setTextForTextView(int textViewId, CharSequence text) {
        TextView textView = ViewFinder.findViewById(mContentView, textViewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * 为id为editTextId的TextView设置文本内容
     *
     * @param editTextId 视图id
     * @param text 要设置的文本内容
     */
    public void setTextForEditText(int editTextId, CharSequence text) {
        EditText et = ViewFinder.findViewById(mContentView, editTextId);
        if (et != null) {
            et.setText(text);
        }
    }
    /**
     * 为id为textViewId的TextView设置文本内容
     *
     * @param textViewId 视图id
     * @param color 要设置的色值
     */
    public void setColorForTextView(int textViewId, int color) {
        TextView textView = ViewFinder.findViewById(mContentView, textViewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param imageViewId ImageView的id, 例如R.id.my_imageview
     * @param drawableId Drawable图片的id, 例如R.drawable.my_photo
     */
    public void setImageForView(int imageViewId, int drawableId) {
        ImageView imageView = ViewFinder.findViewById(mContentView, imageViewId);
        if (imageView != null) {
            imageView.setImageResource(drawableId);
        }
    }


    /**
     * 为CheckBox设置是否选中
     *
     * @param checkViewId CheckBox的id
     * @param isCheck 是否选中
     */
    public void setCheckForCheckBox(int checkViewId, boolean isCheck) {
        CheckBox checkBox = ViewFinder.findViewById(mContentView, checkViewId);
        if (checkBox != null) {
            checkBox.setChecked(isCheck);
        }
    }

    /**
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        View view = ViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, OnClickListener listener) {
        View view = ViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * @param viewId
     * @param listener
     */
    public void setOnTouchListener(int viewId, OnTouchListener listener) {
        View view = ViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setOnTouchListener(listener);
        }
    }

    public void setBackgroundForView(int viewId, int red) {
        View view = ViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setBackgroundColor(red);
        }

    }

    public void setTextWidth(int viewId, int width) {
        View view = ViewFinder.findViewById(mContentView, viewId);
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);

    }


    public void setAdapterForListView(int viewId, CommonAdapter<LinkListState.LinkListBean.ChannelListBean> commonAdapter) {
        ListView view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){
            view.setAdapter(commonAdapter);
        }
    }

    public void setTextForCheckBox(int viewId, CharSequence text) {
        CheckBox view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){

            view.setText(text);
        }
    }

    public void setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        CheckBox checkBox=ViewFinder.findViewById(mContentView,viewId);
        if(checkBox!=null&&onCheckedChangeListener!=null){
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void setHeightForListView(int viewId) {
        ListView view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){
            LvHeightUtil.setListViewHeightBasedOnChildren(view);
        }
    }
    public void setHeightForGridView(int viewId) {
        GridView view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){
            LvHeightUtil.setGridViewHeightBasedOnChildren(view);
        }
    }

    public void setOnSwitchButtonCheckedChangeListener(int viewId, SwitchButton.OnCheckedChangeListener onCheckedChangeListener) {
        SwitchButton view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){
            view.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void setCheckForSwitchButton(int viewId, boolean enable) {
        SwitchButton view = ViewFinder.findViewById(mContentView, viewId);
        if(view!=null){
            view.setChecked(enable);
        }
    }
    public void setItemWeightEditText(final Context mContext, int editTextId, final ServiceInfoModel.ServiceInfoBean.TransferPolicyListBean item) {
        final EditText et = ViewFinder.findViewById(mContentView, editTextId);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(ChechIpMask.a2b(et.getText().toString(), 1, 100)){

                }else if("".equals(et.getText().toString())) {

                }else if(!ChechIpMask.a2b(et.getText().toString(), 1, 100)){
                    Toast.makeText(mContext, "输入的权重不合法，请重新输入！", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setWeight(s.toString());
            }
        });
    }
}
