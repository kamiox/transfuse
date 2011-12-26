package org.androidrobotics.analysis.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ASTClassField implements ASTField {

    private Field field;
    private ASTType astType;

    public ASTClassField(Field field, ASTType astType) {
        this.field = field;
        this.astType = astType;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return field.getAnnotation(annotation);
    }

    @Override
    public List<ASTAnnotation> getAnnotations() {
        List<ASTAnnotation> annotationList = new ArrayList<ASTAnnotation>();

        for (Annotation annotation : field.getAnnotations()) {
            annotationList.add(new ASTClassAnnotation(annotation));
        }

        return annotationList;
    }
}