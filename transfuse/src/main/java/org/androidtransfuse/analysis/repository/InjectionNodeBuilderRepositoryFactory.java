package org.androidtransfuse.analysis.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import com.google.common.collect.ImmutableMap;
import com.sun.codemodel.JExpr;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTStringType;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.GeneratedProviderInjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.util.matcher.ASTMatcherBuilder;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class InjectionNodeBuilderRepositoryFactory {

    public static final java.lang.String POWER_SERVICE = "power";
    public static final java.lang.String WINDOW_SERVICE = "window";
    public static final java.lang.String LAYOUT_INFLATER_SERVICE = "layout_inflater";
    public static final java.lang.String ACCOUNT_SERVICE = "account";
    public static final java.lang.String ACTIVITY_SERVICE = "activity";
    public static final java.lang.String ALARM_SERVICE = "alarm";
    public static final java.lang.String NOTIFICATION_SERVICE = "notification";
    public static final java.lang.String ACCESSIBILITY_SERVICE = "accessibility";
    public static final java.lang.String KEYGUARD_SERVICE = "keyguard";
    public static final java.lang.String LOCATION_SERVICE = "location";
    public static final java.lang.String SEARCH_SERVICE = "search";
    public static final java.lang.String SENSOR_SERVICE = "sensor";
    public static final java.lang.String STORAGE_SERVICE = "storage";
    public static final java.lang.String WALLPAPER_SERVICE = "wallpaper";
    public static final java.lang.String VIBRATOR_SERVICE = "vibrator";
    public static final java.lang.String CONNECTIVITY_SERVICE = "connectivity";
    public static final java.lang.String WIFI_SERVICE = "wifi";
    public static final java.lang.String WIFI_P2P_SERVICE = "wifip2p";
    public static final java.lang.String NSD_SERVICE = "servicediscovery";
    public static final java.lang.String AUDIO_SERVICE = "audio";
    public static final java.lang.String MEDIA_ROUTER_SERVICE = "media_router";
    public static final java.lang.String TELEPHONY_SERVICE = "phone";
    public static final java.lang.String CLIPBOARD_SERVICE = "clipboard";
    public static final java.lang.String INPUT_METHOD_SERVICE = "input_method";
    public static final java.lang.String TEXT_SERVICES_MANAGER_SERVICE = "textservices";
    public static final java.lang.String DROPBOX_SERVICE = "dropbox";
    public static final java.lang.String DEVICE_POLICY_SERVICE = "device_policy";
    public static final java.lang.String UI_MODE_SERVICE = "uimode";
    public static final java.lang.String DOWNLOAD_SERVICE = "download";
    public static final java.lang.String NFC_SERVICE = "nfc";
    public static final java.lang.String USB_SERVICE = "usb";
    public static final java.lang.String INPUT_SERVICE = "input";

    private final Map<ASTType, InjectionNodeBuilder> moduleConfiguration = new HashMap<ASTType, InjectionNodeBuilder>();
    private final ImmutableMap<String, ASTType> systemServices;
    private final Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final ASTMatcherBuilder astMatcherBuilder;

    @Inject
    public InjectionNodeBuilderRepositoryFactory(Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider,
                                                 InjectionBindingBuilder injectionBindingBuilder,
                                                 ASTMatcherBuilder astMatcherBuilder) {
        this.generatedProviderInjectionNodeBuilderProvider = generatedProviderInjectionNodeBuilderProvider;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.astMatcherBuilder = astMatcherBuilder;

        ImmutableMap.Builder<String, ASTType> systemServiceBuilder = ImmutableMap.builder();
        //mapping to class by string to avoid differences in Android platform compilation problems.
        systemServiceBuilder.put(ACCESSIBILITY_SERVICE, new ASTStringType("android.view.accessibility.AccessibilityManager"));
        systemServiceBuilder.put(ACCOUNT_SERVICE, new ASTStringType("android.accounts.AccountManager"));
        systemServiceBuilder.put(ACTIVITY_SERVICE, new ASTStringType("android.app.ActivityManager"));
        systemServiceBuilder.put(ALARM_SERVICE, new ASTStringType("android.app.AlarmManager"));
        systemServiceBuilder.put(AUDIO_SERVICE, new ASTStringType("android.media.AudioManager"));
        systemServiceBuilder.put(CLIPBOARD_SERVICE, new ASTStringType("android.text.ClipboardManager"));
        systemServiceBuilder.put(CONNECTIVITY_SERVICE, new ASTStringType("android.net.ConnectivityManager"));
        systemServiceBuilder.put(DEVICE_POLICY_SERVICE, new ASTStringType("android.app.admin.DevicePolicyManager"));
        systemServiceBuilder.put(DOWNLOAD_SERVICE, new ASTStringType("android.app.DownloadManager"));
        systemServiceBuilder.put(DROPBOX_SERVICE, new ASTStringType("android.os.DropBoxManager"));
        systemServiceBuilder.put(INPUT_METHOD_SERVICE, new ASTStringType("android.view.inputmethod.InputMethodManager"));
        systemServiceBuilder.put(INPUT_SERVICE, new ASTStringType("android.hardware.input.InputManager"));
        systemServiceBuilder.put(KEYGUARD_SERVICE, new ASTStringType("android.app.KeyguardManager"));
        systemServiceBuilder.put(LAYOUT_INFLATER_SERVICE, new ASTStringType("android.view.LayoutInflater"));
        systemServiceBuilder.put(LOCATION_SERVICE, new ASTStringType("android.location.LocationManager"));
        systemServiceBuilder.put(MEDIA_ROUTER_SERVICE, new ASTStringType("android.media.MediaRouter"));
        systemServiceBuilder.put(NFC_SERVICE, new ASTStringType("android.nfc.NfcManager"));
        systemServiceBuilder.put(NSD_SERVICE, new ASTStringType("android.net.nsd.NsdManager"));
        systemServiceBuilder.put(NOTIFICATION_SERVICE, new ASTStringType("android.app.NotificationManager"));
        systemServiceBuilder.put(POWER_SERVICE, new ASTStringType("android.os.PowerManager"));
        systemServiceBuilder.put(SEARCH_SERVICE, new ASTStringType("android.app.SearchManager"));
        systemServiceBuilder.put(SENSOR_SERVICE, new ASTStringType("android.hardware.SensorManager"));
        systemServiceBuilder.put(STORAGE_SERVICE, new ASTStringType("android.os.storage.StorageManager"));
        systemServiceBuilder.put(TELEPHONY_SERVICE, new ASTStringType("android.telephony.TelephonyManager"));
        systemServiceBuilder.put(TEXT_SERVICES_MANAGER_SERVICE, new ASTStringType("android.view.textservice.TextServicesManager"));
        systemServiceBuilder.put(UI_MODE_SERVICE, new ASTStringType("android.app.UiModeManager"));
        systemServiceBuilder.put(USB_SERVICE, new ASTStringType("android.hardware.usb.UsbManager"));
        systemServiceBuilder.put(VIBRATOR_SERVICE, new ASTStringType("android.os.Vibrator"));
        systemServiceBuilder.put(WALLPAPER_SERVICE, new ASTStringType("android.service.wallpaper.WallpaperService"));
        systemServiceBuilder.put(WIFI_P2P_SERVICE, new ASTStringType("android.net.wifi.p2p.WifiP2pManager"));
        systemServiceBuilder.put(WIFI_SERVICE, new ASTStringType("android.net.wifi.WifiManager"));
        systemServiceBuilder.put(WINDOW_SERVICE, new ASTStringType("android.view.WindowManager"));

        systemServices = systemServiceBuilder.build();
    }

    public void addApplicationInjections(InjectionNodeBuilderRepository repository) {
        //resources
        repository.putType(Resources.class, injectionBindingBuilder.dependency(android.app.Application.class).invoke(Resources.class, "getResources").build());

        //menu inflator
        repository.putType(MenuInflater.class, injectionBindingBuilder.dependency(android.app.Activity.class).invoke(MenuInflater.class, "getMenuInflater").build());

        //system services
        for (Map.Entry<String, ASTType> systemServiceEntry : systemServices.entrySet()) {
            repository.putType(systemServiceEntry.getValue(),
                    injectionBindingBuilder.dependency(Context.class).invoke(Object.class, "getSystemService").arg(JExpr.lit(systemServiceEntry.getKey())).build());
        }

        repository.putType(SharedPreferences.class,
                injectionBindingBuilder.staticInvoke(PreferenceManager.class, SharedPreferences.class, "getDefaultSharedPreferences").depenencyArg(Context.class).build());

    }

    public void addModuleConfiguration(InjectionNodeBuilderRepository repository) {
        for (Map.Entry<ASTType, InjectionNodeBuilder> astTypeInjectionNodeBuilderEntry : moduleConfiguration.entrySet()) {
            repository.putType(astTypeInjectionNodeBuilderEntry.getKey(), astTypeInjectionNodeBuilderEntry.getValue());
        }

        //provider type
        repository.putMatcher(astMatcherBuilder.type(Provider.class).ignoreGenerics().build(), generatedProviderInjectionNodeBuilderProvider.get());
    }

    public void putModuleConfig(ASTType type, InjectionNodeBuilder injectionNodeBuilder) {
        if(moduleConfiguration.containsKey(type)){
            throw new TransfuseAnalysisException("Binding for type already exists: " + type.toString());
        }
        moduleConfiguration.put(type, injectionNodeBuilder);
    }
}
