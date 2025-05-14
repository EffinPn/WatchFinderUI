package com.example.watchfinder.viewmodels;

import android.content.Context;
import coil.ImageLoader;
import com.example.watchfinder.api.ApiService;
import com.example.watchfinder.data.UserManager;
import com.example.watchfinder.repository.AuthRepository;
import com.example.watchfinder.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ProfileVM_Factory implements Factory<ProfileVM> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<Context> appContextProvider;

  private final Provider<UserManager> userManagerProvider;

  private final Provider<ImageLoader> imageLoaderProvider;

  private final Provider<ApiService> apiServiceProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public ProfileVM_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<Context> appContextProvider, Provider<UserManager> userManagerProvider,
      Provider<ImageLoader> imageLoaderProvider, Provider<ApiService> apiServiceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.appContextProvider = appContextProvider;
    this.userManagerProvider = userManagerProvider;
    this.imageLoaderProvider = imageLoaderProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ProfileVM get() {
    return newInstance(userRepositoryProvider.get(), appContextProvider.get(), userManagerProvider.get(), imageLoaderProvider.get(), apiServiceProvider.get(), authRepositoryProvider.get());
  }

  public static ProfileVM_Factory create(Provider<UserRepository> userRepositoryProvider,
      Provider<Context> appContextProvider, Provider<UserManager> userManagerProvider,
      Provider<ImageLoader> imageLoaderProvider, Provider<ApiService> apiServiceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new ProfileVM_Factory(userRepositoryProvider, appContextProvider, userManagerProvider, imageLoaderProvider, apiServiceProvider, authRepositoryProvider);
  }

  public static ProfileVM newInstance(UserRepository userRepository, Context appContext,
      UserManager userManager, ImageLoader imageLoader, ApiService apiService,
      AuthRepository authRepository) {
    return new ProfileVM(userRepository, appContext, userManager, imageLoader, apiService, authRepository);
  }
}
