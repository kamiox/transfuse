package org.androidtransfuse.gen.componentBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTParameter;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Ericksen
 */
public class MethodCallbackGenerator implements ExpressionVariableDependentGenerator {

    private final Class<? extends Annotation> eventAnnotation;
    private final MethodGenerator methodGenerator;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public MethodCallbackGenerator(@Assisted Class<? extends Annotation> eventAnnotation, @Assisted MethodGenerator methodGenerator, InvocationBuilder invocationBuilder) {
        this.eventAnnotation = eventAnnotation;
        this.methodGenerator = methodGenerator;
        this.invocationBuilder = invocationBuilder;
    }

    public void generate(JDefinedClass definedClass, MethodDescriptor creationMethodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor) {

        MethodDescriptor methodDescriptor = null;
        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);

            if (methodCallbackAspect != null && methodCallbackAspect.contains(eventAnnotation)) {
                Set<ASTMethod> methods = methodCallbackAspect.getListeners(eventAnnotation);

                //define method on demand for possible lazy init
                if (methodDescriptor == null) {
                    methodDescriptor = methodGenerator.buildMethod(definedClass);
                }
                JBlock body = methodDescriptor.getMethod().body();

                for (ASTMethod methodCallback : methods) {

                    List<ASTParameter> matchedParameters = matchMethodArguments(methodDescriptor.getASTMethod().getParameters(), methodCallback);
                    List<ASTType> parameterTypes = new ArrayList<ASTType>();
                    List<JExpression> parameters = new ArrayList<JExpression>();

                    for (ASTParameter matchedParameter : matchedParameters) {
                        parameterTypes.add(matchedParameter.getASTType());
                        parameters.add(methodDescriptor.getParameters().get(matchedParameter).getExpression());
                    }

                    JStatement methodCall = invocationBuilder.buildMethodCall(
                            methodCallback.getAccessModifier(),
                            methodDescriptor.getASTMethod().getReturnType(),
                            methodCallback.getName(),
                            parameters,
                            parameterTypes,
                            injectionNodeJExpressionEntry.getValue().getType(),
                            injectionNodeJExpressionEntry.getValue().getExpression()
                    );

                    body.add(methodCall);
                }
            }
        }

        methodGenerator.closeMethod(methodDescriptor);
    }

    private List<ASTParameter> matchMethodArguments(List<ASTParameter> parametersToMatch, ASTMethod methodToCall) {
        List<ASTParameter> arguments = new ArrayList<ASTParameter>();

        List<ASTParameter> overrideParameters = new ArrayList<ASTParameter>(parametersToMatch);

        for (ASTParameter callParameter : methodToCall.getParameters()) {
            Iterator<ASTParameter> overrideParameterIterator = overrideParameters.iterator();

            while (overrideParameterIterator.hasNext()) {
                ASTParameter overrideParameter = overrideParameterIterator.next();
                if (overrideParameter.getASTType().equals(callParameter.getASTType())) {
                    arguments.add(overrideParameter);
                    overrideParameterIterator.remove();
                    break;
                }
            }
        }

        return arguments;
    }
}
