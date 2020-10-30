package com.jun.moiso.interfaces;

import android.view.View;

import androidx.databinding.ObservableArrayList;

import com.jun.moiso.model.CustomKeyboard;

public interface KeyboardListAdapter {

    //아이템 추가 애니메이션
    void createAnimation(View viewToAnimate, int position);

    //아이템 삭제 애니메이션
    void delelteAnimation(final View viewToAnimate, final View delete_btn, final int position);

    //버튼 클릭 잠금
    void lockClickable(View item, View delete_btn);

    //버튼 클릭 잠금해제
    void unlockClickable(View item, View delete_btn);

    //아이템 변경 알림
    void notifyItemChanged();

    //키보드 리스트 적용
    void setCustomKeyboardList(ObservableArrayList<CustomKeyboard> customKeyboards);
}
