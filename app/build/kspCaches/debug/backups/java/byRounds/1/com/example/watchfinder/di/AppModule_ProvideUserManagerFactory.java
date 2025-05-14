package com.example.watchfinder.di;

import android.content.Context;
import com.example.watchfinder.data.UserManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUserManagerFactory implements Factory<UserManager> {
  private final Provider<Context> appContextProvider;

  public AppModule_ProvideUserManagerFactory(Provider<Context> appContextProvider) {
    this.appContextProvider = appContextProvider;
  }

  @Override
  public UserManager get() {
    return provideUserManager(appContextProvider.get());
  }

  public static AppModule_ProvideUserManagerFactory create(Provider<Context> appContextProvider) {
    return new AppModule_ProvideUserManagerFactory(appContextProvider);
  }

  public static UserManager provideUserManager(Context appContext) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUserManager(appContext));
  }
}
