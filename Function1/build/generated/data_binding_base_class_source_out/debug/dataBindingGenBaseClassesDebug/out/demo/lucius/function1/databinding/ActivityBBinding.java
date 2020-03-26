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

public abstract class ActivityBBinding extends ViewDataBinding {
  @NonNull
  public final Button goToC;

  protected ActivityBBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button goToC) {
    super(_bindingComponent, _root, _localFieldCount);
    this.goToC = goToC;
  }

  @NonNull
  public static ActivityBBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_b, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityBBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityBBinding>inflateInternal(inflater, demo.lucius.function1.R.layout.activity_b, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityBBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_b, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityBBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityBBinding>inflateInternal(inflater, demo.lucius.function1.R.layout.activity_b, null, false, component);
  }

  public static ActivityBBinding bind(@NonNull View view) {
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
  public static ActivityBBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityBBinding)bind(component, view, demo.lucius.function1.R.layout.activity_b);
  }
}
