package com.example.watchfinder.data;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UserManager_Factory implements Factory<UserManager> {
  private final Provider<Context> appContextProvider;

  public UserManager_Factory(Provider<Context> appContextProvider) {
    this.appContextProvider = appContextProvider;
  }

  @Override
  public UserManager get() {
    return newInstance(appContextProvider.get());
  }

  public static UserManager_Factory create(Provider<Context> appContextProvider) {
    return new UserManager_Factory(appContextProvider);
  }

  public static UserManager newInstance(Context appContext) {
    return new UserManager(appContext);
  }
}
