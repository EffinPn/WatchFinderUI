package com.example.watchfinder.di;

import android.content.Context;
import coil.ImageLoader;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class NetworkModule_ProvideAuthenticatedImageLoaderFactory implements Factory<ImageLoader> {
  private final Provider<Context> contextProvider;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_ProvideAuthenticatedImageLoaderFactory(Provider<Context> contextProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    this.contextProvider = contextProvider;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public ImageLoader get() {
    return provideAuthenticatedImageLoader(contextProvider.get(), okHttpClientProvider.get());
  }

  public static NetworkModule_ProvideAuthenticatedImageLoaderFactory create(
      Provider<Context> contextProvider, Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_ProvideAuthenticatedImageLoaderFactory(contextProvider, okHttpClientProvider);
  }

  public static ImageLoader provideAuthenticatedImageLoader(Context context,
      OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAuthenticatedImageLoader(context, okHttpClient));
  }
}
