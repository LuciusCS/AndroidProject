package demo.lucius.funtion2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentDBinding extends ViewDataBinding {
  @NonNull
  public final Button goToE;

  protected FragmentDBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button goToE) {
    super(_bindingComponent, _root, _localFieldCount);
    this.goToE = goToE;
  }

  @NonNull
  public static FragmentDBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_d, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentDBinding>inflateInternal(inflater, demo.lucius.funtion2.R.layout.fragment_d, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_d, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentDBinding>inflateInternal(inflater, demo.lucius.funtion2.R.layout.fragment_d, null, false, component);
  }

  public static FragmentDBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentDBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentDBinding)bind(component, view, demo.lucius.funtion2.R.layout.fragment_d);
  }
}
