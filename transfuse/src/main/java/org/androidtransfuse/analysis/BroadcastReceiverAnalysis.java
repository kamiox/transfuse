package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.OnReceive;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.ContextScopeComponentBuilder;
import org.androidtransfuse.gen.componentBuilder.ObservesRegistrationGenerator;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.model.manifest.Receiver;
import org.androidtransfuse.processor.ManifestManager;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.AnnotationUtil.checkBlank;
import static org.androidtransfuse.util.AnnotationUtil.checkDefault;
import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * @author John Ericksen
 */
public class BroadcastReceiverAnalysis implements Analysis<ComponentDescriptor> {

    private final ASTClassFactory astClassFactory;
    private final Provider<Receiver> receiverProvider;
    private final ManifestManager manifestManager;
    private final ComponentBuilderFactory componentBuilderFactory;
    private final IntentFilterFactory intentFilterBuilder;
    private final MetaDataBuilder metaDataBuilder;
    private final ContextScopeComponentBuilder contextScopeComponentBuilder;
    private final ObservesRegistrationGenerator observesExpressionDecorator;

    @Inject
    public BroadcastReceiverAnalysis(ASTClassFactory astClassFactory,
                                     Provider<Receiver> receiverProvider,
                                     ManifestManager manifestManager,
                                     ComponentBuilderFactory componentBuilderFactory,
                                     IntentFilterFactory intentFilterBuilder,
                                     MetaDataBuilder metaDataBuilder,
                                     ContextScopeComponentBuilder contextScopeComponentBuilder,
                                     ObservesRegistrationGenerator observesExpressionDecorator) {
        this.astClassFactory = astClassFactory;
        this.receiverProvider = receiverProvider;
        this.manifestManager = manifestManager;
        this.componentBuilderFactory = componentBuilderFactory;
        this.intentFilterBuilder = intentFilterBuilder;
        this.metaDataBuilder = metaDataBuilder;
        this.contextScopeComponentBuilder = contextScopeComponentBuilder;
        this.observesExpressionDecorator = observesExpressionDecorator;
    }

    public ComponentDescriptor analyze(ASTType astType) {

        BroadcastReceiver broadcastReceiver = astType.getAnnotation(BroadcastReceiver.class);

        PackageClass receiverClassName;

        ComponentDescriptor receiverDescriptor = null;

        if (astType.extendsFrom(astClassFactory.getType(android.content.BroadcastReceiver.class))) {
            //vanilla Android broadcast receiver
            PackageClass activityPackageClass = astType.getPackageClass();
            receiverClassName = buildPackageClass(astType, activityPackageClass.getClassName());
        } else {
            receiverClassName = buildPackageClass(astType, broadcastReceiver.name());

            TypeMirror type = getTypeMirror(new ReceiverTypeRunnable(broadcastReceiver));
            String receiverType = buildReceiverType(type);

            receiverDescriptor = new ComponentDescriptor(receiverType, receiverClassName);

            receiverDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildBroadcastReceiverInjectionNodeFactory(astType));

            receiverDescriptor.setInitMethodBuilder(OnReceive.class, componentBuilderFactory.buildOnReceiveMethodBuilder());

            receiverDescriptor.addGenerators(contextScopeComponentBuilder);

            receiverDescriptor.addRegistration(observesExpressionDecorator);
        }

        setupManifest(receiverClassName.getFullyQualifiedName(), broadcastReceiver, astType);

        return receiverDescriptor;
    }

    private String buildReceiverType(TypeMirror type) {
        if (type != null) {
            return type.toString();
        } else {
            return android.content.BroadcastReceiver.class.getName();
        }
    }

    private void setupManifest(String name, BroadcastReceiver annotation, ASTType astType) {

        Receiver manifestReceiver = receiverProvider.get();

        manifestReceiver.setName(name);
        manifestReceiver.setLabel(checkBlank(annotation.label()));
        manifestReceiver.setProcess(checkBlank(annotation.process()));
        manifestReceiver.setPermission(checkBlank(annotation.permission()));
        manifestReceiver.setIcon(checkBlank(annotation.icon()));
        manifestReceiver.setEnabled(checkDefault(annotation.enabled(), true));
        manifestReceiver.setExported(checkDefault(annotation.exported(), true));

        manifestReceiver.setIntentFilters(intentFilterBuilder.buildIntentFilters(astType));
        manifestReceiver.setMetaData(metaDataBuilder.buildMetaData(astType));

        manifestManager.addBroadcastReceiver(manifestReceiver);
    }

    private PackageClass buildPackageClass(ASTType astType, String className) {
        PackageClass inputPackageClass = astType.getPackageClass();

        if (StringUtils.isBlank(className)) {
            return inputPackageClass.append("BroadcastReceiver");
        } else {
            return inputPackageClass.replaceName(className);
        }
    }

    private static final class ReceiverTypeRunnable extends TypeMirrorRunnable<BroadcastReceiver> {
        private ReceiverTypeRunnable(BroadcastReceiver receiverAnnotation) {
            super(receiverAnnotation);
        }

        @Override
        public void run(BroadcastReceiver annotation) {
            //accessing this throws an exception, caught in TypeMiirrorUtil
            annotation.type();
        }
    }
}
