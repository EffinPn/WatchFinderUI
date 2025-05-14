package com.example.watchfinder;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import coil3.ImageLoader;
import com.example.watchfinder.api.AInterceptor;
import com.example.watchfinder.api.ApiService;
import com.example.watchfinder.data.UserManager;
import com.example.watchfinder.data.Utils;
import com.example.watchfinder.data.prefs.TokenManager;
import com.example.watchfinder.di.AppModule_ProvideTokenManagerFactory;
import com.example.watchfinder.di.AppModule_ProvideUserManagerFactory;
import com.example.watchfinder.di.CoilModule_ProvideImageLoaderFactory;
import com.example.watchfinder.di.NetworkModule_ProvideApiServiceFactory;
import com.example.watchfinder.di.NetworkModule_ProvideAuthInterceptorFactory;
import com.example.watchfinder.di.NetworkModule_ProvideAuthenticatedImageLoaderFactory;
import com.example.watchfinder.di.NetworkModule_ProvideOkHttpClientFactory;
import com.example.watchfinder.di.NetworkModule_ProvideRetrofitFactory;
import com.example.watchfinder.repository.AuthRepository;
import com.example.watchfinder.repository.GenreRepository;
import com.example.watchfinder.repository.MovieRepository;
import com.example.watchfinder.repository.SeriesRepository;
import com.example.watchfinder.repository.UserRepository;
import com.example.watchfinder.viewmodels.DetailsVM;
import com.example.watchfinder.viewmodels.DetailsVM_HiltModules;
import com.example.watchfinder.viewmodels.DiscoverMoviesVM;
import com.example.watchfinder.viewmodels.DiscoverMoviesVM_HiltModules;
import com.example.watchfinder.viewmodels.DiscoverSeriesVM;
import com.example.watchfinder.viewmodels.DiscoverSeriesVM_HiltModules;
import com.example.watchfinder.viewmodels.ForgotPasswordVM;
import com.example.watchfinder.viewmodels.ForgotPasswordVM_HiltModules;
import com.example.watchfinder.viewmodels.LoginVM;
import com.example.watchfinder.viewmodels.LoginVM_HiltModules;
import com.example.watchfinder.viewmodels.MyContentVM;
import com.example.watchfinder.viewmodels.MyContentVM_HiltModules;
import com.example.watchfinder.viewmodels.ProfileVM;
import com.example.watchfinder.viewmodels.ProfileVM_HiltModules;
import com.example.watchfinder.viewmodels.RegisterVM;
import com.example.watchfinder.viewmodels.RegisterVM_HiltModules;
import com.example.watchfinder.viewmodels.ResetPasswordVM;
import com.example.watchfinder.viewmodels.ResetPasswordVM_HiltModules;
import com.example.watchfinder.viewmodels.SearchVM;
import com.example.watchfinder.viewmodels.SearchVM_HiltModules;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerWatchFinderApp_HiltComponents_SingletonC {
  private DaggerWatchFinderApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public WatchFinderApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements WatchFinderApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements WatchFinderApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements WatchFinderApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements WatchFinderApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements WatchFinderApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements WatchFinderApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements WatchFinderApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public WatchFinderApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends WatchFinderApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends WatchFinderApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends WatchFinderApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends WatchFinderApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(10).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DetailsVM, DetailsVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DiscoverMoviesVM, DiscoverMoviesVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DiscoverSeriesVM, DiscoverSeriesVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ForgotPasswordVM, ForgotPasswordVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_LoginVM, LoginVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_MyContentVM, MyContentVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ProfileVM, ProfileVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_RegisterVM, RegisterVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ResetPasswordVM, ResetPasswordVM_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_SearchVM, SearchVM_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectTokenManager(instance, singletonCImpl.provideTokenManagerProvider.get());
      MainActivity_MembersInjector.injectUserManager(instance, singletonCImpl.provideUserManagerProvider.get());
      MainActivity_MembersInjector.injectAuthRepository(instance, singletonCImpl.authRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_watchfinder_viewmodels_ProfileVM = "com.example.watchfinder.viewmodels.ProfileVM";

      static String com_example_watchfinder_viewmodels_DetailsVM = "com.example.watchfinder.viewmodels.DetailsVM";

      static String com_example_watchfinder_viewmodels_SearchVM = "com.example.watchfinder.viewmodels.SearchVM";

      static String com_example_watchfinder_viewmodels_ResetPasswordVM = "com.example.watchfinder.viewmodels.ResetPasswordVM";

      static String com_example_watchfinder_viewmodels_RegisterVM = "com.example.watchfinder.viewmodels.RegisterVM";

      static String com_example_watchfinder_viewmodels_LoginVM = "com.example.watchfinder.viewmodels.LoginVM";

      static String com_example_watchfinder_viewmodels_DiscoverSeriesVM = "com.example.watchfinder.viewmodels.DiscoverSeriesVM";

      static String com_example_watchfinder_viewmodels_MyContentVM = "com.example.watchfinder.viewmodels.MyContentVM";

      static String com_example_watchfinder_viewmodels_DiscoverMoviesVM = "com.example.watchfinder.viewmodels.DiscoverMoviesVM";

      static String com_example_watchfinder_viewmodels_ForgotPasswordVM = "com.example.watchfinder.viewmodels.ForgotPasswordVM";

      @KeepFieldType
      ProfileVM com_example_watchfinder_viewmodels_ProfileVM2;

      @KeepFieldType
      DetailsVM com_example_watchfinder_viewmodels_DetailsVM2;

      @KeepFieldType
      SearchVM com_example_watchfinder_viewmodels_SearchVM2;

      @KeepFieldType
      ResetPasswordVM com_example_watchfinder_viewmodels_ResetPasswordVM2;

      @KeepFieldType
      RegisterVM com_example_watchfinder_viewmodels_RegisterVM2;

      @KeepFieldType
      LoginVM com_example_watchfinder_viewmodels_LoginVM2;

      @KeepFieldType
      DiscoverSeriesVM com_example_watchfinder_viewmodels_DiscoverSeriesVM2;

      @KeepFieldType
      MyContentVM com_example_watchfinder_viewmodels_MyContentVM2;

      @KeepFieldType
      DiscoverMoviesVM com_example_watchfinder_viewmodels_DiscoverMoviesVM2;

      @KeepFieldType
      ForgotPasswordVM com_example_watchfinder_viewmodels_ForgotPasswordVM2;
    }
  }

  private static final class ViewModelCImpl extends WatchFinderApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DetailsVM> detailsVMProvider;

    private Provider<DiscoverMoviesVM> discoverMoviesVMProvider;

    private Provider<DiscoverSeriesVM> discoverSeriesVMProvider;

    private Provider<ForgotPasswordVM> forgotPasswordVMProvider;

    private Provider<LoginVM> loginVMProvider;

    private Provider<MyContentVM> myContentVMProvider;

    private Provider<ProfileVM> profileVMProvider;

    private Provider<RegisterVM> registerVMProvider;

    private Provider<ResetPasswordVM> resetPasswordVMProvider;

    private Provider<SearchVM> searchVMProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.detailsVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.discoverMoviesVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.discoverSeriesVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.forgotPasswordVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.loginVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.myContentVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.profileVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.registerVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.resetPasswordVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.searchVMProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(10).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DetailsVM, ((Provider) detailsVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DiscoverMoviesVM, ((Provider) discoverMoviesVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_DiscoverSeriesVM, ((Provider) discoverSeriesVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ForgotPasswordVM, ((Provider) forgotPasswordVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_LoginVM, ((Provider) loginVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_MyContentVM, ((Provider) myContentVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ProfileVM, ((Provider) profileVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_RegisterVM, ((Provider) registerVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_ResetPasswordVM, ((Provider) resetPasswordVMProvider)).put(LazyClassKeyProvider.com_example_watchfinder_viewmodels_SearchVM, ((Provider) searchVMProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_watchfinder_viewmodels_DiscoverMoviesVM = "com.example.watchfinder.viewmodels.DiscoverMoviesVM";

      static String com_example_watchfinder_viewmodels_SearchVM = "com.example.watchfinder.viewmodels.SearchVM";

      static String com_example_watchfinder_viewmodels_DetailsVM = "com.example.watchfinder.viewmodels.DetailsVM";

      static String com_example_watchfinder_viewmodels_MyContentVM = "com.example.watchfinder.viewmodels.MyContentVM";

      static String com_example_watchfinder_viewmodels_ResetPasswordVM = "com.example.watchfinder.viewmodels.ResetPasswordVM";

      static String com_example_watchfinder_viewmodels_DiscoverSeriesVM = "com.example.watchfinder.viewmodels.DiscoverSeriesVM";

      static String com_example_watchfinder_viewmodels_LoginVM = "com.example.watchfinder.viewmodels.LoginVM";

      static String com_example_watchfinder_viewmodels_RegisterVM = "com.example.watchfinder.viewmodels.RegisterVM";

      static String com_example_watchfinder_viewmodels_ForgotPasswordVM = "com.example.watchfinder.viewmodels.ForgotPasswordVM";

      static String com_example_watchfinder_viewmodels_ProfileVM = "com.example.watchfinder.viewmodels.ProfileVM";

      @KeepFieldType
      DiscoverMoviesVM com_example_watchfinder_viewmodels_DiscoverMoviesVM2;

      @KeepFieldType
      SearchVM com_example_watchfinder_viewmodels_SearchVM2;

      @KeepFieldType
      DetailsVM com_example_watchfinder_viewmodels_DetailsVM2;

      @KeepFieldType
      MyContentVM com_example_watchfinder_viewmodels_MyContentVM2;

      @KeepFieldType
      ResetPasswordVM com_example_watchfinder_viewmodels_ResetPasswordVM2;

      @KeepFieldType
      DiscoverSeriesVM com_example_watchfinder_viewmodels_DiscoverSeriesVM2;

      @KeepFieldType
      LoginVM com_example_watchfinder_viewmodels_LoginVM2;

      @KeepFieldType
      RegisterVM com_example_watchfinder_viewmodels_RegisterVM2;

      @KeepFieldType
      ForgotPasswordVM com_example_watchfinder_viewmodels_ForgotPasswordVM2;

      @KeepFieldType
      ProfileVM com_example_watchfinder_viewmodels_ProfileVM2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.watchfinder.viewmodels.DetailsVM 
          return (T) new DetailsVM(singletonCImpl.movieRepositoryProvider.get(), singletonCImpl.seriesRepositoryProvider.get(), singletonCImpl.userRepositoryProvider.get());

          case 1: // com.example.watchfinder.viewmodels.DiscoverMoviesVM 
          return (T) new DiscoverMoviesVM(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.movieRepositoryProvider.get());

          case 2: // com.example.watchfinder.viewmodels.DiscoverSeriesVM 
          return (T) new DiscoverSeriesVM(singletonCImpl.userRepositoryProvider.get(), singletonCImpl.seriesRepositoryProvider.get());

          case 3: // com.example.watchfinder.viewmodels.ForgotPasswordVM 
          return (T) new ForgotPasswordVM(singletonCImpl.authRepositoryProvider.get());

          case 4: // com.example.watchfinder.viewmodels.LoginVM 
          return (T) new LoginVM(singletonCImpl.authRepositoryProvider.get());

          case 5: // com.example.watchfinder.viewmodels.MyContentVM 
          return (T) new MyContentVM(singletonCImpl.userRepositoryProvider.get());

          case 6: // com.example.watchfinder.viewmodels.ProfileVM 
          return (T) new ProfileVM(singletonCImpl.userRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideUserManagerProvider.get(), singletonCImpl.provideAuthenticatedImageLoaderProvider.get(), singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.authRepositoryProvider.get());

          case 7: // com.example.watchfinder.viewmodels.RegisterVM 
          return (T) new RegisterVM(singletonCImpl.authRepositoryProvider.get());

          case 8: // com.example.watchfinder.viewmodels.ResetPasswordVM 
          return (T) new ResetPasswordVM(singletonCImpl.authRepositoryProvider.get());

          case 9: // com.example.watchfinder.viewmodels.SearchVM 
          return (T) new SearchVM(singletonCImpl.genreRepositoryProvider.get(), singletonCImpl.movieRepositoryProvider.get(), singletonCImpl.seriesRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends WatchFinderApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends WatchFinderApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends WatchFinderApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<TokenManager> provideTokenManagerProvider;

    private Provider<AInterceptor> provideAuthInterceptorProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<ImageLoader> provideImageLoaderProvider;

    private Provider<UserManager> provideUserManagerProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<ApiService> provideApiServiceProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<MovieRepository> movieRepositoryProvider;

    private Provider<SeriesRepository> seriesRepositoryProvider;

    private Provider<UserRepository> userRepositoryProvider;

    private Provider<coil.ImageLoader> provideAuthenticatedImageLoaderProvider;

    private Provider<GenreRepository> genreRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideTokenManagerProvider = DoubleCheck.provider(new SwitchingProvider<TokenManager>(singletonCImpl, 3));
      this.provideAuthInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<AInterceptor>(singletonCImpl, 2));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 1));
      this.provideImageLoaderProvider = DoubleCheck.provider(new SwitchingProvider<ImageLoader>(singletonCImpl, 0));
      this.provideUserManagerProvider = DoubleCheck.provider(new SwitchingProvider<UserManager>(singletonCImpl, 4));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 7));
      this.provideApiServiceProvider = DoubleCheck.provider(new SwitchingProvider<ApiService>(singletonCImpl, 6));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 5));
      this.movieRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MovieRepository>(singletonCImpl, 8));
      this.seriesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SeriesRepository>(singletonCImpl, 9));
      this.userRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 10));
      this.provideAuthenticatedImageLoaderProvider = DoubleCheck.provider(new SwitchingProvider<coil.ImageLoader>(singletonCImpl, 11));
      this.genreRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<GenreRepository>(singletonCImpl, 12));
    }

    @Override
    public void injectWatchFinderApp(WatchFinderApp watchFinderApp) {
      injectWatchFinderApp2(watchFinderApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private WatchFinderApp injectWatchFinderApp2(WatchFinderApp instance) {
      WatchFinderApp_MembersInjector.injectImageLoaderProvider(instance, provideImageLoaderProvider);
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // coil3.ImageLoader 
          return (T) CoilModule_ProvideImageLoaderFactory.provideImageLoader(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideOkHttpClientProvider.get());

          case 1: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(singletonCImpl.provideAuthInterceptorProvider.get());

          case 2: // com.example.watchfinder.api.AInterceptor 
          return (T) NetworkModule_ProvideAuthInterceptorFactory.provideAuthInterceptor(singletonCImpl.provideTokenManagerProvider.get());

          case 3: // com.example.watchfinder.data.prefs.TokenManager 
          return (T) AppModule_ProvideTokenManagerFactory.provideTokenManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.example.watchfinder.data.UserManager 
          return (T) AppModule_ProvideUserManagerFactory.provideUserManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.example.watchfinder.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.provideTokenManagerProvider.get(), singletonCImpl.provideUserManagerProvider.get());

          case 6: // com.example.watchfinder.api.ApiService 
          return (T) NetworkModule_ProvideApiServiceFactory.provideApiService(singletonCImpl.provideRetrofitProvider.get());

          case 7: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 8: // com.example.watchfinder.repository.MovieRepository 
          return (T) new MovieRepository(singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.provideTokenManagerProvider.get(), new Utils());

          case 9: // com.example.watchfinder.repository.SeriesRepository 
          return (T) new SeriesRepository(singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.provideTokenManagerProvider.get(), new Utils());

          case 10: // com.example.watchfinder.repository.UserRepository 
          return (T) new UserRepository(singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.provideTokenManagerProvider.get(), new Utils());

          case 11: // coil.ImageLoader 
          return (T) NetworkModule_ProvideAuthenticatedImageLoaderFactory.provideAuthenticatedImageLoader(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideOkHttpClientProvider.get());

          case 12: // com.example.watchfinder.repository.GenreRepository 
          return (T) new GenreRepository(singletonCImpl.provideApiServiceProvider.get(), singletonCImpl.provideTokenManagerProvider.get(), singletonCImpl.provideUserManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
