package com.example.watchfinder.viewmodels;

import com.example.watchfinder.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class LoginVM_Factory implements Factory<LoginVM> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public LoginVM_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public LoginVM get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static LoginVM_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new LoginVM_Factory(authRepositoryProvider);
  }

  public static LoginVM newInstance(AuthRepository authRepository) {
    return new LoginVM(authRepository);
  }
}
