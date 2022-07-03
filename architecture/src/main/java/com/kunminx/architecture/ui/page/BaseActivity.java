/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kunminx.architecture.BaseApplication;
import com.kunminx.architecture.utils.AdaptScreenUtils;
import com.kunminx.architecture.utils.BarUtils;
import com.kunminx.architecture.utils.ScreenUtils;


/**
 * Create by KunMinX at 19/8/1
 */
public abstract class BaseActivity extends AppCompatActivity {

  private ViewModelProvider mActivityProvider;
  private ViewModelProvider mApplicationProvider;

  protected abstract void onInit();

  protected abstract void onOutPut();

  protected abstract void onIntPut();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
    BarUtils.setStatusBarLightMode(this, true);

    super.onCreate(savedInstanceState);

    onInit();
    onOutPut();
    onIntPut();
  }

  //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
  //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
  //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
  //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。

  //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840

  protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
    if (mActivityProvider == null) {
      mActivityProvider = new ViewModelProvider(this);
    }
    return mActivityProvider.get(modelClass);
  }

  protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
    if (mApplicationProvider == null) {
      mApplicationProvider = new ViewModelProvider((BaseApplication) this.getApplicationContext());
    }
    return mApplicationProvider.get(modelClass);
  }

  @Override
  public Resources getResources() {
    if (ScreenUtils.isPortrait()) {
      return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
    } else {
      return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
    }
  }

  protected void toggleSoftInput() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  protected void openUrlInBrowser(String url) {
    Uri uri = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    startActivity(intent);
  }
}