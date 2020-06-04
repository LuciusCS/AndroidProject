package demo.lucius.function1.databinding;

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

public abstract class FragmentABinding extends ViewDataBinding {
  @NonNull
  public final Button goToB;

  @NonNull
  public final Button goToF;

  protected FragmentABinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button goToB, Button goToF) {
    super(_bindingComponent, _root, _localFieldCount);
    this.goToB = goToB;
    this.goToF = goToF;
  }

  @NonNull
  public static FragmentABinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_a, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentABinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentABinding>inflateInternal(inflater, demo.lucius.function1.R.layout.fragment_a, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentABinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_a, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentABinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentABinding>inflateInternal(inflater, demo.lucius.function1.R.layout.fragment_a, null, false, component);
  }

  public static FragmentABinding bind(@NonNull View view) {
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
  public static FragmentABinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentABinding)bind(component, view, demo.lucius.function1.R.layout.fragment_a);
  }
}
