package io.bootify.my_app.segment_availability;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Check that classCode is present and available when a new SegmentAvailability is created.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = SegmentAvailabilityClassCodeValid.SegmentAvailabilityClassCodeValidValidator.class
)
public @interface SegmentAvailabilityClassCodeValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class SegmentAvailabilityClassCodeValidValidator implements ConstraintValidator<SegmentAvailabilityClassCodeValid, String> {

        private final SegmentAvailabilityService segmentAvailabilityService;
        private final HttpServletRequest request;

        public SegmentAvailabilityClassCodeValidValidator(
                final SegmentAvailabilityService segmentAvailabilityService,
                final HttpServletRequest request) {
            this.segmentAvailabilityService = segmentAvailabilityService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("classCode");
            if (currentId != null) {
                // only relevant for new objects
                return true;
            }
            String error = null;
            if (value == null) {
                // missing input
                error = "NotNull";
            } else if (segmentAvailabilityService.classCodeExists(value)) {
                error = "Exists.segmentAvailability.classCode";
            }
            if (error != null) {
                cvContext.disableDefaultConstraintViolation();
                cvContext.buildConstraintViolationWithTemplate("{" + error + "}")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

    }

}
