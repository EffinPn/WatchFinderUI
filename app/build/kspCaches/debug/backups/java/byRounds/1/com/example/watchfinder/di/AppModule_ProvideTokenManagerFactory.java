package com.example.watchfinder.di;

import android.content.Context;
import com.example.watchfinder.data.prefs.TokenManager;
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
public final class AppModule_ProvideTokenManagerFactory implements Factory<TokenManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideTokenManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TokenManager get() {
    return provideTokenManager(contextProvider.get());
  }

  public static AppModule_ProvideTokenManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideTokenManagerFactory(contextProvider);
  }

  public static TokenManager provideTokenManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTokenManager(context));
  }
}
