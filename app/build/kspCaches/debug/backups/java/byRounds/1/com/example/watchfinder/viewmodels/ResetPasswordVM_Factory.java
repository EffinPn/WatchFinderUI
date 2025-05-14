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
public final class ResetPasswordVM_Factory implements Factory<ResetPasswordVM> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public ResetPasswordVM_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public ResetPasswordVM get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static ResetPasswordVM_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new ResetPasswordVM_Factory(authRepositoryProvider);
  }

  public static ResetPasswordVM newInstance(AuthRepository authRepository) {
    return new ResetPasswordVM(authRepository);
  }
}
